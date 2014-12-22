package com.bielu.oshift.service.rest.crime;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

public class CrimeProtobufWriter implements MessageBodyWriter<Object> {
  
  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return Crime.class == type || (Collection.class.isAssignableFrom(type) && Crime.class == genericType);
  }

  @Override
  public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
      MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException,
      WebApplicationException {
    
    System.out.println(t);
    System.out.println(type);
    System.out.println(genericType);
  }
}
