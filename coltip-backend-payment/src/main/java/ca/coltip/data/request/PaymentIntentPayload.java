package ca.coltip.data.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentIntentPayload {
  private Long amount;
  private boolean isCent;
  private String currency;

  public Long getAmount() {
    if (!isCent) return amount * 100;
    return amount;
  }
}
