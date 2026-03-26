package com.bravos.recruitment.libs.starter.configuration.objectmapper;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigInteger;

/**
 * Auto-configuration for a shared Jackson {@link ObjectMapper}.
 * <p>
 * Registers a pre-configured mapper and an optional module that serializes
 * {@code Long}/{@code BigInteger} values as strings for JavaScript-safe payloads.
 * </p>
 */
@AutoConfiguration
@ConditionalOnProperty(name = "starter.object-mapper.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ObjectMapperProperties.class)
public class ObjectMapperAutoConfiguration {

  /**
   * Creates a pre-configured {@link ObjectMapper} bean.
   *
   * <p>
   * The mapper has {@code FAIL_ON_UNKNOWN_PROPERTIES} and
   * {@code FAIL_ON_NULL_FOR_PRIMITIVES} disabled, and all classpath Jackson modules
   * auto-registered. When {@code starter.object-mapper.serialize-long-as-string} is
   * {@code true} (the default), {@code Long} and {@code BigInteger} values are
   * serialized as JSON strings.
   * </p>
   *
   * @param properties the ObjectMapper configuration properties
   * @return a fully configured {@link ObjectMapper} instance
   */
  @Bean
  @ConditionalOnMissingBean(ObjectMapper.class)
  public ObjectMapper objectMapper(ObjectMapperProperties properties) {
    JsonMapper.Builder builder = JsonMapper.builder()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
        .findAndAddModules();

    if (properties.isSerializeLongAsString()) {
      builder.addModule(longToStringModule());
    }

    return builder.build();
  }

  /**
   * Creates a Jackson {@link SimpleModule} that serializes {@code Long}, {@code long},
   * and {@code BigInteger} values as JSON strings.
   *
   * <p>
   * This module is registered into the {@link ObjectMapper} when
   * {@code starter.object-mapper.serialize-long-as-string} is {@code true}.
   * It is also exposed as a standalone bean so other components can reuse it.
   * </p>
   *
   * @return the {@code LongToStringModule}
   */
  @Bean
  @ConditionalOnMissingBean(name = "longToStringModule")
  public SimpleModule longToStringModule() {
    SimpleModule module = new SimpleModule("LongToStringModule");
    module.addSerializer(Long.class, ToStringSerializer.instance);
    module.addSerializer(Long.TYPE, ToStringSerializer.instance);
    module.addSerializer(BigInteger.class, ToStringSerializer.instance);
    return module;
  }

}
