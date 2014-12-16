package com.bielu.oshift.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("geoIp")
@Produces({"application/*"})
public class GeoIpService {

  @GET
  @Path("{ip}")
  public GeoData getGeoIpData(@PathParam("ip") String ip) {
    GeoData data = new GeoData();
    data.setCity("Villeneuve-Loubet");
    data.setCountry("FRANCE");
    data.setLatitude("43.646969");
    data.setLongitude("7.098418");
    return data;
  }
}
