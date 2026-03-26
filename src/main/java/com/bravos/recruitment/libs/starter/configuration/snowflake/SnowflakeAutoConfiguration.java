package com.bravos.recruitment.libs.starter.configuration.snowflake;

import com.bravos.recruitment.libs.utils.utils.Snowflake;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration for the shared {@link Snowflake} generator.
 */
@AutoConfiguration
@EnableConfigurationProperties(SnowflakeProperties.class)
public class SnowflakeAutoConfiguration {

  /**
   * Creates a {@link Snowflake} bean configured with the specified machine ID.
   *
   * @param properties the Snowflake configuration properties
   * @return a new Snowflake instance
   */
  @Bean
  @ConditionalOnMissingBean(Snowflake.class)
  public Snowflake snowflake(SnowflakeProperties properties) {
    Snowflake instance = new Snowflake(properties.getMachineId());
    SnowflakeIdGenerator.configure(instance);
    return instance;
  }

}
