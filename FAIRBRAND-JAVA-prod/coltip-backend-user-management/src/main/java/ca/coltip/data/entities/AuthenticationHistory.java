package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name="authentication_history")
public class AuthenticationHistory {

    @Id
    @Column(name="id_authentication_history")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @ManyToOne
    @JoinColumn(name="id_user")
    private User user;

    @Column(name="date_authentication")
    private LocalDateTime dateAuthentication;

    @Column(name="authentication_status")
    private int authenticationStatus;
}
