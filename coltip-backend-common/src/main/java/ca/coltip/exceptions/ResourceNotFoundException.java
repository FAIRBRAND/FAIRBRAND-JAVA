package ca.coltip.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }

    public ResourceNotFoundException(String resourceName, Object... fieldNameValuePairs) {
        super(buildMessage(resourceName, fieldNameValuePairs));
    }

    private static String buildMessage(String resourceName, Object... fieldNameValuePairs) {
        if (fieldNameValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Field name-value pairs must be even.");
        }

        StringBuilder sb = new StringBuilder(resourceName + " not found with ");
        for (int i = 0; i < fieldNameValuePairs.length; i += 2) {
            String field = fieldNameValuePairs[i].toString();
            Object value = fieldNameValuePairs[i + 1];
            sb.append(String.format("%s: '%s'", field, value));
            if (i + 2 < fieldNameValuePairs.length) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}