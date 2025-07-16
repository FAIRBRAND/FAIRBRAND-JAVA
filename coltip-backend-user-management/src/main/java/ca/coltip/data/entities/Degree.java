package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="degree")
public class Degree {
    @Id
    @Column(name="id_degree")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    @Column(name="degree_name", length = 100)
    private String degreeName;

    @NonNull
    @Column(name="record_status")
    private int recordStatus=1;

    @ManyToOne
    @JoinColumn(name="id_language")
    private Language language;

    @ManyToOne (fetch = FetchType.LAZY)
    private Degree origin;

    @ManyToOne
    @JoinColumn(name="id_university")
    private University university;

    @ManyToMany(mappedBy="listDegrees")
    private List<User> listUsers;

    @Column(name="date_created")
    private LocalDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name="id_admin", insertable = false, updatable = false)
    private Admin createdBy;

    @Column(name="date_modified")
    private LocalDateTime dateModified;

    @ManyToOne
    @JoinColumn(name="id_admin", insertable = false, updatable = false)
    private Admin modifiedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(@NonNull String degreeName) {
        this.degreeName = degreeName;
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

    public Degree getOrigin() {
        return origin;
    }

    public void setOrigin(Degree origin) {
        this.origin = origin;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public List<User> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Admin getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Admin createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }

    public Admin getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Admin modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
