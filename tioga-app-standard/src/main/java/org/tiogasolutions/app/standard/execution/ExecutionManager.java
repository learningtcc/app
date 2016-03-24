package org.tiogasolutions.app.standard.execution;

import org.springframework.stereotype.Component;
import org.tiogasolutions.dev.common.exceptions.ApiException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

@Component
public class ExecutionManager<T> implements ExecutionAccessor<T> {

  private final ThreadLocal<ExecutionContext<T>> threadLocal = new ThreadLocal<>();

  public ExecutionManager() {
  }

  public void clearContext() {
    threadLocal.remove();
  }

  public ExecutionContext newContext(String domainName, T domain, UriInfo uriInfo, HttpHeaders headers, Request request, SecurityContext securityContext, Providers providers) {
    ExecutionContext<T> context = createContext(domainName, domain, uriInfo, headers, request, securityContext, providers);
    assignContext(context);
    return context;
  }

  public ExecutionContext<T> createContext(String domainName, T domain, UriInfo uriInfo, HttpHeaders headers, Request request, SecurityContext securityContext, Providers providers) {
    return new ExecutionContext<>(domainName, domain, uriInfo, headers, request, securityContext, providers);
  }

  public void assignContext(ExecutionContext<T> context) {
    threadLocal.set(context);
  }

  @Override
  public boolean isContextSet() {
    return threadLocal.get() != null;
  }

  @Override
  public ExecutionContext<T> getContext() {
    ExecutionContext<T> context = threadLocal.get();
    if (context == null) {
      throw ApiException.internalServerError("There is no current execution context for this thread.");
    } else {
      return context;
    }
  }
}
