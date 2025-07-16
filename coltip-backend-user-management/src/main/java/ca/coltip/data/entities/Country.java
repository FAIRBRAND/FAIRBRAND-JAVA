package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="country")
public class Country {
    @Id
    @Column(name="id_country")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    @Column(name="country_name", length = 100)
    private String countryName;

    @OneToMany(mappedBy = "country")
    private List<User> listUsers;

    @ManyToOne
    @JoinColumn(name="id_language")
    private Language language;

    @ManyToOne (fetch = FetchType.LAZY)
    private Country origin;

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

    public List<User> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Country getOrigin() {
        return origin;
    }

    public void setOrigin(Country origin) {
        this.origin = origin;
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
