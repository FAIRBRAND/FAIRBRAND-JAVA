package ca.coltip.config;

import ca.coltip.data.dto.ServerError;
import ca.coltip.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class ExceptionHandlerControllerAdvice {
  @ExceptionHandler(value = {
    CalendarEventCreationException.class,
    FreeBusyException.class,
    UnavailableSlotException.class,
    UserNotFoundException.class,
    CalendarEventDeleteException.class
  })
  public ResponseEntity<ServerError> handleException(Exception ex) {
    if (ex instanceof CalendarEventCreationException) {
      return ResponseEntity
        .internalServerError()
        .body(new ServerError(ex.getMessage()));
    } else if (ex instanceof FreeBusyException) {
      return ResponseEntity
        .internalServerError()
        .body(new ServerError(ex.getMessage()));
    } else if (ex instanceof UnavailableSlotException) {
      return ResponseEntity
        .badRequest()
        .body(new ServerError("Slot not available"));
    } else if (ex instanceof UserNotFoundException) {
        return ResponseEntity
          .badRequest()
          .body(new ServerError("User not found"));
    } else if (ex instanceof CalendarEventDeleteException) {
      return ResponseEntity
        .badRequest()
        .body(new ServerError(ex.getMessage()));
    }
    return ResponseEntity
      .internalServerError()
      .build();
  }
}
