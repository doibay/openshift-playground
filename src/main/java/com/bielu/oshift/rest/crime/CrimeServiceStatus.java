package com.bielu.oshift.rest.crime;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CrimeServiceStatus {
  
  CrimeServiceStatus() {
  }

  CrimeServiceStatus(String operation, String result, Map<QName, String> details) {
    this.operation = operation;
    this.result = result;
    this.details = details;
  }

  @SuppressWarnings("serial")
  CrimeServiceStatus(String operation, String result, final String details) {
    this(operation, result, new HashMap<QName, String>() {
      {
        put(new QName("details"), details);
      }
    });
  }

  @XmlAttribute
  String operation;
  
  @XmlAttribute
  String result;
  
  @XmlAnyAttribute
  Map<QName, String> details = new HashMap<QName, String>();
}
