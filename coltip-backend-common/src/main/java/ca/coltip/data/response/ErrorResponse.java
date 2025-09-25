package ca.coltip.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
  public static ErrorResponse of(int status, String message) {
    return new ErrorResponse(status, message);
  }

  private int status;
  private String message;
}
