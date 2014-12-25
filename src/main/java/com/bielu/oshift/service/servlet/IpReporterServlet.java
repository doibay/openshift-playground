package com.bielu.oshift.service.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/ip")
public class IpReporterServlet extends HttpServlet {

  private static final long serialVersionUID = 8346893959019237040L;
  
  @Override
  protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setHeader("IP-Address", req.getRemoteAddr());
    resp.setHeader("IP-Host", req.getRemoteHost());
  }

}
