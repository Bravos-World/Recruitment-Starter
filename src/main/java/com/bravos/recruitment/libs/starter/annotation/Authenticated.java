package com.bravos.recruitment.libs.starter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Requires the current request to be authenticated.
 * <p>
 * The security aspect checks {@link com.bravos.recruitment.libs.starter.context.AuthContext#authenticated()}
 * and rejects access when the request is unauthenticated.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authenticated {
}
