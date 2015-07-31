package com.bielu.gpw.domain;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.bielu.gpw.Util;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ShareStatistics {
  
  @XmlAttribute String name;
  @XmlAttribute BigDecimal initQuote;
  @XmlAttribute BigDecimal quote;  
  @XmlAttribute BigDecimal initValue;
  @XmlAttribute BigDecimal valueNet;
  @XmlAttribute BigDecimal profitNet;

  public ShareStatistics() {
  }

  public ShareStatistics(ShareInfo initialShare, ShareInfo currentShare) {
    name = initialShare.getName();
    initValue = initialShare.value();
    valueNet = currentShare.netValue();
    profitNet = valueNet.subtract(initValue).multiply(Util.NET_PROFIT_RATE);
    quote = currentShare.getQuote();
    initQuote = initialShare.getQuote();
  }

}
