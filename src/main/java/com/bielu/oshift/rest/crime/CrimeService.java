package com.bielu.oshift.rest.crime;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.bielu.oshift.Utils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

@Path("crime")
public class CrimeService {
  
  DBCollection collection;
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

  public CrimeService() throws UnknownHostException {
    MongoClient mongo = new MongoClient(
        new MongoClientURI(
            "mongodb://" 
                + Utils.getenv("OPENSHIFT_MONGODB_DB_USER", "admin")
                + ":"
                + Utils.getenv("OPENSHIFT_MONGODB_DB_PASS")
                + "@"
                + Utils.getenv("OPENSHIFT_MONGODB_DB_HOST", "localhost") 
                + ":" 
                + Utils.getenv("OPENSHIFT_MONGODB_DB_PORT", "27017")));
    
    collection = mongo.getDB(Utils.getenv("OPENSHIFT_MONGODB_DB_NAME", "playground")).getCollection("crimes");
  }

  @GET
  @Path("{id}")
  public Crime getCrime(@PathParam("id") String id) {
    try (DBCursor cursor = collection.find(new BasicDBObject("id", id))) {
      if (cursor.hasNext()) {
        return buildCrime(cursor.next());
      }
    }
    
    return null;
  }
  
  @POST
  public Response addCrime(Crime crime) {
    try {
      try (DBCursor cursor = collection.find(new BasicDBObject("id", crime.id))) {
        if (cursor.hasNext()) {
          return Response.status(Status.CONFLICT)
              .entity(new CrimeServiceStatus("add", "error", "Crime with given UUID already exists"))
              .build();
        }
      }
      
      WriteResult result = collection.insert(new BasicDBObject("id", crime.id)
          .append("title", crime.title)
          .append("date", dateFormat.format(crime.date))
          .append("solved", crime.solved));
      
      if (result.getError() != null) {
        return Response.status(Status.INTERNAL_SERVER_ERROR)
            .entity(new CrimeServiceStatus("add", "error", result.getError()))
            .build();
      }
      return Response.ok(new CrimeServiceStatus("add", "success", crime.id.toString())).build();
    } catch (MongoException e) {
      return Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity(new CrimeServiceStatus("add", "error", e.toString()))
          .build();
    }
  }
  
  @GET
  public List<Crime> getCrimeList() {
    List<Crime> list = new ArrayList<>();
    try (DBCursor cursor = collection.find()) {
      while (cursor.hasNext()) {
        DBObject res = cursor.next();
        list.add(buildCrime(res));
      }
    }
    
    return list;
  }

  Crime buildCrime(DBObject res) {
    Crime crime = new Crime();
    crime.id = UUID.fromString(res.get("id").toString());
    crime.title = res.get("title").toString();
    try {
      crime.date = dateFormat.parse(res.get("date").toString());
    } catch (ParseException e) {
      // ignore (?)
    }
    crime.solved = (Boolean) res.get("solved");
    return crime;
  }
}