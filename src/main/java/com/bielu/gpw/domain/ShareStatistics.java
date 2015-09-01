package com.bielu.gpw.domain;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.bielu.gpw.Util;

@XmlRootElement(name = "s")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShareStatistics extends AbstractStatistics {
  
  @XmlAttribute(name = "n") String name;
  @XmlAttribute(name = "iq") String initQuote;
  @XmlAttribute(name = "q") String quote;
  @XmlAttribute(name = "iv") String initValue;
  @XmlAttribute(name = "vnet") String valueNet;
  @XmlAttribute(name = "pnet") String profitNet;
  @XmlAttribute(name = "ptaxed") String profitNetTaxed;

  public ShareStatistics() {
  }

  public ShareStatistics(ShareInfo initialShare, ShareInfo currentShare) {
    name = initialShare.getName();
    initValue = format(initialShare.value());
    valueNet = format(currentShare.netValue());
    BigDecimal profitNet = currentShare.netValue().subtract(initialShare.netValue());
    this.profitNet = format(profitNet);
    quote = format(currentShare.getQuote());
    initQuote = format(initialShare.getQuote());
    if (profitNet.intValue() > 0) {
      profitNetTaxed = format(profitNet.multiply(Util.NET_PROFIT_RATE));
    } else {
      profitNetTaxed = format(profitNet);
    }
  }
}
