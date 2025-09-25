package ca.coltip.repository;

import ca.coltip.data.entity.ProfessionalSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionalSkillRepository extends JpaRepository<ProfessionalSkill, Long> {
}
