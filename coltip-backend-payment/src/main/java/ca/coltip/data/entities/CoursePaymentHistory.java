package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name="course_payment_history")
public class CoursePaymentHistory {
    @Id
    @Column(name="id_payment_history")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @ManyToOne
    @JoinColumn(name="id_user")
    private User user;

    @NonNull
    @Column(name="record_status")
    private int recordStatus=1;

    @NonNull
    @Column(name="payment_date")
    private LocalDateTime paymentDate = LocalDateTime.now();

    @NonNull
    @Column(name="amount_paid")
    private double amountPaid = 0.0;

    @ManyToOne
    @JoinColumn(name="id_payment_mode")
    private PaymentMode paymentMode;
}
