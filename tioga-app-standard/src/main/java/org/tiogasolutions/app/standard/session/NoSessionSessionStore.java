package org.tiogasolutions.app.standard.session;

import javax.ws.rs.container.ContainerRequestContext;

public class NoSessionSessionStore implements SessionStore {

  @Override
  public Session getSession(ContainerRequestContext requestContext) {
    return null;
  }
}
