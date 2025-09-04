package ca.coltip.data.repository;

import ca.coltip.data.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findAllByRecordStatus(int recordStatus);
}
