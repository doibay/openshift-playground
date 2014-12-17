package com.bielu.oshift.service.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GeoData {

  private String latitude = "";
  private String longitude = "";
  private String country = "";
  private String countryCode = "";
  private String city = "";
  private String ip = "";

  @XmlAttribute
  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  @XmlAttribute
  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  @XmlAttribute
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @XmlAttribute
  public String getCounrtyCode() {
    return countryCode;
  }
  
  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  @XmlAttribute
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
  
  @XmlAttribute
  public String getIp() {
    return ip;
  }
  
  public void setIp(String ip) {
    this.ip = ip;
  }
}
