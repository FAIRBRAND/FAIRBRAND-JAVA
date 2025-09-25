package ca.coltip.service;

import ca.coltip.data.entity.Dummy;
import ca.coltip.repository.DummyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DummyService {
  private final DummyRepository dummyRepository;

  public Dummy getLatest() {
    return dummyRepository.getTopBySort(Sort.by(
      Sort.Direction.DESC,
      "createdAt"
    ));
  }
}
