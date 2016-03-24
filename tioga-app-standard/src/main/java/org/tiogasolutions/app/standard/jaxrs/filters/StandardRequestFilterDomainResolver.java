package org.tiogasolutions.app.standard.jaxrs.filters;

import javax.ws.rs.container.ContainerRequestContext;

public interface StandardRequestFilterDomainResolver<T> {

  public T getDomain(ContainerRequestContext requestContext);
  public String getDomainName(ContainerRequestContext requestContext);

}
