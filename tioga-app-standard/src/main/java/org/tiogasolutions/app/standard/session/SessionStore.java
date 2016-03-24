package org.tiogasolutions.app.standard.session;

import javax.ws.rs.container.ContainerRequestContext;

public interface SessionStore {

  Session getSession(ContainerRequestContext requestContext);

}
