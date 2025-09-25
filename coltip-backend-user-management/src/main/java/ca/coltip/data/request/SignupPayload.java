package ca.coltip.data.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignupPayload {
  @NotBlank(message = "{validation.error.blank}")
  public String firstname;

  @NotBlank(message = "{validation.error.blank}")
  public String lastname;

  @Email(message = "{validation.error.email}")
  public String email;

  @NotBlank(message = "{validation.error.blank}")
  public String password;

  @NotNull(message = "{validation.error.null}")
  public Long languageId;
}
