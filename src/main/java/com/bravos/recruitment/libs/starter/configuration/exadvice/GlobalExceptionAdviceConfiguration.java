package com.bravos.recruitment.libs.starter.configuration.exadvice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalExceptionAdviceConfiguration {

  /**
   * Creates and registers the {@link DefaultGlobalExceptionAdvice} bean.
   *
   * @return a new instance of DefaultGlobalExceptionAdvice
   */
  @Bean
  public DefaultGlobalExceptionAdvice globalExceptionAdvice() {
    return new DefaultGlobalExceptionAdvice();
  }

}
