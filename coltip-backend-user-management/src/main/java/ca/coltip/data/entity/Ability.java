package ca.coltip.data.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@Entity
@Table(name = "ability")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Ability {
  public static final String DEFAULT_ADMIN = "ADMIN";
  public static final String DEFAULT_USER = "USER";

  @Id
  @Column(name = "id_ability")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 50)
  private String name;

  @Column(name = "record_status")
  private RecordStatus recordStatus;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;
}
