package com.bielu.gpw.listener;

import com.bielu.gpw.domain.Wallet;

public interface WalletChangeListener {
  void walletChanged(Wallet wallet);
}
