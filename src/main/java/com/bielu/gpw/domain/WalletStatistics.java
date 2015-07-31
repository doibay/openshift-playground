package com.bielu.gpw.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.bielu.gpw.Util;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WalletStatistics extends AbstractStatistics {
  
  List<ShareStatistics> share;
  @XmlAttribute String initValue;
  @XmlAttribute String valueNet;
  @XmlAttribute String profitNet;
  
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
    
    initValue = format(initialWallet.value());
    valueNet = format(currentWallet.netValue());
    profitNet = format(currentWallet.netValue().subtract(initialWallet.value()).multiply(Util.NET_PROFIT_RATE));
  }
}
