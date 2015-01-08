package com.bielu.oshift.rest.crime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.bielu.oshift.protobuf.CrimesProto;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Crime {
  
  static final String SOLVED = "solved";
  static final String DATE = "date";
  static final String TITLE = "title";
  static final String ID = "id";
  
  private Crime() {
  }
  
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

  DBObject toDBObject() {
    return new BasicDBObject(ID, this.id.toString())
      .append(TITLE, this.title)
      .append(DATE, dateFormat().format(this.date))
      .append(SOLVED, this.solved);
  }

  static DateFormat dateFormat() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm");  
  }
  
  static Crime fromProtoBuf(CrimesProto.CrimeList.Crime crime) {
    return null;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Crime other = (Crime) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }
}
