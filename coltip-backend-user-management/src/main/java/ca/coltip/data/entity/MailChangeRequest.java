package ca.coltip.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class MailChangeRequest {
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "id_user")
  private User user;

  @Column
  private String token;

  @Column
  private Instant expiration;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;
}
