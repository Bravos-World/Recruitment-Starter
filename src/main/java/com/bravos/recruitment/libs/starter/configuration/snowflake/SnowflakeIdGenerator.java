package com.bravos.recruitment.libs.starter.configuration.snowflake;

import com.bravos.recruitment.libs.starter.annotation.SnowflakeGeneratedId;
import com.bravos.recruitment.libs.utils.utils.Snowflake;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.GeneratorCreationContext;

import java.lang.reflect.Member;
import java.util.EnumSet;


public class SnowflakeIdGenerator implements BeforeExecutionGenerator {

  private static volatile Snowflake snowflake;


  public static void configure(Snowflake instance) {
    snowflake = instance;
  }


  public SnowflakeIdGenerator(
      SnowflakeGeneratedId annotation,
      Member member,
      GeneratorCreationContext context) {
    // no per-annotation configuration needed
  }

  /**
   * Returns the set of events for which this generator should run.
   * Only {@link EventType#INSERT} is needed since IDs are assigned once.
   */
  @Override
  public EnumSet<EventType> getEventTypes() {
    return EnumSet.of(EventType.INSERT);
  }

  /**
   * Generates the next Snowflake ID.
   *
   * @throws IllegalStateException if {@link #configure(Snowflake)} has not been called
   */
  @Override
  public Object generate(
      SharedSessionContractImplementor session,
      Object owner,
      Object currentValue,
      EventType eventType) {
    if (snowflake == null) {
      throw new IllegalStateException(
          "SnowflakeIdGenerator has not been configured. "
              + "Make sure SnowflakeAutoConfiguration is active or call SnowflakeIdGenerator.configure(snowflake).");
    }
    return snowflake.next();
  }
}

