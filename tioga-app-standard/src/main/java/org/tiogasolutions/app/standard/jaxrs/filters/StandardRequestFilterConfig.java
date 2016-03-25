package org.tiogasolutions.app.standard.jaxrs.filters;

import org.tiogasolutions.app.standard.jaxrs.auth.RequestFilterAuthenticator;

import java.util.HashMap;
import java.util.Map;

public class StandardRequestFilterConfig {

    private String unauthorizedQueryParamName;
    private String unauthorizedQueryParamValue;
    private String unauthorizedPath;
    private boolean redirectUnauthorized;

    private Map<String,RequestFilterAuthenticator> securedUris = new HashMap<>();

    public StandardRequestFilterConfig() {
    }

    public Map<String, RequestFilterAuthenticator> getSecuredUris() {
        return securedUris;
    }
    public void setSecuredUris(Map<String, RequestFilterAuthenticator> securedUris) {
        this.securedUris = securedUris;
    }
    public void registerAuthenticator(RequestFilterAuthenticator authenticator, String...uriRegExs) {
        for (String uriRegEx : uriRegExs) {
            securedUris.put(uriRegEx, authenticator);
        }
    }

    public String getUnauthorizedQueryParamName() {
        return unauthorizedQueryParamName;
    }
    public void setUnauthorizedQueryParamName(String unauthorizedQueryParamName) {
        this.unauthorizedQueryParamName = unauthorizedQueryParamName;
    }

    public String getUnauthorizedQueryParamValue() {
        return unauthorizedQueryParamValue;
    }
    public void setUnauthorizedQueryParamValue(String unauthorizedQueryParamValue) {
        this.unauthorizedQueryParamValue = unauthorizedQueryParamValue;
    }

    public String getUnauthorizedPath() {
        return unauthorizedPath;
    }
    public void setUnauthorizedPath(String unauthorizedPath) {
        this.unauthorizedPath = unauthorizedPath;
    }

    public boolean isRedirectUnauthorized() {
        return redirectUnauthorized;
    }
    public void setRedirectUnauthorized(boolean redirectUnauthorized) {
        this.redirectUnauthorized = redirectUnauthorized;
    }

}
