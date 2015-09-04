package com.bielu.gpw.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.xml.bind.annotation.XmlAttribute;

import com.bielu.gpw.Util;

public abstract class AbstractStatistics {

  @XmlAttribute(name = "iv") String initValue;
  @XmlAttribute(name = "vnet") String valueNet;
  @XmlAttribute(name = "pnet") String profitNet;
  @XmlAttribute(name = "ptaxed") String profitNetTaxed;
  @XmlAttribute(name = "r") String rate;

  public AbstractStatistics() {
  }

  protected AbstractStatistics(Investment initial, Investment current) {
    if (initial == null || current == null) {
      return;
    }

    initValue = format(initial.value());
    valueNet = format(current.netValue());
    BigDecimal profitNet = current.netValue().subtract(initial.netValue());
    this.profitNet = format(profitNet);
    if (profitNet.intValue() > 0) {
      profitNetTaxed = format(profitNet.multiply(Util.NET_PROFIT_RATE));
    } else {
      profitNetTaxed = format(profitNet);
    }

    rate = format(profitNet.divide(current.netValue(), RoundingMode.FLOOR).multiply(BigDecimal.valueOf(100))) + "%";
  }

  protected String format(BigDecimal value) {
    return String.format(Util.FORMAT_MONEY, value);
  }
}
