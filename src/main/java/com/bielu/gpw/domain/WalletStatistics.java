package com.bielu.gpw.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "wallet")
@XmlAccessorType(XmlAccessType.FIELD)
public class WalletStatistics extends AbstractStatistics {
  
  @XmlElement(name = "s") List<ShareStatistics> share;
  
  public WalletStatistics() {
  }
  
  public WalletStatistics(Wallet initialWallet, Wallet currentWallet) {
    super(initialWallet, currentWallet);
    if (initialWallet == null || currentWallet == null) {
      return;
    }
    
    share = new ArrayList<>(initialWallet.size());
    for (int i = 0; i < initialWallet.size(); i++) {
      share.add(new ShareStatistics(initialWallet.getShareInfo(i), currentWallet.getShareInfo(i)));
    }
  }
}
