package org.tiogasolutions.app.standard.jaxrs.auth;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

public interface RequestFilterAuthenticator {

    SecurityContext authenticate(ContainerRequestContext requestContext);

}
