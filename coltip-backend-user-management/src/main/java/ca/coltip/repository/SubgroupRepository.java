package ca.coltip.repository;

import ca.coltip.data.entity.SubGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubgroupRepository extends JpaRepository<SubGroup, Long> {
  <S extends SubGroup> Optional<S> findFirstByName(String name);
}
