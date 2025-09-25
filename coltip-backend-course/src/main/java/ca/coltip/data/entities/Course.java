package ca.coltip.data.entities;

import ca.coltip.data.entity.Auditable;
import ca.coltip.data.entity.Language;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name="course")
public class Course extends Auditable {

    @Id
    @Column(name="id_course")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    @Column(name="course_title", length = 100)
    private String courseTitle;

    @NonNull
    @Column(name="description", length = 30000)
    private String description;

    @Column(name="price")
    private double price;

    @Column(length = 1000)
    private String photo;

    @ManyToOne
    @JoinColumn(name="id_course_type")
    private CourseType courseType;

    @NonNull
    @Column(name="record_status")
    private int recordStatus=1;

    @ManyToOne
    @JoinColumn(name="id_language")
    private Language language;

    @ManyToOne (fetch = FetchType.LAZY)
    private Course origin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(@NonNull String courseTitle) {
        this.courseTitle = courseTitle;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
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

    public Course getOrigin() {
        return origin;
    }

    public void setOrigin(Course origin) {
        this.origin = origin;
    }
}
