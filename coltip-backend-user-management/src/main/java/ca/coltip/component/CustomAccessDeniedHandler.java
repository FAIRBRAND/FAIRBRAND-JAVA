package ca.coltip.component;

import ca.coltip.data.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
  private final MessageSource messageSource;

  @Override
  public void handle(
    HttpServletRequest request,
    HttpServletResponse response,
    AccessDeniedException accessDeniedException
  ) throws IOException {
    final var status = HttpServletResponse.SC_FORBIDDEN;
    response.setStatus(status);
    response.setContentType("application/json");
    response
      .getWriter()
      .write(new ObjectMapper().writeValueAsString(
        new ErrorResponse(
          status,
          messageSource.getMessage("response.error.forbidden", null, request.getLocale())
        )
      ));
  }
}
