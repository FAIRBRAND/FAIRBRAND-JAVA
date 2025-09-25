package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name="course_enrollment")
public class CourseEnrollment {
    @Id
    @Column(name="id_course_enrollment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @ManyToOne
    @JoinColumn(name="id_user")
    private User user;

    @NonNull
    @Column(name="record_status")
    private int recordStatus=1;

    @ManyToOne
    @JoinColumn(name="id_course")
    private Course course;

    @NonNull
    @Column(name="total_paid")
    private double totalPaid = 0.0;

    @ManyToOne
    @JoinColumn(name="id_payment_mode")
    private PaymentMode paymentMode;
}
