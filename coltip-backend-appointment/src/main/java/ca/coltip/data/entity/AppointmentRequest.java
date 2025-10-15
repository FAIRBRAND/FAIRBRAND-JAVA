package ca.coltip.data.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@Table
@Entity
@NoArgsConstructor
public class AppointmentRequest {
  @Id
  @Column(name = "id_appointment_request")
  @Setter(AccessLevel.NONE)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "start_at", nullable = false)
  private Instant startAt;

  @Column(name = "end_at", nullable = false)
  private Instant endAt;

  @Column(nullable = false)
  private AppointmentStatus status;

  @ManyToOne
  @JoinColumn(name = "id_user")
  private User user;

  @Column
  private String title;

  @Column
  private String description;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;
}
