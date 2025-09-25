package ca.coltip.data.entity;

import ca.coltip.data.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
  @Id
  @Column(name = "id_user")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String firstname;

  @Column
  private String lastname;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column
  private String email;

  @Column
  private String password;

  @Column
  private String description;

  @Enumerated
  @Column(name = "record_status")
  private RecordStatus recordStatus;

  @ManyToOne
  @JoinColumn(name = "id_domain")
  private Domain domain;

  @ManyToOne
  @JoinColumn(name = "id_country")
  private Country country;

  @ManyToOne
  @JoinColumn(name = "id_language")
  private Language language;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "modified_at")
  private Instant modifiedAt;

  @ManyToMany
  @JoinTable(
    name = "user_professional_skill",
    joinColumns = @JoinColumn(name = "id_user"),
    inverseJoinColumns = @JoinColumn(name = "id_personal_skill")
  )
  private List<ProfessionalSkill> professionalSkills;

  @ManyToMany
  @JoinTable(
    name = "user_degree",
    joinColumns = @JoinColumn(name = "id_user"),
    inverseJoinColumns = @JoinColumn(name = "id_degree")
  )
  private List<Degree> degrees;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "user_sub_group",
    joinColumns = @JoinColumn(name = "id_user"),
    inverseJoinColumns = @JoinColumn(name = "id_sub_group")
  )
  private Set<SubGroup> subGroups;

  public UserDto toDto() {
    return new UserDto(this);
  }

  /**
   * @param description Null: for no description, "": blank for no edit, otherwise update
   */
  public void setDescription(String description) {
    if (description == null) {
      this.description = null;
      return;
    }
    final var value = description.trim();
    if (!value.isBlank()) {
      this.description = description;
    }
  }
}
