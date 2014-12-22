package com.bielu.oshift.service.rest.registration;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("register")
public class RegistrationService {
  
  static Set<String> regSet = new LinkedHashSet<String>();
  static RandomAccessFile regFile;
  static String filename = System.getProperty("user.dir") + System.getProperty("file.separator") + "regids.txt";
  private static Logger log = Logger.getLogger(RegistrationService.class.getSimpleName());
  
  static {
    try {
      regFile = new RandomAccessFile(filename, "rw");
      log.info("Registration file is '" + filename + "'");
      String line = null;
      while ((line = regFile.readLine()) != null) {
        regSet.add(line);
      }
    } catch (IOException e) {
      log.log(Level.SEVERE, "Unable to open registration file '" + filename + "'", e);
    }
  }

  @POST
  public RegistrationResponse register(RegistrationRequest request) {
    if (regSet.contains(request.registrationId)) {
      return new RegistrationResponse("already_registered", request.registrationId);
    }

    if (addNewRegistration(request)) { 
      return new RegistrationResponse("success", request.registrationId);
    }

    return new RegistrationResponse("unable_to_register", request.registrationId);    
  }

  private boolean addNewRegistration(RegistrationRequest request) {
    if (regFile != null) {
      try {
        regFile.seek(regFile.length());
        regFile.writeBytes(request.registrationId + System.lineSeparator());
        return regSet.add(request.registrationId);
      } catch (IOException e) {
        log.log(Level.WARNING, "IO exception writing to registration file '" + filename + "'", e);
      }
    }
    return false;
  }
}
