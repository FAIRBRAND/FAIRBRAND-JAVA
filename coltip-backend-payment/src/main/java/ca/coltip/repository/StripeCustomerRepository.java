package ca.coltip.repository;

import ca.coltip.data.entity.StripeCustomer;
import ca.coltip.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StripeCustomerRepository extends JpaRepository<StripeCustomer, Long> {
  Optional<StripeCustomer> findFirstByUser(User userId);

  Optional<StripeCustomer> findFirstByStripeCustomerId(String stripeCustomerId);
}
