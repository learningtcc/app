package org.tiogasolutions.dev.webmvc;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.time.*;
import java.util.*;
import org.apache.commons.logging.*;
import org.tiogasolutions.dev.common.DateUtils;
import org.tiogasolutions.dev.jackson.TiogaJacksonObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.accept.*;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.*;

@Configuration
public class MvcSpringConfig extends WebMvcConfigurationSupport {

  public static final Log log = LogFactory.getLog(MvcSpringConfig.class);

  private static final String MESSAGE_SOURCE = "/WEB-INF/i18n/messages";

  private ContentNegotiationManager contentNegotiationManager;

  public MvcSpringConfig() {
    log.debug("Created " + getClass().getName());
  }

  @Override
  protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(jacksonConverter(tiogaJacksonObjectMapper()));
    converters.add(new StringHttpMessageConverter());
    super.configureMessageConverters(converters);
  }

  @Bean
  public TiogaJacksonObjectMapper tiogaJacksonObjectMapper() {
    return new TiogaJacksonObjectMapper();
  }

  @Override
  public ContentNegotiationManager mvcContentNegotiationManager() {
    if (this.contentNegotiationManager == null) {
      this.contentNegotiationManager = new ContentNegotiationManager(
          new HeaderContentNegotiationStrategy()
      );
    }
    return this.contentNegotiationManager;
  }

  @Bean
  public MappingJackson2HttpMessageConverter jacksonConverter(TiogaJacksonObjectMapper tiogaJacksonObjectMapper) {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(Arrays.asList(
        new MediaType("application", "json"),
        new MediaType("application", "*+json"),
        new MediaType("application", "json", Charset.forName("UTF-8")),
        new MediaType("application", "*+json", Charset.forName("UTF-8"))
    ));
    converter.setObjectMapper(tiogaJacksonObjectMapper);
    return converter;
  }

  @Override
  protected void addFormatters(FormatterRegistry registry) {

    registry.addFormatterForFieldType(LocalDate.class, new org.springframework.format.Formatter<LocalDate>() {
      @Override public LocalDate parse(String text, Locale locale) throws ParseException {
        return DateUtils.toLocalDate(text);
      }
      @Override public String print(LocalDate object, Locale locale) {
        return (object == null) ? null : object.toString();
      }
    });

    registry.addFormatterForFieldType(LocalTime.class, new org.springframework.format.Formatter<LocalTime>() {
      @Override
      public LocalTime parse(String text, Locale locale) throws ParseException {
        return DateUtils.toLocalTime(text);
      }

      @Override
      public String print(LocalTime object, Locale locale) {
        return (object == null) ? null : object.toString();
      }
    });

    registry.addFormatterForFieldType(LocalDateTime.class, new org.springframework.format.Formatter<LocalDateTime>() {
      @Override
      public LocalDateTime parse(String text, Locale locale) throws ParseException {
        return DateUtils.toLocalDateTime(text);
      }

      @Override
      public String print(LocalDateTime object, Locale locale) {
        return (object == null) ? null : object.toString();
      }
    });

    registry.addFormatterForFieldType(ZonedDateTime.class, new org.springframework.format.Formatter<ZonedDateTime>() {
      @Override public ZonedDateTime parse(String text, Locale locale) throws ParseException {
        return DateUtils.toZonedDateTime(text);
      }
      @Override public String print(ZonedDateTime object, Locale locale) {
        return (object == null) ? null : object.toString();
      }
    });
  }

  @Override
  public RequestMappingHandlerMapping requestMappingHandlerMapping() {
    RequestMappingHandlerMapping requestMappingHandlerMapping = super.requestMappingHandlerMapping();
    requestMappingHandlerMapping.setUseSuffixPatternMatch(false);
    requestMappingHandlerMapping.setUseTrailingSlashMatch(false);
    return requestMappingHandlerMapping;
  }

  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }

  @Bean(name = "messageSource")
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename(MESSAGE_SOURCE);
    // TODO - Refactor "0" only for development.
    messageSource.setCacheSeconds(0);
    return messageSource;
  }

  @Override
  public Validator getValidator() {
      LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
      validator.setValidationMessageSource(messageSource());
      return validator;
  }

  @Bean
  public TemplateResolver templateResolver() {

    TemplateResolver templateResolver = new ServletContextTemplateResolver();
    templateResolver.setPrefix("");
    templateResolver.setSuffix("");
    templateResolver.setTemplateMode("HTML5");
    templateResolver.setCacheable(false);

    return templateResolver;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
      SpringTemplateEngine templateEngine = new SpringTemplateEngine();
      templateEngine.setTemplateResolver(templateResolver());
      templateEngine.addDialect(new SpringSecurityDialect());
      return templateEngine;
  }

  private class CustomTemplateResolver extends TemplateResolver {

  }
}
