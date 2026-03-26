package com.bravos.recruitment.libs.starter.context;


import java.util.Set;
/**
 * Immutable authentication context representing the security state of a request.
 * <p>
 * This context is populated during the authentication process and provides
 * information about whether the request is internal, whether it is authenticated,
 * the user ID (if authenticated), and the set of permissions associated with the
 * user.
 * </p>
 *
 * @param internal      Indicates if the request is from an internal service (true) or an external client (false).
 * @param authenticated Indicates if the request has been successfully authenticated.
 * @param userId        The ID of the authenticated user, or null if not authenticated.
 * @param permissions   A set of permission strings representing the user's permissions, or an empty set if not authenticated.
 */
public record AuthContext(
    boolean internal,
    boolean authenticated,
    Long userId,
    Set<String> permissions) {

  /**
   * Authentication context for trusted service-to-service requests.
   * <p>
   * This context is used when a request is validated via the internal secret
   * header and should bypass user authentication checks.
   * </p>
   */
  public static final AuthContext INTERNAL_CONTEXT = new AuthContext(
      true,
      false,
      null,
      Set.of());

  /**
   * Authentication context for anonymous or failed authentication requests.
   * <p>
   * This context is used when no authentication headers are present or when the
   * authentication flow does not establish a user identity.
   * </p>
   */
  public static final AuthContext UNAUTHENTICATED_CONTEXT = new AuthContext(
      false,
      false,
      null,
      Set.of());

}
