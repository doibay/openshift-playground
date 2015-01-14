package com.bielu.oshift.rest.crime;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.namespace.QName;

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
@Produces({"application/json", "application/xml", "application/xml+fastinfoset", "application/x-protobuf"})
@Consumes({"application/json", "application/xml", "application/xml+fastinfoset", "application/x-protobuf"})
public class CrimeService {
  
  Logger logger = Logger.getLogger(this.getClass().getSimpleName());
  DBCollection collection;

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
    try (DBCursor cursor = collection.find(new BasicDBObject(Crime.ID, id))) {
      if (cursor.hasNext()) {
        return Crime.fromDBObject(cursor.next());
      }
    }
    
    return null;
  }
  
  WriteResult deleteCrime0(String id) throws MongoException {
    try (DBCursor cursor = collection.find(new BasicDBObject(Crime.ID, id))) {
      if (cursor.hasNext() == false) {
        return null;
      }
      
      return collection.remove(new BasicDBObject(Crime.ID, id));
    }
  }

  @DELETE
  @Path("{id}")
  public Response deleteCrime(@PathParam("id") String id) {
    try {
      WriteResult result = deleteCrime0(id);
      if (result == null) {
        return notFound("delete", id);
      }
      
      if (result.getError() != null) {
        return error("delete", result.getError());
      }
      
      return Response
          .ok(new CrimeServiceStatus("delete", "success", "Crime has been deleted"))
          .build();
    } catch (MongoException e) {
      return error("delete", e.toString());
    }
  }
  
  WriteResult updateCrime0(Crime crime) throws MongoException {
    try (DBCursor cursor = collection.find(new BasicDBObject(Crime.ID, crime.id.toString()))) {
      if (cursor.hasNext() == false) {
        return null;
      }
      
      return collection.update(cursor.next(), crime.toDBObject());
    }
  }
  
  @POST
  public Response updateCrime(Crime crime) {
    try {
      WriteResult result = updateCrime0(crime);
      if (result == null) {
        return notFound("update", crime.id.toString());
      }
      
      if (result.getError() != null) {
        return error("update", result.getError());
      }
      
      return Response
          .ok(new CrimeServiceStatus("update", "success", crime.id.toString()))
          .build();
    } catch (MongoException e) {
      return error("update", e.toString());
    }
  }
  
  @POST
  @Path("/sync")
  public Response syncCrimes(List<Crime> crimeList) {
    List<Crime> dbCrimes = getCrimeList();
    List<Crime> toRemove = new ArrayList<>();
    List<Crime> toAdd = new ArrayList<>();
    List<Crime> toUpdate = new ArrayList<>();
    for (Crime crime : dbCrimes) {
      if (crimeList.contains(crime) == false) {
        toRemove.add(crime);
      }
    }
    
    for (Crime crime : crimeList) {
      if (dbCrimes.contains(crime)) {
        toUpdate.add(crime);
      } else {
        toAdd.add(crime);
      }
    }
    
    int errors = 0;
    int updates = 0;
    int inserts = 0;
    int removals = 0;
    
    for (Crime crime : toRemove) {
      if (deleteCrime0(crime.id.toString()).getError() == null) {
        removals++;
      } else {
        logger.warning("Unable to remove crime with id '" + crime.id + "'.");
        errors++;
      }
    }
    
    for (Crime crime : toAdd) {
      if (addCrime0(crime).getError() == null) {
        inserts++;
      } else {
        logger.warning("Unable to add crime with id '" + crime.id + "'.");
        errors++;
      }
    }
    
    for (Crime crime : toUpdate) {
      if (updateCrime0(crime).getError() == null) {
        updates++;
      } else {
        logger.warning("Unable to update crime with id '" + crime.id + "'.");
        errors++;
      }
    }
    
    Map<QName, String> resMap = new HashMap<>();
    resMap.put(new QName("inserts"), "" + inserts);
    resMap.put(new QName("updates"), "" + updates);
    resMap.put(new QName("removals"), "" + removals);
    resMap.put(new QName("errors"), "" + errors);
    return Response.ok()
        .entity(new CrimeServiceStatus("sync", "success", resMap))
        .build();
  }
  
  WriteResult addCrime0(Crime crime) throws MongoException {
    try (DBCursor cursor = collection.find(new BasicDBObject(Crime.ID, crime.id.toString()))) {
      if (cursor.hasNext()) {
        return null;
      }
      
      return collection.insert(crime.toDBObject());
    }
  }

  @PUT
  public Response addCrime(Crime crime) {
    try {
      WriteResult result = addCrime0(crime);
      if (result == null) {
        return Response.status(Status.CONFLICT)
            .entity(new CrimeServiceStatus("add", "error", "Crime with given UUID already exists"))
            .build();
      }

      if (result.getError() != null) {
        return error("add", result.getError());
      }
      
      return Response.status(Status.CREATED)
          .entity(new CrimeServiceStatus("add", "success", crime.id.toString()))
          .build();
    } catch (MongoException e) {
      return error("add", e.toString());
    }
  }
  
  @GET
  public List<Crime> getCrimeList() {
    List<Crime> list = new ArrayList<>();
    try (DBCursor cursor = collection.find()) {
      while (cursor.hasNext()) {
        DBObject res = cursor.next();
        list.add(Crime.fromDBObject(res));
      }
    }
    
    return list;
  }
  
  private Response notFound(String operation, String uuid) {
    return Response.status(Status.NOT_FOUND)
        .entity(new CrimeServiceStatus(operation, "error", "Crime with given UUID '" + uuid + "' does not exist"))
        .build();
  }
  
  private Response error(String operation, String errorMessage) {
    return Response.status(Status.INTERNAL_SERVER_ERROR)
        .entity(new CrimeServiceStatus(operation, "error", errorMessage))
        .build();
  }
}
