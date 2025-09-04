package ca.coltip.data.entities;


import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.Instant;

@Entity
@Table(name="pending_user_registration")
public class PendingUserRegistration {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @Column(name = "firstname")
    public String firstname;

    @Column(name = "lastname")
    public String lastname;

    @Column(name = "email")
    public String email;

    @Column(name = "password")
    public String password;

    @Column(name = "otp")
    public String otp;

    @Column(name = "expires_at", nullable = false)
    public Instant expiresAt;
}
