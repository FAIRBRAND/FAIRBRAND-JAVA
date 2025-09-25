package ca.coltip.data.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)
public class ApiResponse<T> {
  public static <T> ApiResponse<T> of(int status, String message, T data) {
    return new ApiResponse<>(status, message, data);
  }

  public static <T> ApiResponse<T> ok(T data) {
    final var status = HttpStatus.OK;
    return of(status.value(), status.getReasonPhrase(), data);
  }

  public static <T> ApiResponse<T> ok(String message, T data) {
    return of(HttpStatus.OK.value(), message, data);
  }

  private int status;
  private String message;
  private T data;
}
