package ca.coltip.repository;

import ca.coltip.data.entity.Dummy;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyRepository extends JpaRepository<Dummy, Long> {
  @Query(value = "SELECT d FROM Dummy d")
  Dummy getTopBySort(Sort sort);
}
