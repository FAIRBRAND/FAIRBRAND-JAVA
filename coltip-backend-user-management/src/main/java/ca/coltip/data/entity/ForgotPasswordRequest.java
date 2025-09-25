package ca.coltip.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "forgot_password_request")
public class ForgotPasswordRequest {
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  @Column(name = "verification_code")
  private String verificationCode;

  @Column
  private Instant expiration;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  public boolean isExpired() {
    return expiration.isBefore(Instant.now());
  }
}
