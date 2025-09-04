package ca.coltip.data.entities;


import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name="course_progress")
public class CourseProgress {
    @Id
    @Column(name="id_course")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "completion_level")
    private int completionLevel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getCompletionLevel() {
        return completionLevel;
    }

    public void setCompletionLevel(int completionLevel) {
        this.completionLevel = completionLevel;
    }
}
