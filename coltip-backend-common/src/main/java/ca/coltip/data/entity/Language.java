package ca.coltip.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Locale;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Language {
  @Id
  @Column(name = "id_language")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  @Column
  private String code;

  public Locale toLocale() {
    return Locale.of(code);
  }
}
