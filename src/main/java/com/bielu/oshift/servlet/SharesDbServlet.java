package com.bielu.oshift.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bielu.gpw.GpwMonitor;
import com.bielu.gpw.Util;

@WebServlet("/sharesDb")
public class SharesDbServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if (req.getParameter("content") != null) {
    }
    
    Util.writeToFile(req.getParameter("content"), GpwMonitor.SHARES_DB_FILE);
    resp.sendRedirect("/gpw.jsp");
  }
}
