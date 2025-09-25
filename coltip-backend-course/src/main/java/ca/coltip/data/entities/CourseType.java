package ca.coltip.data.entities;

import ca.coltip.data.entity.Auditable;
import ca.coltip.data.entity.Language;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name="course_type")
public class CourseType extends Auditable {
    @Id
    @Column(name="id_course_type")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    @Column(name="course_type_name",length = 100)
    private String courseTypeName;

    @NonNull
    @Column(name="record_status")
    private int recordStatus=1;

    @ManyToOne
    @JoinColumn(name="id_language")
    private Language language;

    @ManyToOne (fetch = FetchType.LAZY)
    private CourseType origin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(@NonNull String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public CourseType getOrigin() {
        return origin;
    }

    public void setOrigin(CourseType origin) {
        this.origin = origin;
    }
}
