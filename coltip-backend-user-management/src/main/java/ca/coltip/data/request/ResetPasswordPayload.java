package ca.coltip.data.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = true)
public class ResetPasswordPayload extends ForgetPasswordPayload {
  @NotBlank(message = "{validation.error.blank}")
  private String verificationCode;

  @NotBlank(message = "{validation.error.blank}")
  private String newPassword;
}
