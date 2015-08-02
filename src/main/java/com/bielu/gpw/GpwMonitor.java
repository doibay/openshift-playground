package com.bielu.gpw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bielu.gpw.domain.ShareInfo;
import com.bielu.gpw.domain.ShareInfo.ShareBuilder;
import com.bielu.gpw.domain.ShareTypeEnum;
import com.bielu.gpw.domain.Wallet;

public class GpwMonitor implements Closeable {

  public static final String SHARES_DB_FILE = File.separator + "tmp" + File.separator + "shares.db";
  private static final Log LOG = LogFactory.getLog(GpwMonitor.class);
  private final ScheduledExecutorService service;
  private final SimpleDateFormat format;
  private File dbFile;
  private boolean exiting = false;

  protected GpwMonitor() {
    service = Executors.newScheduledThreadPool(2, new GpwThreadFactory("GpwMonitor"));
    format = new SimpleDateFormat("yyyy-MM-dd");
  }

  @Override
  public void close() {
    if (exiting == true) {
      return;
    }
    exiting = true;
    LOG.info("Exiting application");
    service.shutdownNow();

    try {
      service.awaitTermination(2, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      LOG.warn("awaitTermination interrupted", e);
    }
  }

  protected Wallet createWallet() {
    try {
      LineNumberReader in = new LineNumberReader(new InputStreamReader(findDb()));
      final List<ShareInfo> shares = new ArrayList<>();

      String line = null;
      while ((line = in.readLine()) != null) {
        // comment?
        if (line.trim().startsWith("#")) {
          continue;
        }
        
        String[] tmp = line.split(",");
        ShareBuilder builder = new ShareBuilder();
        builder.name = tmp[0].trim();
        builder.quote = Double.parseDouble(tmp[1].trim());
        builder.sharesCount = Double.parseDouble(tmp[2].trim());
        builder.value = -1;
        builder.startDate = format.parse(tmp[3].trim());
        if (tmp.length > 4) {
          builder.shareType = ShareTypeEnum.valueOf(tmp[4].trim());
        }
        ShareInfo share = builder.buildShareInfo();

        if (shares.contains(share)) {
          ShareInfo prev = shares.get(shares.indexOf(share));
          shares.remove(prev);
          shares.add(ShareInfo.merge(share, prev));
        } else {
          shares.add(share);
        }
      }
      in.close();
      return new Wallet(shares);
    } catch (Exception e) {
      LOG.error("Unable to read Wallet data.", e);
      return null;
    }
  }

  private InputStream findDb() throws FileNotFoundException {
    FileInputStream stream = new FileInputStream(SHARES_DB_FILE);
    dbFile = new File(SHARES_DB_FILE);
    return stream;
  }

  public long getLastDbModification() {
    if (dbFile == null) {
      return 0;
    }
    return dbFile.lastModified();
  }

  public void scheduleAtFixedRate(Runnable command, long initDelay, int period, TimeUnit unit) {
    service.scheduleAtFixedRate(command, initDelay, period, unit);
  }
}
