package com.bielu.oshift.service.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GeoData {

  @XmlAttribute
  String latitude = "";
  @XmlAttribute
  String longitude = "";
  @XmlAttribute
  String country = "";
  @XmlAttribute
  String countryCode = "";
  @XmlAttribute
  String city = "";
  @XmlAttribute
  String ip = "";

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCounrtyCode() {
    return countryCode;
  }
  
  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
  
  public String getIp() {
    return ip;
  }
  
  public void setIp(String ip) {
    this.ip = ip;
  }
}
