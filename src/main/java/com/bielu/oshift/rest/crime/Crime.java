package com.bielu.oshift.rest.crime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Crime {
  
  static final String SOLVED = "solved";
  static final String DATE = "date";
  static final String TITLE = "title";
  static final String ID = "id";
  
  UUID id;
  String title;
  Boolean solved;
  Date date;
  
  static Crime fromDBObject(DBObject res) {
    Crime crime = new Crime();
    crime.id = UUID.fromString(res.get(ID).toString());
    crime.title = res.get(TITLE).toString();
    try {
      crime.date = dateFormat().parse(res.get(DATE).toString());
    } catch (ParseException e) {
      crime.date = new Date();
    }
    crime.solved = (Boolean) res.get(SOLVED);
    return crime;
  }

  static DBObject fromCrime(Crime crime) {
    return new BasicDBObject(ID, crime.id.toString())
      .append(TITLE, crime.title)
      .append(DATE, dateFormat().format(crime.date))
      .append(SOLVED, crime.solved);
  }

  static DateFormat dateFormat() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm");  
  }
}
