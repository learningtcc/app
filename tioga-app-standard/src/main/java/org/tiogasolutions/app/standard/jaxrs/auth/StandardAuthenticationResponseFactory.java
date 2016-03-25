package org.tiogasolutions.app.standard.jaxrs.auth;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

public interface StandardAuthenticationResponseFactory {

    Response createForbiddenResponse(ContainerRequestContext requestContext);
    Response createUnauthorizedResponse(ContainerRequestContext requestContext, String authenticationScheme);

}
