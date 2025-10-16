package ca.coltip.service;

import ca.coltip.data.entity.StripeCustomer;
import ca.coltip.data.entity.User;
import ca.coltip.repository.StripeCustomerRepository;
import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.stripe.param.CustomerCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class StripeService {
  private final StripeClient client;
  private final StripeCustomerRepository stripeCustomerRepository;

  public Event webhookChecking(
    String payload,
    String signature
  ) throws SignatureVerificationException {
    return Webhook.constructEvent(
      payload,
      signature,
      ""
    );
  }

  @Transactional
  public Customer createOrGetCustomer(User user) throws StripeException {
    final var customerOptional = stripeCustomerRepository.findFirstByUser(user);

    if (customerOptional.isPresent()) {
      return client.v1()
        .customers()
        .retrieve(customerOptional.get().getStripeCustomerId());
    }

    final var params = CustomerCreateParams.builder()
      .setEmail(user.getEmail())
      .setPhone(user.getPhoneNumber())
      .setName(user.getFirstname() + " " + user.getLastname())
      .putMetadata("app_user_id", String.valueOf(user.getId()))
      .build();

    final var customer = client.v1().customers().create(params);
    stripeCustomerRepository.save(
      StripeCustomer.builder()
        .user(user)
        .stripeCustomerId(customer.getId())
        .build()
    );

    return customer;
  }
}
