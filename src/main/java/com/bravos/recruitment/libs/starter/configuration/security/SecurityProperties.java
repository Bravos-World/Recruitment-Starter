package com.bravos.recruitment.libs.starter.configuration.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
/**
 * Configuration properties for the security starter.
 * <p>
 * These properties can be configured in your {@code application.yml} or
 * {@code application.properties}:
 * </p>
 *
 * <pre>{@code
 * starter.security.internal-secret: "your-secret-key"
 * starter.security.internal-path-prefix: "/internal/"
 * }</pre>
 *
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "starter.security")
public class SecurityProperties {

  /**
   * The secret key used for internal service-to-service authentication.
   * <p>
   * This secret must be passed in the {@code X-Internal-Secret} header for
   * requests to internal endpoints.
   * </p>
   */
  @NotBlank(message = "starter.security.internal-secret is required")
  private String internalSecret;

  /**
   * The URI path prefix that identifies internal endpoints.
   * <p>
   * Requests to paths starting with this prefix will require the internal secret
   * header.
   * Defaults to {@code /internal/}.
   * </p>
   */
  private String internalPathPrefix = "/internal/";

  /**
   * List of URI path patterns to exclude from security processing.
   * <p>
   * Requests matching these patterns will bypass the security filter entirely.
   * Useful for health checks, actuator endpoints, etc.
   * </p>
   */
  private List<String> excludedPaths = new ArrayList<>();

}
