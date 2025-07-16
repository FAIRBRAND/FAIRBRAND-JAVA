package ca.coltip.repository;

import ca.coltip.data.entities.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseTypeRepository extends JpaRepository<CourseType, Integer> {
    List<CourseType> findAllByRecordStatus(int recordStatus);
    Optional<CourseType> findByIdAndRecordStatus(int id, int recordStatus);
}
