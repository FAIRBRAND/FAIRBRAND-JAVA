package ca.coltip.service;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RefundService {
  private final StripeClient client;

  public Refund requestFullRefund(String paymentIntentId) throws StripeException {
    return client.v1().refunds().create(
      RefundCreateParams.builder()
      .setPaymentIntent(paymentIntentId)
      .build()
    );
  }

  public Refund requestPartialRefund(String paymentIntentId, long amount) throws StripeException {
    return client.v1().refunds().create(
      RefundCreateParams.builder()
        .setPaymentIntent(paymentIntentId)
        .setAmount(amount)
        .build()
    );
  }
}
