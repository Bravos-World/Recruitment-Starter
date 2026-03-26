package com.bravos.recruitment.libs.starter.annotation;


import com.bravos.recruitment.libs.starter.configuration.security.CustomSecurityAutoConfiguration;
import com.bravos.recruitment.libs.starter.configuration.security.DefaultSecurityFilterChain;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.lang.annotation.*;

/**
 * Enables the starter's security integration for a Spring application.
 * <p>
 * Importing this annotation registers the security filter, security aspect,
 * and a default stateless {@code SecurityFilterChain} when one is missing.
 * </p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableWebSecurity
@Import({
    CustomSecurityAutoConfiguration.class,
    DefaultSecurityFilterChain.class
})
public @interface EnableSecurity {
}
