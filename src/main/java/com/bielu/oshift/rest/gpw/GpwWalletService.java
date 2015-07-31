package com.bielu.oshift.rest.gpw;

import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.bielu.gpw.Util;
import com.bielu.gpw.domain.Wallet;
import com.bielu.gpw.domain.WalletStatistics;

@Path("gpwWallet")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
public class GpwWalletService {
  
  Logger logger = Logger.getLogger(this.getClass().getSimpleName());

  @Context
  private ServletContext context;
  
  @GET
  public WalletStatistics getWallet() {
    return new WalletStatistics((Wallet) context.getAttribute(Util.INITIAL_WALLET),
                                (Wallet) context.getAttribute(Util.CURRENT_WALLET));
  }
}
