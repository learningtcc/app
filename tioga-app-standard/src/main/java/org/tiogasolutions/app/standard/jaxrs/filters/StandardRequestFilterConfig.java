package org.tiogasolutions.app.standard.jaxrs.filters;

import org.springframework.beans.factory.annotation.Required;

public class StandardRequestFilterConfig {

    private String unauthorizedQueryParamName;
    private String unauthorizedQueryParamValue;
    private boolean sessionRequired;
    private String authenticationScheme;
    private String unauthorizedPath;
    private boolean redirectUnauthorized;

    public StandardRequestFilterConfig() {
    }

    @Required
    public String getUnauthorizedQueryParamName() {
        return unauthorizedQueryParamName;
    }
    public void setUnauthorizedQueryParamName(String unauthorizedQueryParamName) {
        this.unauthorizedQueryParamName = unauthorizedQueryParamName;
    }

    @Required
    public String getUnauthorizedQueryParamValue() {
        return unauthorizedQueryParamValue;
    }
    public void setUnauthorizedQueryParamValue(String unauthorizedQueryParamValue) {
        this.unauthorizedQueryParamValue = unauthorizedQueryParamValue;
    }

    @Required
    public boolean isSessionRequired() {
        return sessionRequired;
    }
    public void setSessionRequired(boolean sessionRequired) {
        this.sessionRequired = sessionRequired;
    }

    @Required
    public String getAuthenticationScheme() {
        return authenticationScheme;
    }
    public void setAuthenticationScheme(String authenticationScheme) {
        this.authenticationScheme = authenticationScheme;
    }

    @Required
    public String getUnauthorizedPath() {
        return unauthorizedPath;
    }
    public void setUnauthorizedPath(String unauthorizedPath) {
        this.unauthorizedPath = unauthorizedPath;
    }

    @Required
    public boolean isRedirectUnauthorized() {
        return redirectUnauthorized;
    }
    public void setRedirectUnauthorized(boolean redirectUnauthorized) {
        this.redirectUnauthorized = redirectUnauthorized;
    }
}
