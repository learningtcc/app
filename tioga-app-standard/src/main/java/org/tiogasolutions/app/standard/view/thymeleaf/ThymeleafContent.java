package org.tiogasolutions.app.standard.view.thymeleaf;

import java.util.HashMap;
import java.util.Map;

public class ThymeleafContent {

  private final String view;
  private final Map<String, Object>  variables = new HashMap<>();

  public ThymeleafContent(String view, Object model) {
    this.view = view;
    this.variables.put("it", model);
  }

  public String getView() {
    return view;
  }

  public Map<String, ?> getVariables() {
    return variables;
  }
}
