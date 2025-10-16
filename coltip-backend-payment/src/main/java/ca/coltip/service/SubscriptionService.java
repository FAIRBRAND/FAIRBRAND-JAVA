package ca.coltip.service;

import ca.coltip.data.entity.User;
import com.stripe.StripeClient;
import com.stripe.model.StripeCollection;
import com.stripe.model.Subscription;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.SubscriptionListParams;
import com.stripe.param.SubscriptionUpdateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SubscriptionService {
  private final StripeClient client;
  private final StripeService stripeService;

  public Subscription createSubscription(User user, String priceId) throws Exception {
    final var customer = stripeService.createOrGetCustomer(user);
    return client.v1()
      .subscriptions()
      .create(
        SubscriptionCreateParams.builder()
          .setCustomer(customer.getId())
          .addItem(
            SubscriptionCreateParams.Item.builder()
              .setPrice(priceId)
              .build()
          )
          .setPaymentBehavior(SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE)
          .addExpand("latest_invoice.payment_intent")
          .build()
      );
  }

  public StripeCollection<Subscription> listUserSubscriptions(User user) throws Exception {
    final var customer = stripeService.createOrGetCustomer(user);

    return client.v1()
      .subscriptions()
      .list(SubscriptionListParams.builder()
        .setCustomer(customer.getId())
        .build()
      );
  }

  public Subscription cancelImmediately(String subscriptionId) throws Exception {
    return client.v1().subscriptions().cancel(subscriptionId);
  }

  public Subscription changePlan(String subscriptionId, String newPriceId) throws Exception {
    final var subscription = client.v1().subscriptions().retrieve(subscriptionId);
    final var oldPriceId = subscription.getItems().getData().getFirst().getId();
    final var params = SubscriptionUpdateParams.builder()
      .addItem(
        SubscriptionUpdateParams.Item.builder()
          .setId(oldPriceId)
          .setPrice(newPriceId)
          .build()
      )
      .build();
    return subscription.update(params);
  }
}
