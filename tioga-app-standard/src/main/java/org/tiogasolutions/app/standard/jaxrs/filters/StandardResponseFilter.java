package org.tiogasolutions.app.standard.jaxrs.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.app.standard.execution.ExecutionManager;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class StandardResponseFilter implements ContainerResponseFilter {

    private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";

    private final ExecutionManager executionManager;
    private final StandardResponseFilterConfig filterConfig;

    @Autowired
    public StandardResponseFilter(ExecutionManager executionManager, StandardResponseFilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.executionManager = executionManager;
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        for (Map.Entry<String, String> entry : filterConfig.getExtraHeaders().entrySet()) {
            responseContext.getHeaders().add(entry.getKey(), entry.getValue());
        }

        if (filterConfig.isEchoingAccessControlRequestHeaders()) {
            List<String> accessHeaders = requestContext.getHeaders().get(ACCESS_CONTROL_REQUEST_HEADERS);
            if (accessHeaders != null) {
                responseContext.getHeaders().addAll(ACCESS_CONTROL_ALLOW_HEADERS, accessHeaders.toArray());
            }
        }

        // Clear everything when we are all done.
        executionManager.clearContext();
    }
}
