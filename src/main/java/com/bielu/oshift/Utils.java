package com.bielu.oshift;

public class Utils {

  public static String getenv(String variable, String... defaultValue) {
    String result = System.getenv(variable);
    if (result == null && defaultValue.length == 1) {
      return defaultValue[0];
    }
    
    return result;
  }
}
