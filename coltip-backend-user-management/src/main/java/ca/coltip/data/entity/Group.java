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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups")
public class Group {
  public static final String DEFAULT_ADMINISTRATOR = "Administrator";
  public static final String DEFAULT_USER = "User";

  @Id
  @Column(name = "id_group")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  @Enumerated
  @Column(name = "record_status")
  private RecordStatus recordStatus;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "modified_at")
  private Instant modifiedAt;
}
