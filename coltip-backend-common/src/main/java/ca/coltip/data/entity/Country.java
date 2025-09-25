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
@Table(name = "country")
public class Country {
  @Id
  @Column(name = "id_country")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  @ManyToOne
  @JoinColumn(name = "id_language")
  private Language language;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;
}
