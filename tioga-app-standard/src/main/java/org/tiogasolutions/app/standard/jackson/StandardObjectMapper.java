package org.tiogasolutions.app.standard.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.dev.jackson.TiogaJacksonInjectable;
import org.tiogasolutions.dev.jackson.TiogaJacksonModule;
import org.tiogasolutions.dev.jackson.TiogaJacksonObjectMapper;

import java.util.*;

public class StandardObjectMapper extends TiogaJacksonObjectMapper {

  @Autowired
  public StandardObjectMapper(Collection<? extends Module> modules,
                              Collection<? extends TiogaJacksonInjectable> injectables) {

    super(merge(new TiogaJacksonModule(), modules), injectables);
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private static Collection<? extends Module> merge(Module first, Collection<? extends Module> others) {
    List<Module> modules = new ArrayList<>();
    modules.add(first);
    modules.addAll(others);
    return modules;
  }

}
