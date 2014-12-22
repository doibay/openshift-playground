package com.bielu.oshift.service.rest.crime;

import java.util.Date;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Crime {

  UUID id;
  String title;
  Boolean solved;
  Date date;
}
