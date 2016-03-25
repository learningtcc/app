package org.tiogasolutions.app.standard.jaxrs.filters;

import org.tiogasolutions.app.standard.jaxrs.auth.RequestFilterAuthenticator;

import java.util.HashMap;
import java.util.Map;

public class StandardRequestFilterConfig {

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
}
