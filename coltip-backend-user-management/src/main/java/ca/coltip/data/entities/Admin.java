package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.List;

@Entity
@Table(name="admin")
public class Admin {
    @Id
    @Column(name="id_admin")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @Column(length = 100)
    private String username;

    @Column(length = 64)
    private String password;

    @ManyToMany
    @JoinTable(name="admin_ability",
            joinColumns = @JoinColumn(name="admin_id"),
            inverseJoinColumns = @JoinColumn(name="ability_id"))
    private List<Ability> listAbilities;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Ability> getListAbilities() {
        return listAbilities;
    }

    public void setListAbilities(List<Ability> listAbilities) {
        this.listAbilities = listAbilities;
    }
}
