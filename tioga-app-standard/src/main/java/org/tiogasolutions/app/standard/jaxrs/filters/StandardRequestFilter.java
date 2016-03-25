package org.tiogasolutions.app.standard.jaxrs.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.app.standard.execution.ExecutionManager;
import org.tiogasolutions.app.standard.jaxrs.auth.StandardAuthenticationResponseFactory;
import org.tiogasolutions.app.standard.jaxrs.auth.RequestFilterAuthenticator;
import org.tiogasolutions.dev.common.exceptions.ApiException;
import org.tiogasolutions.dev.common.exceptions.ApiUnauthorizedException;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Providers;
import java.io.IOException;
import java.security.Principal;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class StandardRequestFilter<T> implements ContainerRequestFilter {

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private Request request;

    @Context
    private SecurityContext securityContext;

    @Context
    private Providers providers;

    private final ExecutionManager<T> executionManager;
    private final StandardRequestFilterConfig filterConfig;
    private final StandardRequestFilterDomainResolver<T> domainResolver;
    private final StandardAuthenticationResponseFactory authenticationResponseFactory;

    @Autowired
    public StandardRequestFilter(StandardRequestFilterConfig filterConfig, ExecutionManager<T> executionManager, StandardRequestFilterDomainResolver<T> domainResolver, StandardAuthenticationResponseFactory authenticationResponseFactory) {
        this.filterConfig = filterConfig;
        this.domainResolver = domainResolver;
        this.executionManager = executionManager;
        this.authenticationResponseFactory = authenticationResponseFactory;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        try {
            // By default it will be the same as the current.
            SecurityContext newSecurityContext = authenticate(requestContext, securityContext);

            T domain = domainResolver.getDomain(requestContext);
            String domainName = domainResolver.getDomainName(requestContext);

            executionManager.newContext(domainName, domain, uriInfo, httpHeaders, request, newSecurityContext, providers);

        } catch (NotAuthorizedException | ApiUnauthorizedException  e) {

            Response response = authenticationResponseFactory.createForbiddenResponse(requestContext);
            requestContext.abortWith(response);

        } catch (ApiException e) {
            Response response = Response.status(e.getStatusCode()).build();
            requestContext.abortWith(response);
        }
    }

    private SecurityContext authenticate(ContainerRequestContext requestContext, SecurityContext securityContext) {

        String path = uriInfo.getPath();
        Set<String> uris = filterConfig.getSecuredUris().keySet();

        for (String uri : uris){
            Pattern pattern = Pattern.compile(uri);
            Matcher matcher = pattern.matcher(path);
            if (matcher.matches()) {
                RequestFilterAuthenticator authenticator = filterConfig.getSecuredUris().get(uri);
                securityContext = authenticator.authenticate(requestContext);
                requestContext.setSecurityContext(securityContext);
                break;
            }
        }

        return securityContext;
    }

    private static class StandardSecurityContext implements SecurityContext {
        private final boolean secure;
        private final Principal principal;
        private final String authenticationScheme;

        public StandardSecurityContext(SecurityContext oldSecurityContext, Principal principal, String authenticationScheme) {
            this.principal = principal;
            this.secure = oldSecurityContext.isSecure();
            this.authenticationScheme = authenticationScheme;
        }

        @Override
        public boolean isUserInRole(String role) {
            return false;
        }

        @Override
        public boolean isSecure() {
            return secure;
        }

        @Override
        public String getAuthenticationScheme() {
            return authenticationScheme;
        }

        @Override
        public Principal getUserPrincipal() {
            return principal;
        }
    }
}
