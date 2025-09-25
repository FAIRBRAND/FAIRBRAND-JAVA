package ca.coltip.util;

import ca.coltip.data.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
  private final long expirationMs;
  private final SecretKey secretKey;

  public JwtUtil(Environment env) {
    this.expirationMs = env.getRequiredProperty("jwt.expiration", Long.class);
    var secret = env.getRequiredProperty("jwt.secret", String.class);
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(User user) throws InvalidKeyException {
    return Jwts.builder()
      .id(user.getId().toString())
      .subject(user.getEmail())
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + expirationMs))
      .signWith(secretKey)
      .compact();
  }

  public String extractUsername(String token) throws JwtException {
    return extractAllClaims(token).getSubject();
  }

  public Long extractId(String token) throws JwtException {
    return Long.parseLong(extractAllClaims(token).getId());
  }

  public Date extractExpiration(String token) throws JwtException {
    return extractAllClaims(token).getExpiration();
  }

  private Claims extractAllClaims(String token) throws JwtException {
    return Jwts.parser()
      .verifyWith(secretKey)
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  public boolean validateToken(String token) {
    return !isTokenExpired(token);
  }

  public boolean isTokenExpired(String token) {
    Date expirationDate = extractExpiration(token);
    return expirationDate.before(new Date());
  }
}