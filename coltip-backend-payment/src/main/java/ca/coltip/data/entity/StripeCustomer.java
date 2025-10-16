package ca.coltip.data.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stripe_customer")
public class StripeCustomer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "id_user", nullable = false, unique = true)
  private User user;

  @Column(name = "stripe_customer_id", nullable = false)
  private String stripeCustomerId;
}
