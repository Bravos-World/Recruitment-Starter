package com.bravos.recruitment.libs.starter.annotation;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HasPermission {

  /**
   * The resource name for the permission check.
   * <p>This represents the entity or resource type being accessed (e.g., "user", "order", "document").</p>
   *
   * @return the resource name
   */
  String resource();

  /**
   * The action being performed on the resource.
   * <p>This represents the operation type (e.g., "read", "write", "delete", "update").</p>
   *
   * @return the action name
   */
  String action();

}
