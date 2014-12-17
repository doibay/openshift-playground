package com.bielu.oshift.service.rest;

import java.io.InputStream;

import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.spi.JsonProvider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@Path("geoIp")
@Produces({ "application/*" })
public class GeoIpService {

  @GET
  public GeoData getGeoIpData() {
    return getGeoIpData(null);
  }

  @GET
  @Path("{ip}")
  public GeoData getGeoIpData(@PathParam("ip") String ip) {
    Client client = ClientBuilder.newClient();
    String uri = "http://api.hostip.info/get_json.php?position=true";
    if (ip != null) {
      uri += "&ip=" + ip;
    }

    WebTarget target = client.target(uri);
    Response response = target.request().get();
    JsonReader reader = JsonProvider.provider().createReader(response.readEntity(InputStream.class));

    JsonObject jsonObj = reader.readObject();
    GeoData data = new GeoData();
    data.setCountry(jsonObj.getString("country_name"));
    data.setCountryCode(jsonObj.getString("country_code"));
    data.setCity(jsonObj.getString("city"));
    data.setIp(jsonObj.getString("ip"));
    if (jsonObj.isNull("lat") == false) {
      data.setLatitude(jsonObj.getString("lat"));
    }

    if (jsonObj.isNull("lng") == false) {
      data.setLongitude(jsonObj.getString("lng"));
    }
    return data;
  }
}
