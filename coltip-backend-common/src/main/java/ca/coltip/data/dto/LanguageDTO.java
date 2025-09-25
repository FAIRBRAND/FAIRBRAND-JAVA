package ca.coltip.data.dto;

import ca.coltip.data.entity.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDTO {
  public Long id;
  public String name;
  public String code;

  public LanguageDTO(Language entity) {
    this.id = entity.getId();
    this.name = entity.getName();
    this.code = entity.getCode();
  }

  public Language toLanguage() {
    final var language = new Language();
    language.setId(id);
    language.setName(name);
    language.setCode(code);
    return language;
  }
}