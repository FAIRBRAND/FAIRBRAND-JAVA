package ca.coltip.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sub_group")
public class SubGroup {
  public static final String DEFAULT_ADMIN = "Development";
  public static final String DEFAULT_USER = "Simple";

  @Id
  @Column(name = "id_sub_group")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  @Enumerated
  @Column(name = "record_status")
  private RecordStatus recordStatus;

  @ManyToOne
  @JoinColumn(name = "id_group")
  private Group group;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "sub_group_ability",
    joinColumns = @JoinColumn(name = "sub_group_id"),
    inverseJoinColumns = @JoinColumn(name = "ability_id")
  )
  private List<Ability> abilities;
}
