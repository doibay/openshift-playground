package com.bielu.oshift.rest.crime;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
@Produces({"application/xml", "application/xml+fastinfoset", "application/x+protobuf"})
@Consumes({"application/xml", "application/xml+fastinfoset", "application/x+protobuf"})
public class CrimeService {
  
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

  @DELETE
  @Path("{id}")
  public Response deleteCrime(@PathParam("id") String id) {
    try (DBCursor cursor = collection.find(new BasicDBObject(Crime.ID, id))) {
      if (cursor.hasNext() == false) {
        return notFound("delete", id);
      }
      
      WriteResult result = collection.remove(new BasicDBObject(Crime.ID, id));
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
  
  @POST
  public Response updateCrime(Crime crime) {
    try (DBCursor cursor = collection.find(new BasicDBObject(Crime.ID, crime.id.toString()))) {
      if (cursor.hasNext() == false) {
        return notFound("update", crime.id.toString());
      }
      
      DBObject query = cursor.next();
      DBObject updated = Crime.fromCrime(crime);
      WriteResult result = collection.update(query, updated);
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

  @PUT
  public Response addCrime(Crime crime) {
    try (DBCursor cursor = collection.find(new BasicDBObject(Crime.ID, crime.id.toString()))) {
      if (cursor.hasNext()) {
        return Response.status(Status.CONFLICT)
            .entity(new CrimeServiceStatus("add", "error", "Crime with given UUID already exists"))
            .build();
      }

      WriteResult result = collection.insert(Crime.fromCrime(crime));
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
}
