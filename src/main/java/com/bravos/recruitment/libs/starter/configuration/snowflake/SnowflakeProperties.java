package com.bravos.recruitment.libs.starter.configuration.snowflake;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Snowflake ID generation.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "starter.snowflake")
public class SnowflakeProperties {

    /**
     * The unique machine ID used for generating Snowflake IDs.
     * <p>
     * Must be unique across all instances in a distributed system.
     * Valid range: 0-1023.
     * </p>
     * <p>
     * Default: 1
     * </p>
     */
    private long machineId = 1;

}
