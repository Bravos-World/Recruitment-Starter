package com.bravos.recruitment.libs.starter.annotation;

import com.bravos.recruitment.libs.starter.context.AuthContext;
import com.bravos.recruitment.libs.starter.context.AuthContextHolder;
import com.bravos.recruitment.libs.utils.exceptions.ForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jspecify.annotations.NonNull;


@Slf4j
@Aspect
public class SecurityAspect {


  @Around("@annotation(com.bravos.recruitment.libs.starter.annotation.HasPermission)")
  public Object hasPermissionAdvice(@NonNull ProceedingJoinPoint pjp) {
    MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
    HasPermission hasPermission = methodSignature.getMethod().getAnnotation(HasPermission.class);

    AuthContext authContext = AuthContextHolder.get();
    if (authContext == null) {
      log.debug("No AuthContext found in AuthContextHolder");
      throw new ForbiddenException();
    }

    if (!authContext.authenticated()) {
      log.debug("User is not authenticated");
      throw new ForbiddenException();
    }

    String permissionKey = hasPermission.resource() + ":" + hasPermission.action();

    if (!authContext.permissions().contains(permissionKey)) {
      log.debug("Access denied: user lacks permission {} required for method {}", permissionKey, methodSignature.getMethod().getName());
      throw new ForbiddenException();
    }

    return next(pjp);
  }

  /**
   * Around advice that checks for internal access before method execution.
   *
   * @param pjp the proceeding join point
   * @return the result of the method execution
   */
  @Around("@annotation(com.bravos.recruitment.libs.starter.annotation.Internal)")
  public Object isInternal(@NonNull ProceedingJoinPoint pjp) {
    AuthContext authContext = AuthContextHolder.get();
    if (authContext == null || !authContext.internal()) {
      log.debug("Access denied: method is marked as internal but AuthContext is missing or not internal");
      throw new ForbiddenException();
    }
    return next(pjp);
  }

  /**
   * Around advice that checks for authentication before method execution.
   *
   * @param pjp the proceeding join point
   * @return the result of the method execution
   */
  @Around("@annotation(com.bravos.recruitment.libs.starter.annotation.Authenticated)")
  public Object isAuthenticated(@NonNull ProceedingJoinPoint pjp) {
    AuthContext authContext = AuthContextHolder.get();
    if (authContext == null) {
      log.debug("Access denied: method requires authentication but no AuthContext found");
      throw new ForbiddenException();
    }
    if (!authContext.authenticated()) {
      log.debug("Access denied: method requires authentication but user is not authenticated");
      throw new ForbiddenException();
    }
    return next(pjp);
  }

  private Object next(ProceedingJoinPoint pjp) {
    try {
      return pjp.proceed();
    } catch (RuntimeException e) {
      throw e;
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

}
