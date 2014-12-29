package com.bielu.oshift.rest.crime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CrimeServiceStatus {
  
  CrimeServiceStatus() {
  }

  CrimeServiceStatus(String operation, String result, String details) {
    this.operation = operation;
    this.result = result;
    this.details = details;
  }

  @XmlAttribute
  String operation;
  
  @XmlAttribute
  String result;
  
  @XmlAttribute
  String details;
}
