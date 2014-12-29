package com.bielu.oshift.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bielu.oshift.Utils;

@WebFilter("/*")
public class CheckSslFilter implements Filter {

  private boolean allowUnsecureAccess = false;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    allowUnsecureAccess = Boolean.parseBoolean(Utils.getenv("OPENSHIFT_ALLOW_UNSECURE_ACCESS", "false"));
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
      throws IOException, ServletException {

    if (allowUnsecureAccess || request.isSecure()) {
      chain.doFilter(request, response);
    } else {
      HttpServletResponse resp = (HttpServletResponse) response;
      HttpServletRequest req = (HttpServletRequest) request;
      resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
      resp.setHeader("Location", req.getRequestURL().toString().replace("http://", "https://"));
    }
  }

  @Override
  public void destroy() {
  }
}
