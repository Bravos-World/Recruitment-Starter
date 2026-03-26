package com.bravos.recruitment.libs.starter.configuration.audit;

import com.bravos.recruitment.libs.starter.context.AuthContext;
import com.bravos.recruitment.libs.starter.context.AuthContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} that provides the current auditor
 * based on the security context.
 * <p>
 * This implementation integrates with {@link AuthContextHolder} to retrieve
 * the authenticated user's ID for JPA auditing purposes.
 * </p>
 *
 * <p>The auditor resolution follows this logic:</p>
 * <ol>
 *   <li>If an authenticated user exists in the context, return their user ID</li>
 *   <li>Otherwise, return the configured default system ID</li>
 * </ol>
 *
 * @see AuditDataProperties
 * @see AuthContextHolder
 */
@Slf4j
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Long> {

  private final AuditDataProperties auditDataProperties;

  /**
   * Returns the current auditor (user ID) for JPA auditing.
   *
   * @return an {@link Optional} containing the user ID, or the default system ID if no user is authenticated
   */
  @Override
  public @NullMarked Optional<Long> getCurrentAuditor() {
    AuthContext authContext = AuthContextHolder.get();
    if (authContext == null) {
      log.debug("No AuthContext found; using default system ID for auditing");
      return Optional.of(auditDataProperties.getDefaultSystemId());
    }
    if (authContext.userId() == null) {
      log.debug("Authenticated user has null userId; using default system ID for auditing");
      return Optional.of(auditDataProperties.getDefaultSystemId());
    }
    return Optional.of(authContext.userId());
  }

}
