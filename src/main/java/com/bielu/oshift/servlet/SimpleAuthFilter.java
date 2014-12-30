package com.bielu.oshift.servlet;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.resteasy.util.Base64;

import com.bielu.oshift.Utils;

@WebFilter("/*")
public class SimpleAuthFilter implements Filter {

  private final boolean allowUnsecureAccess;
  private final boolean allowAnonymousAccess;
  private final String user;
  private final String pass;
  private final MessageDigest digest;
  
  public SimpleAuthFilter() throws ServletException {
    allowUnsecureAccess = Boolean.parseBoolean(Utils.getenv("OPENSHIFT_ALLOW_UNSECURE_ACCESS", "false"));
    allowAnonymousAccess = Boolean.parseBoolean(Utils.getenv("OPENSHIFT_ALLOW_ANONYMOUS_ACCESS", "false"));
    user = Utils.getenv("OPENSHIFT_HTTP_USER", "xakJwSTdj65uByg/BBcRu4ACfLwHOoSdFRbwAfduUzQ="); // SHA-256 + Base64
    pass = Utils.getenv("OPENSHIFT_HTTP_PASS", "uQRM1l3GrjaQl8WTYwgXU4CaiPlZ6w0vr4WEGV/sZPY="); // SHA-256 + Base64
    try {
      digest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new ServletException("Unable to initialize SHA-256 message digester", e);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
      throws IOException, ServletException {

    HttpServletResponse resp = (HttpServletResponse) response;
    HttpServletRequest req = (HttpServletRequest) request;
    if (allowUnsecureAccess || request.isSecure()) {
      checkAuth(req, resp, chain);
    } else {
      resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
      resp.setHeader("Location", req.getRequestURL().toString().replace("http://", "https://"));
    }
  }

  void checkAuth(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
    if (allowAnonymousAccess || verifyUserAndPass(req)) {
      chain.doFilter(req, resp);
    } else {
      resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Request must be authenticated with username and password");
    }
  }
  
  private boolean verifyUserAndPass(HttpServletRequest req) {
    String usr = req.getHeader("X-Http-User");
    if (usr == null) {
      return false;
    }
    
    String pwd = req.getHeader("X-Http-Pass");
    if (pwd == null) {
      return false;
    }
    
    return user.equals(Base64.encodeBytes(digest.digest(usr.getBytes()))) 
        && pass.equals(Base64.encodeBytes(digest.digest(pwd.getBytes())));
  }

  @Override
  public void destroy() {
  }
}
