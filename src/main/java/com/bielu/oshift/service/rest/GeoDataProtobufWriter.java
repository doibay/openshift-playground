package com.bielu.oshift.service.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.bielu.oshift.service.protobuf.GeoDataOuterClass;

@Provider
@Produces("application/x-protobuf")
public class GeoDataProtobufWriter implements MessageBodyWriter<GeoData> {

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return GeoData.class.isAssignableFrom(type);
  }

  @Override
  public long getSize(GeoData t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(GeoData t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
      MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException,
      WebApplicationException {

    GeoDataOuterClass.GeoData.newBuilder()
      .setCity(t.getCity())
      .setCountry(t.getCountry())
      //.setCountryCode(t.getCounrtyCode())
      .setLatitude(t.getLatitude())
      .setLongitude(t.getLongitude())
      .build().writeTo(entityStream);
  }
  
  

}
