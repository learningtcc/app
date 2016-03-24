package org.tiogasolutions.app.standard.execution;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

public class ExecutionContext<T> {

  private final String domainName;
  private final T domain;

  private final HttpHeaders headers;
  private final UriInfo uriInfo;
  private final Request request;
  private final SecurityContext securityContext;
  private final Providers providers;

  public ExecutionContext(String domainName, T domain, UriInfo uriInfo, HttpHeaders headers, Request request, SecurityContext securityContext, Providers providers) {
    this.domain = domain;
    this.domainName = domainName;

    this.uriInfo = uriInfo;
    this.headers = headers;
    this.request = request;
    this.securityContext = securityContext;
    this.providers = providers;
  }

  public T getDomain() {
    return domain;
  }

  public HttpHeaders getHeaders() {
    return headers;
  }

  public UriInfo getUriInfo() {
    return uriInfo;
  }

  public String getDomainName() {
    return domainName;
  }

  public Request getRequest() {
    return request;
  }

  public SecurityContext getSecurityContext() {
    return securityContext;
  }

  public Providers getProviders() {
    return providers;
  }
}
