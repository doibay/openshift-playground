package com.bielu.oshift.rest.gpw;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.bielu.gpw.Util;
import com.bielu.gpw.domain.Wallet;

@Path("gpwWallet")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
public class GpwWalletService {
  
  Logger logger = Logger.getLogger(this.getClass().getSimpleName());

  @Context
  private ServletContext context;
  
  @GET
  public Map<String, Wallet> getWallet() {
    Map<String, Wallet> result = new HashMap<>();
    if (context == null) {
      logger.warning("ServletContext is null");
      return result;
    }
    
    result.put("INITIAL", (Wallet) context.getAttribute(Util.INITIAL_WALLET));
    result.put("CURRENT", (Wallet) context.getAttribute(Util.CURRENT_WALLET));
    return result;
  }
}
