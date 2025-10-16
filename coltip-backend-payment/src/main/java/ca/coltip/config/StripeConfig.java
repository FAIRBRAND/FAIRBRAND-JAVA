package ca.coltip.config;

import com.stripe.StripeClient;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class StripeConfig {
  @Bean
  public StripeClient stripeClient() {
    return StripeClient.builder()
      .setApiKey("")
      .setMaxNetworkRetries(2)
      .build();
  }
}
