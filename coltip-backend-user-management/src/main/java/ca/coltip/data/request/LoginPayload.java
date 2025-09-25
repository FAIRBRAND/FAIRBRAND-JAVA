package ca.coltip.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginPayload {
  @Email(message = "{validation.error.email}")
  private String email;

  @NotBlank(message = "{validation.error.blank}")
  private String password;
}
