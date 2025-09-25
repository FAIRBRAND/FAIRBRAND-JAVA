package ca.coltip.data.entities;

import ca.coltip.data.entity.Language;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name="course_step")
public class CourseStep {

    @Id
    @Column(name="id_course_step")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    @Column(name="description", length = 30000)
    private String description;

    @NonNull
    @Column(name="file_content", length = 1000)
    private String fileContent;

    @Column(name="step_order")
    private int stepOrder;

    @NonNull
    @Column(name="record_status")
    private int recordStatus=1;

    @ManyToOne
    @JoinColumn(name="id_language")
    private Language language;

    @ManyToOne (fetch = FetchType.LAZY)
    private CourseStep origin;

    @ManyToOne
    @JoinColumn(name="id_course")
    private Course course;

    @Column(name="date_created")
    private LocalDateTime dateCreated;

    @Column(name="date_modified")
    private LocalDateTime dateModified;
}
