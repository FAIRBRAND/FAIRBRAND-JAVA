package ca.coltip.service;

import ca.coltip.data.entity.Language;
import ca.coltip.repository.LanguageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LanguageService {
  private final LanguageRepository repository;

  public Language getByLocale(Locale locale) {
    return repository
      .findByCode(locale.getLanguage())
      .orElseThrow(NullPointerException::new);
  }

  public Language getById(Long id) {
    return repository
      .findById(id)
      .orElseThrow(NullPointerException::new);
  }

  public Optional<Language> findById(Long id) {
    return repository.findById(id);
  }
}
