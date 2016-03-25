package org.tiogasolutions.app.standard.jaxrs.auth;

import org.tiogasolutions.dev.common.exceptions.ApiException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

public abstract class TokenRequestFilterAuthenticator implements RequestFilterAuthenticator{

    public TokenRequestFilterAuthenticator() {
    }

    protected abstract SecurityContext validate(ContainerRequestContext requestContext, String token);

    @Override
    public final String getAuthenticationScheme() {
        return "TOKEN";
    }

    @Override
    public SecurityContext authenticate(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null) {
            throw ApiException.unauthorized("No \"Authorization\" header.");

        } else if (authHeader.startsWith("Token ") == false) {
            throw ApiException.unauthorized("Not \"Token\" authentication.");

        } else {
            String token = authHeader.substring(6);
            return validate(requestContext, token);
        }
    }
}
