package ca.coltip.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {
  @Id
  @Column(name = "id_refresh_token")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional = false)
  @JoinColumn(name = "id_user")
  private User user;

  @Column(name = "expires_at")
  private Instant expiresAt;

  @Column(name = "token")
  private String token;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "modified_at")
  private Instant modifiedAt;

  public boolean isExpired() {
    return expiresAt.isBefore(Instant.now());
  }
}
