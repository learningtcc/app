package org.tiogasolutions.app.standard.jaxrs.auth;

import org.tiogasolutions.dev.common.exceptions.ApiUnauthorizedException;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

public abstract class BasicRequestFilterAuthenticator implements RequestFilterAuthenticator{

    protected abstract SecurityContext validate(ContainerRequestContext requestContext, String username, String password);

    @Override
    public SecurityContext authenticate(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null) {
            throw new NotAuthorizedException("No \"Authorization\" header.");

        } else if (authHeader.startsWith("Basic ") == false) {
            throw new NotAuthorizedException("Not \"Basic\" authentication.");

        } else {
            authHeader = authHeader.substring(6);
        }

        byte[] bytes = DatatypeConverter.parseBase64Binary(authHeader);
        String basicAuth = new String(bytes, StandardCharsets.UTF_8);

        int pos = basicAuth.indexOf(":");

        String username;
        String password;

        if (pos < 0) {
            username = basicAuth;
            password = null;

        } else {
            username = basicAuth.substring(0, pos);
            password = basicAuth.substring(pos+1);
        }

        return validate(requestContext, username, password);
    }
}
