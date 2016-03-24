package org.tiogasolutions.app.standard.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import org.tiogasolutions.dev.jackson.TiogaJacksonInjectable;
import org.tiogasolutions.dev.jackson.TiogaJacksonModule;
import org.tiogasolutions.dev.jackson.TiogaJacksonObjectMapper;

import java.util.*;

public class StandardObjectMapper extends TiogaJacksonObjectMapper {

    public StandardObjectMapper() {
        this(Collections.singletonList(new TiogaJacksonModule()), Collections.emptyList());
    }

    public StandardObjectMapper(Collection<? extends Module> modules,
                                Collection<? extends TiogaJacksonInjectable> injectables) {

        super(modules, injectables);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
