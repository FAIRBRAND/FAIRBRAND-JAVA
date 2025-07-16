package ca.coltip.repository;

import ca.coltip.data.entities.SubGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubgroupRepository extends JpaRepository<SubGroup, Integer> {
    Optional<SubGroup> findBySubGroupName(String subgroupName);
}
