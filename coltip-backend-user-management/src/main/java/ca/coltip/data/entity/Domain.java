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
@Table(name = "domain")
public class Domain {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_domain")
  private Long id;

  @Column(length = 100)
  private String name;

  @Enumerated
  @Column(name = "record_status")
  private RecordStatus recordStatus;

  @ManyToOne
  @JoinColumn(name = "id_language")
  private Language language;

  @Column(name = "created_at")
  @CreationTimestamp
  private Instant createdAt;
}
