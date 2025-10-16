package ca.coltip.service;

import ca.coltip.data.entity.User;
import ca.coltip.data.request.PaymentIntentPayload;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeCollection;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentListParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PaymentService {
  private final StripeClient client;
  private final StripeService stripeService;

  public PaymentIntent makePayment(User user, PaymentIntentPayload payload) throws StripeException {
    final var customer = stripeService.createOrGetCustomer(user);

    final var params = PaymentIntentCreateParams.builder()
      .setAmount(payload.getAmount())
      .setCurrency(payload.getCurrency())
      .setCustomer(customer.getId())
      .setReceiptEmail(user.getEmail())
      .setAutomaticPaymentMethods(
        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
        .setEnabled(true)
        .build()
      )
      .build();

    return client.v1().paymentIntents().create(params);
  }

  public PaymentIntent confirmPayment(
    String paymentIntentId,
    String paymentMethodId
  ) throws StripeException {
    final var paymentIntent = client.v1().paymentIntents().retrieve(paymentIntentId);
    return paymentIntent.confirm(
      PaymentIntentConfirmParams.builder()
      .setPaymentMethod(paymentMethodId)
      .build()
    );
  }

  public StripeCollection<PaymentIntent> listPaymentsByUser(User user) throws StripeException {
    final var customer = stripeService.createOrGetCustomer(user);
    return client
      .v1()
      .paymentIntents()
      .list(
        PaymentIntentListParams.builder()
          .setCustomer(customer.getId())
          .build()
      );
  }

  public PaymentIntent cancelPayment(String paymentId) throws StripeException {
    return client.v1().paymentIntents().retrieve(paymentId).cancel();
  }
}
