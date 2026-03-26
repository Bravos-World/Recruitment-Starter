package com.bravos.recruitment.libs.starter.annotation;

import com.bravos.recruitment.libs.starter.configuration.snowflake.SnowflakeIdGenerator;
import org.hibernate.annotations.IdGeneratorType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an entity ID field to be generated using the starter's Snowflake generator.
 * <p>
 * This delegates to {@link SnowflakeIdGenerator} through Hibernate's
 * {@link IdGeneratorType} integration.
 * </p>
 */
@IdGeneratorType(SnowflakeIdGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface SnowflakeGeneratedId {
}
