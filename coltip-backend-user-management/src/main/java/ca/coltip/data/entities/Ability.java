package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.List;

@Entity
@Table(name="ability")
public class Ability {

    @Id
    @Column(name="id_ability")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    @Column(name="ability_name",length = 50)
    private String abilityName;

    @NonNull
    @Column(name="record_status")
    private int recordStatus=1;

    @ManyToMany(mappedBy="listAbilities")
    private List<User> listUsers;

    @ManyToMany(mappedBy="listAbilities")
    private List<Admin> listAdmin;

    @ManyToMany(mappedBy="listAbilities")
    private List<SubGroup> listSubgroup;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getAbilityName() {
        return abilityName;
    }

    public void setAbilityName(@NonNull String abilityName) {
        this.abilityName = abilityName;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public List<User> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    public List<Admin> getListAdmin() {
        return listAdmin;
    }

    public void setListAdmin(List<Admin> listAdmin) {
        this.listAdmin = listAdmin;
    }

    public List<SubGroup> getListGroup() {
        return listSubgroup;
    }

    public void setListGroup(List<SubGroup> listGroup) {
        this.listSubgroup = listGroup;
    }
}
