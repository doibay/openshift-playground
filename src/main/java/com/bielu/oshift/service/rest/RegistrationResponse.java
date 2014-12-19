package com.bielu.oshift.service.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RegistrationResponse {

  @XmlAttribute
  String status;
  
  @XmlAttribute(name = "registration_id")
  String regid;
  
  RegistrationResponse() {
  }
  
  RegistrationResponse(String status, String regid) {
    this.status = status;
    this.regid = regid;
  }
}
