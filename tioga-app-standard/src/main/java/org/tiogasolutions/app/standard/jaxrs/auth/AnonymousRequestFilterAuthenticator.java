package org.tiogasolutions.app.standard.jaxrs.auth;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

public class AnonymousRequestFilterAuthenticator implements RequestFilterAuthenticator{

    public static final AnonymousRequestFilterAuthenticator SINGLETON = new AnonymousRequestFilterAuthenticator();

    private AnonymousRequestFilterAuthenticator() {
    }

    @Override
    public String getAuthenticationScheme() {
        return "ANONYMOUS";
    }

    @Override
    public SecurityContext authenticate(ContainerRequestContext requestContext) {
        return requestContext.getSecurityContext();
    }
}
