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
  
  List<ShareStatistics> shareList;
  @XmlAttribute BigDecimal initialValue;
  @XmlAttribute BigDecimal currentValueNet;
  @XmlAttribute BigDecimal currentProfitNet;
  
  public WalletStatistics() {
  }
  
  public WalletStatistics(Wallet initialWallet, Wallet currentWallet) {
    if (initialWallet == null || currentWallet == null) {
      return;
    }
    
    shareList = new ArrayList<>(initialWallet.size());
    for (int i = 0; i < initialWallet.size(); i++) {
      shareList.add(new ShareStatistics(initialWallet.getShareInfo(i), currentWallet.getShareInfo(i)));
    }
    
    initialValue = initialWallet.value();
    currentValueNet = currentWallet.netValue();
    currentProfitNet = currentValueNet.subtract(initialValue).multiply(Util.NET_PROFIT_RATE);
  }
}
