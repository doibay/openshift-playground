package com.bielu.gpw.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "s")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShareStatistics extends AbstractStatistics {
  
  @XmlAttribute(name = "n") String name;
  @XmlAttribute(name = "q") String quote;
  @XmlAttribute(name = "iq") String initQuote;

  public ShareStatistics() {
  }

  public ShareStatistics(ShareInfo initialShare, ShareInfo currentShare) {
    super(initialShare, currentShare);
    name = initialShare.getName();
    quote = format(currentShare.getQuote());
    initQuote = format(initialShare.getQuote());
  }
}
