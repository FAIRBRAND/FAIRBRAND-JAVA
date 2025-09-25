package ca.coltip.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class HttpException extends RuntimeException {
  private int status;

  public HttpException(int status, String message) {
    super(message);
    this.status = status;
  }

  public HttpException(int status, String message, Throwable cause) {
    super(message, cause);
    this.status = status;
  }

  public HttpException(HttpStatus status, String message) {
    super(message);
    this.status = status.value();
  }

  public HttpException(HttpStatus status, String message, Throwable cause) {
    super(message, cause);
    this.status = status.value();
  }
}
