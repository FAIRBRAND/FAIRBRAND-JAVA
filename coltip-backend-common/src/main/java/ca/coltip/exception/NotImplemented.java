package ca.coltip.exception;

import org.springframework.http.HttpStatus;

public class NotImplemented extends HttpException {
  public NotImplemented(String message) {
    super(HttpStatus.NOT_IMPLEMENTED, message);
  }

  public NotImplemented(String message, Throwable cause) {
    super(HttpStatus.NOT_IMPLEMENTED, message, cause);
  }
}
