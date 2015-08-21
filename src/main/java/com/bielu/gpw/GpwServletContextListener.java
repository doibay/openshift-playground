package com.bielu.gpw;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bielu.gpw.domain.Wallet;
import com.bielu.gpw.listener.ChangeListener;
import com.bielu.gpw.listener.RecommendationsChangeListener;
import com.bielu.gpw.listener.ServletContextWriterListener;
import com.bielu.gpw.listener.WalletChangeListener;
import com.bielu.gpw.task.RecommendationReader;
import com.bielu.gpw.task.ShareQuoteReader;

@WebListener
public final class GpwServletContextListener implements ServletContextListener {

  private static final long TASK_PERIOD = 30L * 1000L;
  private static final int HOUR = 3600;
  private static final int TEN_SECONDS = 10;
  private static final Log LOG = LogFactory.getLog(GpwServletContextListener.class);

  private GpwMonitor gpwMon;
  private Timer timer;

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    gpwMon.close();
    timer.cancel();
  }
  
  @Override
  public void contextInitialized(final ServletContextEvent sce) {
    gpwMon = initialize(sce.getServletContext());
    if (gpwMon == null) {
      return;
    }
    gpwMon.hasDbChanged();

    timer = new Timer("Shares DB Modification Monitor");
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        if (gpwMon.hasDbChanged()) {
          gpwMon = initialize(sce.getServletContext());
          if (gpwMon == null) {
            cancel();
          }
        }
      }
    };
    timer.scheduleAtFixedRate(task, 0, TASK_PERIOD); // each minute
  }

  private static GpwMonitor initialize(ServletContext servletContext) {
    GpwMonitor monitor = new GpwMonitor();
    final Wallet myWallet = monitor.createWallet();
    if (myWallet == null || myWallet.size() == 0) {
      LOG.error("Wallet is empty - exiting application");
      return null;
    }
    servletContext.setAttribute(Util.INITIAL_WALLET, myWallet);

    ServletContextWriterListener listener = new ServletContextWriterListener(servletContext);
    final List<WalletChangeListener> quoteListeners = new ArrayList<>();
    final List<ChangeListener<Object>> objectListeners = new ArrayList<>();
    final List<RecommendationsChangeListener> recListeners = new ArrayList<>();
    quoteListeners.add(listener);
    recListeners.add(listener);
    monitor.scheduleAtFixedRate(new ShareQuoteReader(myWallet, quoteListeners, objectListeners), 0, 1, TimeUnit.MINUTES);
    monitor.scheduleAtFixedRate(new RecommendationReader(myWallet, recListeners, objectListeners), TEN_SECONDS,
        HOUR, TimeUnit.SECONDS);

    return monitor;
  }
}
