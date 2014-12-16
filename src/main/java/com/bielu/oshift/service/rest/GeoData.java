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
  private String countryCode;
  private String city = "";

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
    if (country != null) {
      int leftIdx = country.indexOf("(");
      int rightIdx = country.indexOf(")");
      if (leftIdx > -1 && leftIdx < rightIdx) {
        countryCode = country.substring(leftIdx + 1, rightIdx);
        if (countryCode.length() != 2) {
          countryCode = null;
        }
      }
    }
  }

  @XmlAttribute
  public String getCounrtyCode() {
    return countryCode;
  }

  @XmlAttribute
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
}
