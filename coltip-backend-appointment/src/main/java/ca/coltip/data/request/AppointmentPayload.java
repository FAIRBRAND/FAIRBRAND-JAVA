package ca.coltip.data.request;

import ca.coltip.util.Patterns;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppointmentPayload {
  @NotBlank(message = "{validation.error.blank}")
  @NotNull(message = "{validation.error.null}")
  private String title;

  @NotNull(message = "{validation.error.null}")
  @NotBlank(message = "{validation.error.blank}")
  private String description;

  @NotNull(message = "{validation.error.null}")
  @Pattern(regexp = Patterns.DATE_FORMAT, message = "{validation.error.format.datetime}")
  private Instant startAt;

  @NotNull(message = "{validation.error.null}")
  @Pattern(regexp = Patterns.DATE_FORMAT, message = "{validation.error.format.datetime}")
  private Instant endAt;
}
