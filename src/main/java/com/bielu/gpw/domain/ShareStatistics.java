package com.bielu.gpw.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.bielu.gpw.Util;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ShareStatistics extends AbstractStatistics {
  
  @XmlAttribute String name;
  @XmlAttribute String initQuote;
  @XmlAttribute String quote;  
  @XmlAttribute String initValue;
  @XmlAttribute String valueNet;
  @XmlAttribute String profitNet;

  public ShareStatistics() {
  }

  public ShareStatistics(ShareInfo initialShare, ShareInfo currentShare) {
    name = initialShare.getName();
    initValue = format(initialShare.value());
    valueNet = format(currentShare.netValue());
    profitNet = format(currentShare.netValue().subtract(initialShare.value()).multiply(Util.NET_PROFIT_RATE));
    quote = format(currentShare.getQuote());
    initQuote = format(initialShare.getQuote());
  }

}
