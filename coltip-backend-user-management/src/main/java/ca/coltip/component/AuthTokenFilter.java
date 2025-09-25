package ca.coltip.component;

import ca.coltip.service.CustomUserDetailService;
import ca.coltip.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtils;
  private final CustomUserDetailService userDetailsService;
  private final HandlerExceptionResolver exceptionResolver;

  public AuthTokenFilter(
    JwtUtil jwtUtils,
    CustomUserDetailService userDetailsService,
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver exceptionResolver
  ) {
    this.jwtUtils = jwtUtils;
    this.userDetailsService = userDetailsService;
    this.exceptionResolver = exceptionResolver;
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");
    if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }
    return null;
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) {
    try {
      final var jwt = parseJwt(request);

      if (jwt != null && jwtUtils.validateToken(jwt)) {
        final var username = jwtUtils.extractUsername(jwt);
        final var userDetails = userDetailsService.loadUserByUsername(username);
        final var authentication = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      exceptionResolver.resolveException(request, response, null, e);
    }
  }
}