package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="domain")
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_domain")
    private int id;

    @NonNull
    @Column(name="domain_name", length = 100)
    private String domainName;

    @NonNull
    @Column(name="record_status")
    private int recordStatus;

    @ManyToOne
    @JoinColumn(name="id_language")
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    private Domain origin;

    @OneToMany(mappedBy="domain")
    List<User> listUsers;

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
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(@NonNull String domainName) {
        this.domainName = domainName;
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

    public Domain getOrigin() {
        return origin;
    }

    public void setOrigin(Domain origin) {
        this.origin = origin;
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
