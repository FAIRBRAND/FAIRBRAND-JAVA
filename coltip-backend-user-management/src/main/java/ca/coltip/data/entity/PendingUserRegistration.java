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
@Table(name = "pending_user_registration")
public class PendingUserRegistration {
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  public String firstname;

  @Column
  public String lastname;

  @Column
  public String email;

  @Column
  public String password;

  @ManyToOne
  @JoinColumn(name = "id_language")
  public Language language;

  @Column
  public String otp;

  @Column(name = "expires_at")
  public Instant expiresAt;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  public boolean isExpired() {
    return expiresAt.isBefore(Instant.now());
  }
}
