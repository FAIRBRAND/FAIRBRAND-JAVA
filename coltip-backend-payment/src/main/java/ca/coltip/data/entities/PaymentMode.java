package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name="payment_mode")
public class PaymentMode {
    @Id
    @Column(name="id_payment_mode")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    @Column(name="mode_name")
    private String mode_name;

    @NonNull
    @Column(name="mode_code", length = 50)
    private String modeCode;

    @ManyToOne
    @JoinColumn(name="id_payment_category")
    private PaymentCategory paymentCategory;

    @NonNull
    @Column(name="record_status")
    private int recordStatus=1;

    @ManyToOne
    @JoinColumn(name="id_language")
    private Language language;

    @ManyToOne (fetch = FetchType.LAZY)
    private PaymentMode origin;

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
}
