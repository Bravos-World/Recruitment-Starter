package com.bravos.recruitment.libs.starter.annotation;

import com.bravos.recruitment.libs.starter.configuration.exadvice.GlobalExceptionAdviceConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables the starter's default global exception advice.
 * <p>
 * This imports {@link GlobalExceptionAdviceConfiguration}, which registers
 * {@link com.bravos.recruitment.libs.starter.configuration.exadvice.DefaultGlobalExceptionAdvice}.
 * </p>
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GlobalExceptionAdviceConfiguration.class)
public @interface EnableGlobalExceptionHandler {
}
