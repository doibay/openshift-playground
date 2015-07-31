package com.bielu.gpw.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.bielu.gpw.Util;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WalletStatistics {
  
  List<ShareStatistics> share;
  @XmlAttribute BigDecimal initValue;
  @XmlAttribute BigDecimal valueNet;
  @XmlAttribute BigDecimal profitNet;
  
  public WalletStatistics() {
  }
  
  public WalletStatistics(Wallet initialWallet, Wallet currentWallet) {
    if (initialWallet == null || currentWallet == null) {
      return;
    }
    
    share = new ArrayList<>(initialWallet.size());
    for (int i = 0; i < initialWallet.size(); i++) {
      share.add(new ShareStatistics(initialWallet.getShareInfo(i), currentWallet.getShareInfo(i)));
    }
    
    initValue = initialWallet.value();
    valueNet = currentWallet.netValue();
    profitNet = valueNet.subtract(initValue).multiply(Util.NET_PROFIT_RATE);
  }
}
