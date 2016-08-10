package org.tiogasolutions.app.standard.jaxrs.filters;

import java.util.HashMap;
import java.util.Map;

public class StandardResponseFilterConfig {

    private Map<String, String> extraHeaders = new HashMap<>();
    private boolean echoingAccessControlRequestHeaders = false;

    public StandardResponseFilterConfig() {
    }

    public boolean isEchoingAccessControlRequestHeaders() {
        return echoingAccessControlRequestHeaders;
    }

    public void setEchoingAccessControlRequestHeaders(boolean echoingAccessControlRequestHeaders) {
        this.echoingAccessControlRequestHeaders = echoingAccessControlRequestHeaders;
    }

    public Map<String, String> getExtraHeaders() {
        return extraHeaders;
    }

    public void setExtraHeaders(Map<String, String> extraHeaders) {
        this.extraHeaders = extraHeaders;
    }
}
