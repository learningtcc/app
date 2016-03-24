package org.tiogasolutions.app.standard.jaxrs.filters;

import java.util.HashMap;
import java.util.Map;

public class StandardResponseFilterConfig {

    public static final String P3P = "p3p";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String X_UA_COMPATIBLE = "X-UA-Compatible";

    private Map<String, String> extraHeaders = new HashMap<>();

    public StandardResponseFilterConfig() {
        extraHeaders.put(X_UA_COMPATIBLE, "IE=Edge");
        extraHeaders.put(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        extraHeaders.put(P3P, "CP=\"This application does not have a P3P policy.\"");
    }

    public Map<String, String> getExtraHeaders() {
        return extraHeaders;
    }

    public void setExtraHeaders(Map<String, String> extraHeaders) {
        this.extraHeaders = extraHeaders;
    }
}
