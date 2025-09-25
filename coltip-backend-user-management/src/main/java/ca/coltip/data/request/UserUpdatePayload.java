package ca.coltip.data.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserUpdatePayload {
  public String firstname;
  public String lastname;
  private String phoneNumber;
  private String description;
  private Long domainId;
  private Long countryId;
  private Long languageId;
}
