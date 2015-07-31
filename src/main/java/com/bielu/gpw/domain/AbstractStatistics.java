package com.bielu.gpw.domain;

import java.math.BigDecimal;

import com.bielu.gpw.Util;

public abstract class AbstractStatistics {

  protected String format(BigDecimal value) {
    return String.format(Util.FORMAT_MONEY, value);
  }
}
