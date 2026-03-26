package com.bravos.recruitment.libs.starter.context;

/**
 * Utility holder for the current {@link AuthContext} bound to a request scope.
 * <p>
 * The holder uses {@link ScopedValue} (introduced in Java 21) so authentication
 * data can be propagated safely without relying on {@link ThreadLocal}. The
 * value is only available inside the scope where it was bound.
 * </p>
 *
 * <p>
 * <b>Typical usage:</b>
 * </p>
 *
 * <pre>{@code
 * // Bind the context at the beginning of a request
 * ScopedValue.runWhere(AuthContextHolder.AUTH_CONTEXT, authContext, () -> {
 *   AuthContext ctx = AuthContextHolder.get();
 *   if (ctx == null) {
 *     throw new UnauthorizedException("Not authenticated");
 *   }
 * });
 *
 * // Read the context directly; null means no context is bound
 * AuthContext context = AuthContextHolder.get();
 * }</pre>
 *
 * @see AuthContext
 * @see ScopedValue
 */
public final class AuthContextHolder {

  /**
   * The scoped value holding the current {@link AuthContext}.
   * <p>
   * This should be bound using {@code ScopedValue.runWhere()} or similar methods
   * at the beginning of a request scope.
   * </p>
   */
  public static final ScopedValue<AuthContext> AUTH_CONTEXT = ScopedValue.newInstance();

  private AuthContextHolder() {
  }

  /**
   * Returns the current {@link AuthContext} bound to this scope.
   *
   * @return the current {@link AuthContext}, or {@code null} if none has been
   *         bound to the current scope
   */
  public static AuthContext get() {
    if (!AUTH_CONTEXT.isBound()) {
      return null;
    }
    return AUTH_CONTEXT.get();
  }

}
