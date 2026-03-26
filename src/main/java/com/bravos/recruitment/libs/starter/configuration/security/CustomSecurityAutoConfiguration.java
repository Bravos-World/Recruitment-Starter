package com.bravos.recruitment.libs.starter.configuration.security;

import com.bravos.recruitment.libs.starter.annotation.SecurityAspect;
import com.bravos.recruitment.libs.starter.configuration.security.filter.SecurityFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tools.jackson.databind.ObjectMapper;

/**
 * Registers core security components used by the starter on servlet applications.
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class CustomSecurityAutoConfiguration {

  /**
   * Creates the request security filter that populates and validates the auth context.
   */
  @Bean
  public SecurityFilter securityFilter(SecurityProperties securityProperties,
                                       ObjectProvider<ObjectMapper> objectMapperProvider) {
    ObjectMapper objectMapper = objectMapperProvider.getIfAvailable(ObjectMapper::new);
    return new SecurityFilter(securityProperties, objectMapper);
  }

  /**
   * Creates the AOP security aspect if the application has not provided one.
   */
  @Bean
  @ConditionalOnMissingBean(SecurityAspect.class)
  public SecurityAspect securityAspect() {
    return new SecurityAspect();
  }

}