package ca.coltip.data.entities;

import ca.coltip.data.entity.Auditable;
import ca.coltip.data.entity.User;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name="certificate")
public class Certificate extends Auditable {

    @Id
    @Column(name="id_certificate")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    @Column(name="content", length = 1000)
    private String content;

    @ManyToOne
    @JoinColumn(name="id_course")
    private Course course;

    @ManyToOne
    @JoinColumn(name="id_user")
    private User user;

    @NonNull
    @Column(name = "record_status")
    private int recordStatus = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }
}
