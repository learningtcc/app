package org.tiogasolutions.app.standard.jaxrs.filters;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

@PreMatching
@Priority(1)
/** See https://java.net/jira/browse/JERSEY-2726 */
public class JerseySpringWorkaroundFilter extends RequestContextFilter {

  @Inject
  public JerseySpringWorkaroundFilter(ServiceLocator locator) {
    super(locator);
  }

}
