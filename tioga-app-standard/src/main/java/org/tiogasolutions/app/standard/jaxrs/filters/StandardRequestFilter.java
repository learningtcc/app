package org.tiogasolutions.app.standard.jaxrs.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.app.standard.execution.ExecutionManager;
import org.tiogasolutions.app.standard.jaxrs.auth.AnonymousRequestFilterAuthenticator;
import org.tiogasolutions.app.standard.jaxrs.auth.RequestFilterAuthenticator;
import org.tiogasolutions.app.standard.jaxrs.auth.StandardAuthenticationResponseFactory;
import org.tiogasolutions.dev.common.exceptions.ApiException;
import org.tiogasolutions.dev.common.exceptions.ApiForbiddenException;
import org.tiogasolutions.dev.common.exceptions.ApiUnauthorizedException;

import javax.annotation.Priority;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Providers;
import java.io.IOException;
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

        RequestFilterAuthenticator authenticator = getRequestFilterAuthenticator();
        String authenticationScheme = authenticator.getAuthenticationScheme();

        try {
            SecurityContext securityContext = authenticator.authenticate(requestContext);
            requestContext.setSecurityContext(securityContext);

            T domain = domainResolver.getDomain(requestContext);
            String domainName = domainResolver.getDomainName(requestContext);

            executionManager.newContext(domainName, domain, uriInfo, httpHeaders, request, securityContext, providers);

        } catch (ForbiddenException | ApiForbiddenException e) {
            Response response = authenticationResponseFactory.createForbiddenResponse(requestContext);
            requestContext.abortWith(response);

        } catch (NotAuthorizedException | ApiUnauthorizedException  e) {
            Response response = authenticationResponseFactory.createUnauthorizedResponse(requestContext, authenticationScheme);
            requestContext.abortWith(response);

        } catch (ApiException e) {
            Response response = Response.status(e.getStatusCode()).build();
            requestContext.abortWith(response);
        }
    }

    private RequestFilterAuthenticator getRequestFilterAuthenticator() {

        String path = uriInfo.getPath();
        Set<String> uris = filterConfig.getSecuredUris().keySet();

        for (String uri : uris) {
            Pattern pattern = Pattern.compile(uri);
            Matcher matcher = pattern.matcher(path);
            if (matcher.matches()) {
                return filterConfig.getSecuredUris().get(uri);
            }
        }
        return AnonymousRequestFilterAuthenticator.SINGLETON;
    }
}
