package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    @Column(name = "surname", length = 100)
    private String surname;

    @NonNull
    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column
    private String email;

    @NonNull
    @Column(length = 64)
    private String password;

    @Column(length = 30000)
    private String description;

    @Column(name = "cv_content", length = 1000)
    private String cvContent;

    @NonNull
    @Column(name = "record_status")
    private int recordStatus = 1;

    @ManyToOne
    @JoinColumn(name = "id_domain")
    private Domain domain;

    @ManyToOne
    @JoinColumn(name = "id_country")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "id_language")
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    private User originUser;

    @ManyToMany
    @JoinTable(name = "user_professional_skill", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_personal_skill"))
    private List<ProfessionalSkill> listProfessionalSkills;

    @ManyToMany
    @JoinTable(name = "user_degree", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_degree"))
    private List<Degree> listDegrees;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_ability", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_ability"))
    private Set<Ability> listAbilities;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_sub_group", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_sub_group"))
    private Set<SubGroup> listSubgroups;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name = "id_admin", insertable = false, updatable = false)
    private Admin createdBy;

    @Column(name = "date_modified")
    private LocalDateTime dateModified;

    @ManyToOne
    @JoinColumn(name = "id_admin", insertable = false, updatable = false)
    private Admin modifiedBy;
    //
    // @ManyToMany
    // @JoinTable(name="user_sub_group",
    // joinColumns = @JoinColumn(name="id_user"),
    // inverseJoinColumns = @JoinColumn(name="id_sub_group"))
    // private List<SubGroup> listSubGroups;

    public String getName() {
        return firstName + " " + surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getSurname() {
        return surname;
    }

    public void setSurname(@NonNull String surname) {
        this.surname = surname;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCvContent() {
        return cvContent;
    }

    public void setCvContent(String cvContent) {
        this.cvContent = cvContent;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public User getOriginUser() {
        return originUser;
    }

    public void setOriginUser(User originUser) {
        this.originUser = originUser;
    }

    public List<ProfessionalSkill> getListPersonalSkills() {
        return listProfessionalSkills;
    }

    public void setListPersonalSkills(List<ProfessionalSkill> listProfessionalSkills) {
        this.listProfessionalSkills = listProfessionalSkills;
    }

    public List<Degree> getListDegrees() {
        return listDegrees;
    }

    public void setListDegrees(List<Degree> listDegrees) {
        this.listDegrees = listDegrees;
    }

    public Set<Ability> getListAbilities() {
        return listAbilities;
    }

    public void setListAbilities(Set<Ability> listAbilities) {
        this.listAbilities = listAbilities;
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

    public Set<SubGroup> getListSubgroups() {
        return listSubgroups;
    }

    public void setListSubgroups(Set<SubGroup> listSubgroups) {
        this.listSubgroups = listSubgroups;
    }
}
