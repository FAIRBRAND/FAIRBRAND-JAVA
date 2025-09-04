package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name="user_password_history")
public class UserPasswordHistory {

    @Id
    @Column(name="id_user_password_history")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @ManyToOne
    @JoinColumn(name="id_user")
    private User user;

    @Column(name="password")
    private String password;

    @Column(name="date_added_to_history")
    private LocalDateTime dateAddedToHistory;
}
