package com.bravos.recruitment.libs.starter.configuration.security.filter;


import com.bravos.recruitment.libs.starter.configuration.security.SecurityProperties;
import com.bravos.recruitment.libs.starter.context.AuthContext;
import com.bravos.recruitment.libs.starter.context.AuthContextHolder;
import com.bravos.recruitment.libs.utils.exceptions.ExceptionConstant;
import com.bravos.recruitment.libs.utils.models.RApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;


@Slf4j
public class SecurityFilter extends OncePerRequestFilter implements Ordered {

  private final SecurityProperties securityProperties;
  private final String forbiddenResponseBody;

  private final Set<String> excludedPaths;

  public SecurityFilter(SecurityProperties securityProperties, ObjectMapper objectMapper) {
    this.securityProperties = securityProperties;
    this.excludedPaths = new HashSet<>(securityProperties.getExcludedPaths());
    this.forbiddenResponseBody = objectMapper.writeValueAsString(RApiResponse.of(ExceptionConstant.FORBIDDEN_CODE, null, "Forbidden"));
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws IOException {
    String uri = request.getRequestURI();

    if (isExcludedPath(uri)) {
      log.debug("Excluding path from security filter: {}", uri);
      next(request, response, AuthContext.UNAUTHENTICATED_CONTEXT, filterChain);
      return;
    }

    if (securityProperties.getInternalPathPrefix() != null &&
        !securityProperties.getInternalPathPrefix().isBlank() &&
        uri.startsWith(securityProperties.getInternalPathPrefix())) {
      String internalSecret = request.getHeader("X-Internal-Secret");
      if (!isCorrectInternalSecret(internalSecret)) {
        log.debug("Invalid internal secret for request to: {}", uri);
        responseForbidden(response);
        return;
      }
      log.debug("Internal access granted for: {}", uri);
      next(request, response, AuthContext.INTERNAL_CONTEXT, filterChain);
      return;
    }

    String authenticated = request.getHeader("X-Authenticated");
    if (authenticated == null || !authenticated.equalsIgnoreCase("true")) {
      log.debug("Unauthenticated access to: {}", uri);
      next(request, response, AuthContext.UNAUTHENTICATED_CONTEXT, filterChain);
      return;
    }

    AuthContext authContext = populateAuthContext(request);
    if (authContext.authenticated()) {
      log.debug("Authenticated user {} accessing: {}", authContext.userId(), uri);
    }
    next(request, response, authContext, filterChain);
  }

  private boolean isExcludedPath(String uri) {
    for (String prefix : excludedPaths) {
      if (uri.startsWith(prefix)) {
        return true;
      }
    }
    return false;
  }

  private boolean isCorrectInternalSecret(String internalSecret) {
    if (internalSecret == null) {
      return false;
    }
    byte[] provided = internalSecret.getBytes(StandardCharsets.UTF_8);
    byte[] expected = securityProperties.getInternalSecret().getBytes(StandardCharsets.UTF_8);
    return MessageDigest.isEqual(provided, expected);
  }

  private AuthContext populateAuthContext(HttpServletRequest request) {
    String userIdHeader = request.getHeader("X-UserId");
    String authoritiesHeader = request.getHeader("X-Authorities");
    return new AuthContext(false, true, parseUserId(userIdHeader), parseAuthorities(authoritiesHeader));
  }

  private long parseUserId(String userIdHeader) {
    if (userIdHeader == null || userIdHeader.isBlank()) {
      throw new IllegalArgumentException("Missing X-UserId header");
    }
    try {
      return Long.parseLong(userIdHeader);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid X-UserId header: " + userIdHeader, e);
    }
  }

  private static final Set<String> EMPTY_AUTHORITIES = Set.of();

  private Set<String> parseAuthorities(String authoritiesHeader) {
    if (authoritiesHeader == null || authoritiesHeader.isBlank()) {
      return EMPTY_AUTHORITIES;
    }
    String[] authorities = authoritiesHeader.split(",");
    return authorities.length == 0 ? EMPTY_AUTHORITIES : Set.of(authorities);
  }

  private void responseForbidden(HttpServletResponse response) throws IOException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json");
    response.getWriter().write(forbiddenResponseBody);
  }

  private void next(HttpServletRequest request,
                    HttpServletResponse response,
                    AuthContext authContext,
                    FilterChain filterChain) {
    ScopedValue.where(AuthContextHolder.AUTH_CONTEXT, authContext).run(() -> {
      try {
        filterChain.doFilter(request, response);
      } catch (IOException | ServletException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 1;
  }

}