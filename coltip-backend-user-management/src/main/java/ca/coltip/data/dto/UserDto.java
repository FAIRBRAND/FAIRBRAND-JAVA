package ca.coltip.data.dto;

import ca.coltip.data.entity.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {
  private Long id;
  private String firstname;
  private String lastname;
  private String phoneNumber;
  private String email;
  private String description;
  private Domain domain;
  private Country country;
  private LanguageDTO language;
  private List<ProfessionalSkill> professionalSkills;
  private List<Degree> degrees;
  private Set<SubGroup> subGroups;

  public UserDto(User user) {
    this.id = user.getId();
    this.firstname = user.getFirstname();
    this.lastname = user.getLastname();
    this.phoneNumber = user.getPhoneNumber();
    this.email = user.getEmail();
    this.description = user.getDescription();
    this.domain = user.getDomain();
    this.country = user.getCountry();
    this.language = new LanguageDTO(user.getLanguage());
    this.professionalSkills = user.getProfessionalSkills();
    this.degrees = user.getDegrees();
    this.subGroups = user.getSubGroups();
  }
}
