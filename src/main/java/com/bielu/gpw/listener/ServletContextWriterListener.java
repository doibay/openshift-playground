package com.bielu.gpw.listener;

import java.util.List;

import javax.servlet.ServletContext;

import com.bielu.gpw.Util;
import com.bielu.gpw.domain.Recommendation;
import com.bielu.gpw.domain.Wallet;

public class ServletContextWriterListener implements WalletChangeListener, RecommendationsChangeListener {

  private final ServletContext servletContext;

  public ServletContextWriterListener(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  @Override
  public void recommendationsChanged(List<Recommendation> recommendationList) {
    servletContext.setAttribute(Util.CURRENT_RECOMMENDATIONS, recommendationList);
  }

  @Override
  public void walletChanged(Wallet wallet) {
    servletContext.setAttribute(Util.CURRENT_WALLET, wallet);
  }
}