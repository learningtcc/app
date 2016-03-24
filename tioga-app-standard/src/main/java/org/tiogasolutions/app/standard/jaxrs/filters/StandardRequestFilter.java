package org.tiogasolutions.app.standard.jaxrs.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.app.standard.execution.ExecutionManager;
import org.tiogasolutions.app.standard.session.Session;
import org.tiogasolutions.app.standard.session.SessionStore;
import org.tiogasolutions.dev.common.exceptions.ApiException;
import org.tiogasolutions.dev.common.exceptions.ApiUnauthorizedException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Providers;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;

@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class StandardRequestFilter<T> implements ContainerRequestFilter {

  @Context
  private UriInfo uriInfo;

  @Context
  private HttpHeaders httpHeaders;

  @Context
  private Request request;

  @Context
  private SecurityContext securityContext;

  @Context
  private Providers providers;

  private final SessionStore sessionStore;
  private final ExecutionManager<T> executionManager;
  private final StandardRequestFilterConfig filterConfig;
  private final StandardRequestFilterDomainResolver<T> domainResolver;
  private final StandardRequestPrincipalResolver principalResolver;

  @Autowired
  public StandardRequestFilter(StandardRequestFilterConfig filterConfig, SessionStore sessionStore, ExecutionManager<T> executionManager, StandardRequestFilterDomainResolver<T> domainResolver, StandardRequestPrincipalResolver principalResolver) {
    this.filterConfig = filterConfig;

    this.sessionStore = sessionStore;
    this.executionManager = executionManager;

    this.domainResolver = domainResolver;
    this.principalResolver = principalResolver;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    try {
      Session session = sessionStore.getSession(requestContext);
      if (session == null && filterConfig.isSessionRequired()) {
        throw ApiException.unauthorized();
      }

      T domain = domainResolver.getDomain(requestContext);
      String domainName = domainResolver.getDomainName(requestContext);

      executionManager.newContext(domainName, domain, uriInfo, httpHeaders, request, securityContext, providers);

      requestContext.setSecurityContext(new StandardSecurityContext(
          requestContext.getSecurityContext(),
          principalResolver.getPrincipal(session),
          filterConfig.getAuthenticationScheme()));

      executionManager.getContext();

    } catch (ApiUnauthorizedException e) {
      if (filterConfig.isRedirectUnauthorized()) {
        URI uri = requestContext.getUriInfo()
            .getBaseUriBuilder()
            .path(filterConfig.getUnauthorizedPath())
            .queryParam(filterConfig.getUnauthorizedQueryParamName(),
                filterConfig.getUnauthorizedQueryParamValue()).build();

        Response response = Response.seeOther(uri).build();
        requestContext.abortWith(response);

      } else {
        Response response = Response.status(e.getStatusCode()).build();
        requestContext.abortWith(response);
      }
    } catch (ApiException e) {
      Response response = Response.status(e.getStatusCode()).build();
      requestContext.abortWith(response);
    }
  }

  private static class StandardSecurityContext implements SecurityContext {
    private final boolean secure;
    private final Principal principal;
    private final String authenticationScheme;

    public StandardSecurityContext(SecurityContext oldSecurityContext, Principal principal, String authenticationScheme) {
      this.principal = principal;
      this.secure = oldSecurityContext.isSecure();
      this.authenticationScheme = authenticationScheme;
    }

    @Override public boolean isUserInRole(String role) {
      return false;
    }
    @Override public boolean isSecure() {
      return secure;
    }
    @Override public String getAuthenticationScheme() {
      return authenticationScheme;
    }
    @Override public Principal getUserPrincipal() {
      return principal;
    }
  }
}
