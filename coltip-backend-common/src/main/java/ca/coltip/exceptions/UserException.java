package ca.coltip.exceptions;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException {
    private HttpStatus statusCode;

    public UserException(String message) {
        super(message);
    }
    public UserException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
