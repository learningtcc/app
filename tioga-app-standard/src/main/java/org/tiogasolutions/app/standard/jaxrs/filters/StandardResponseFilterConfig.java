package org.tiogasolutions.app.standard.jaxrs.filters;

import java.util.HashMap;
import java.util.Map;

public class StandardResponseFilterConfig {

  private Map<String,String> extraHeaders = new HashMap<>();

  public StandardResponseFilterConfig() {
    extraHeaders.put("Access-Control-Allow-Origin", "*");
    extraHeaders.put("X-UA-Compatible", "CP=\"This application does not have a P3P policy.\"");
    extraHeaders.put("p3p", "IE=Edge");
  }

  public Map<String, String> getExtraHeaders() {
    return extraHeaders;
  }

  public void setExtraHeaders(Map<String, String> extraHeaders) {
    this.extraHeaders = extraHeaders;
  }
}
