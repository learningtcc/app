package org.tiogasolutions.app.standard.jaxrs.auth;

import org.tiogasolutions.dev.common.exceptions.ApiUnauthorizedException;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

public abstract class TokenRequestFilterAuthenticator implements RequestFilterAuthenticator{

    protected abstract SecurityContext validate(ContainerRequestContext requestContext, String token);

    @Override
    public SecurityContext authenticate(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null) {
            throw new NotAuthorizedException("No \"Authorization\" header.");

        } else if (authHeader.startsWith("Token ") == false) {
            throw new NotAuthorizedException("Not \"Token\" authentication.");

        } else {
            String token = authHeader.substring(6);
            return validate(requestContext, token);
        }
    }
}
