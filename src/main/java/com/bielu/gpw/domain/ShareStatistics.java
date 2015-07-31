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
  @XmlAttribute BigDecimal initialValue;
  @XmlAttribute BigDecimal currentValueNet;
  @XmlAttribute BigDecimal currentProfitNet;

  public ShareStatistics() {
  }

  public ShareStatistics(ShareInfo initialShare, ShareInfo currentShare) {
    name = initialShare.getName();
    initialValue = initialShare.value();
    currentValueNet = currentShare.netValue();
    currentProfitNet = currentValueNet.subtract(initialValue).multiply(Util.NET_PROFIT_RATE);
  }

}
