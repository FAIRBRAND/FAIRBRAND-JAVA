package ca.coltip.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "degree")
public class Degree {
  @Id
  @Column(name = "id_degree")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  @Enumerated
  @Column(name = "record_status")
  private RecordStatus recordStatus;

  @ManyToOne
  @JoinColumn(name = "id_language")
  private Language language;

  @ManyToOne
  @JoinColumn(name = "id_university")
  private University university;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "modified_at")
  private Instant modifiedAt;
}
