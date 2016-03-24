package org.tiogasolutions.app.standard.jaxrs.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.app.standard.execution.ExecutionManager;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import java.io.IOException;

public interface StandardRequestFilterDomainResolver<T> {

  public T getDomain();
  public String getDomainName();

}
