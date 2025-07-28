package ca.coltip.data.repository;

import ca.coltip.data.entities.CourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseProgressRepository extends JpaRepository<CourseProgress, Integer> {
    @Query("SELECT cp FROM CourseProgress cp WHERE cp.user.id = :userId AND cp.course.id = :courseId")
    Optional<CourseProgress> findProgressByUserIdAndCourseId(@Param("userId") int userId, @Param("courseId") int courseId);
}
