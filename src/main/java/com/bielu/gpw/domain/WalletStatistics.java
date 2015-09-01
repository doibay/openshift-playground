package com.bielu.gpw.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.bielu.gpw.Util;

@XmlRootElement(name = "wallet")
@XmlAccessorType(XmlAccessType.FIELD)
public class WalletStatistics extends AbstractStatistics {
  
  @XmlElementRef(name = "s") List<ShareStatistics> share;
  @XmlAttribute(name = "iv") String initValue;
  @XmlAttribute(name = "vnet") String valueNet;
  @XmlAttribute(name = "pnet") String profitNet;
  @XmlAttribute(name = "ptaxed") String profitNetTaxed;
  
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
    BigDecimal profitNet = currentWallet.netValue().subtract(initialWallet.netValue());
    this.profitNet = format(profitNet);
    if (profitNet.intValue() > 0) {
      profitNetTaxed = format(profitNet.multiply(Util.NET_PROFIT_RATE));
    } else {
      profitNetTaxed = format(profitNet);
    }
  }
}
