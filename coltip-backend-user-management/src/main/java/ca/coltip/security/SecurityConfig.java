package ca.coltip.security;

import ca.coltip.component.AuthEntryPointJwt;
import ca.coltip.component.AuthTokenFilter;
import ca.coltip.component.CustomAccessDeniedHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final AuthEntryPointJwt authEntryPointJwt;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;
  private final AuthTokenFilter authTokenFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(request ->
        request
          .requestMatchers("/health/**").permitAll()
          .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
          .requestMatchers("/auth/**").permitAll()
          .requestMatchers("/password/forget", "/password/reset").permitAll()

          .requestMatchers("/actuator/health", "/actuator/info").permitAll()
          .requestMatchers("/actuator/metrics", "/actuator/prometheus").permitAll()
          .requestMatchers("/actuator/**").hasRole("ADMIN")

          .requestMatchers(HttpMethod.GET, "/appointment_request/**").hasRole("ADMIN")
          .requestMatchers("/appointment_request/validate/**").hasRole("ADMIN")

          .requestMatchers(HttpMethod.POST, "/appointment_request").hasAnyRole("USER")

          .anyRequest().authenticated()
      )
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .exceptionHandling(exception -> exception
        .authenticationEntryPoint(authEntryPointJwt)
        .accessDeniedHandler(customAccessDeniedHandler)
      )
      .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }
}
