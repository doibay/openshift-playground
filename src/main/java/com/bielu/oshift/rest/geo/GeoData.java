package com.bielu.oshift.rest.geo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GeoData {

  String latitude;
  String longitude;
  String country;
  String countryCode;
  String city;
  String ip;
}
