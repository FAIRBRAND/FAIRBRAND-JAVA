package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name="course_progress")
public class CourseProgress {

    @Id
    @Column(name="id_course_progress")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @ManyToOne
    @JoinColumn(name="id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name="id_course")
    private Course course;

    @NonNull
    @Column(name="completion_level")
    private double completionLevel;
}
