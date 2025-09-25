package ca.coltip.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@Table
@Entity
public class Slot {
  @Id
  @Column(name = "id_slot")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String googleCalendarId;

  @Column(name = "start_at")
  private Instant startAt;

  @Column(name = "end_at")
  private Instant endAt;

  @Column(nullable = false)
  private String timezone;

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
