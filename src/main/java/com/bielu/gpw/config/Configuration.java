package com.bielu.gpw.config;

import java.net.URL;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Configuration {

  private static final String CONFIGURATION_FILE = "/gpw-configuration.xml";
  private static final Log LOG = LogFactory.getLog(Configuration.class);
  private final XMLConfiguration config;
  private final String recommendationsFile;
  private final String quotesUrl;
  private final String mbankFundsUrl;

  private static final Configuration CONFIGURATION = new Configuration();

  public static Configuration getInstance() {
    return CONFIGURATION;
  }

  public String getRecommendationsFile() {
    return recommendationsFile;
  }

  public String getQuotesUrl() {
    return quotesUrl;
  }

  public String getMbankFundsUrl() {
    return mbankFundsUrl;
  }

  
  private Configuration() {
    config = new XMLConfiguration();
    try {
      URL url = Thread.currentThread().getContextClassLoader().getResource(CONFIGURATION_FILE);
      if (url == null) {
        LOG.error(String.format("Unable to locate %s file with configuration", CONFIGURATION_FILE));
        throw new IllegalStateException(
            String.format("Unable to locate %s file with configuration", CONFIGURATION_FILE));
      }
      config.load(url.openStream());
      config.setExpressionEngine(new XPathExpressionEngine());
    } catch (Exception e) {
      throw new IllegalStateException("Unable to initialize configuration", e);
    }

    recommendationsFile = config.getString("recommendation/@fileName");
    quotesUrl = config.getString("quotes/@url");
    mbankFundsUrl = config.getString("mbankFunds/@url");

    config.clear();
  }
}
