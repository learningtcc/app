package org.tiogasolutions.app.standard.jaxrs.filters;

import org.tiogasolutions.app.standard.session.Session;

import java.security.Principal;

public interface StandardRequestPrincipalResolver {

  public Principal getPrincipal(Session session);

}
