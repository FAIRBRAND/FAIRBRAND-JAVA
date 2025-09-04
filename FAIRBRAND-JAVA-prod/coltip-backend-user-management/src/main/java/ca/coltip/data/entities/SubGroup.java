package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.springframework.lang.NonNull;

import java.util.List;

@Entity
@Table(name="sub_group")
public class SubGroup {
    @Id
    @Column(name="id_sub_group")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    @Column(name="sub_group_name", length = 100)
    private String subGroupName;

    @NonNull
    @Column(name="record_status")
    private int recordStatus=1;

    @ManyToOne
    @JoinColumn(name="id_group")
    private Group group;

    @ManyToMany(mappedBy="listSubgroups")
    private List<User> listUsers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="subgroup_ability",
            joinColumns = @JoinColumn(name="subgroup_id"),
            inverseJoinColumns = @JoinColumn(name="ability_id"))
    private List<Ability> listAbilities;

    public List<Ability> getListAbilities() {
        return listAbilities;
    }

    public void setListAbilities(List<Ability> listAbilities) {
        this.listAbilities = listAbilities;
    }
}
