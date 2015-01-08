package com.bielu.oshift.rest.crime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.bielu.oshift.protobuf.CrimesProto;
import com.bielu.oshift.protobuf.CrimesProto.CrimeList.Builder;

@Provider
@Produces("application/x-protobuf")
@Consumes("application/x-protobuf")
public class CrimeProtobufProvider implements MessageBodyWriter<Object>, MessageBodyReader<Object> {
  
  private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  
  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    if (Crime.class == type) {
      return true;
    }
    
    if (Collection.class.isAssignableFrom(type)) {
      if (genericType instanceof ParameterizedType) {
        ParameterizedType pt = (ParameterizedType) genericType;
        for (Type actual : pt.getActualTypeArguments()) {
          if (Crime.class == actual) {
            return true;
          }
        }
      }
    }
    
    return false;
  }

  @Override
  public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return isWriteable(type, genericType, annotations, mediaType);
  }

  @Override
  public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
      MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
    
    return Crime.fromProtoBuf(null);
  }
  
  @Override
  public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
      MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException,
      WebApplicationException {
    
    if (Crime.class == type) {
      Crime crime = (Crime) t;
      buildCrime(crime).writeTo(entityStream);
      
      return;
    }
    
    if (Collection.class.isAssignableFrom(type)) {
      @SuppressWarnings("unchecked")
      Collection<Crime> coll = (Collection<Crime>) t;
      Builder builder = CrimesProto.CrimeList.newBuilder();
      for (Crime crime : coll) {
        builder.addCrimes(buildCrime(crime));
      }
      
      builder.build().writeTo(entityStream);
    }
  }
  
  CrimesProto.CrimeList.Crime buildCrime(Crime crime) {
    return CrimesProto.CrimeList.Crime.newBuilder()
    .setDate(format.format(crime.date))
    .setSolved(crime.solved)
    .setTitle(crime.title)
    .setUuid(crime.id.toString())
    .build();
  }
}
