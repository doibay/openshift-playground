package com.bielu.oshift;

import java.util.logging.Logger;

public class Utils {
  
  private static final Logger LOG = Logger.getLogger(Utils.class.getName());

  public static String getenv(String variable, String... defaultValue) {
    String result = System.getenv(variable);
    if (result == null) {
      LOG.warning("Environment variable '" + variable + "' not set. This might cause system instability.");
      if (defaultValue.length == 1) {
        return defaultValue[0];
      }
    }
    
    return result;
  }
}
