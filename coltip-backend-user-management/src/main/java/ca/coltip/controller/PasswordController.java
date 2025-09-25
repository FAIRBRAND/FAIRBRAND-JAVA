package ca.coltip.controller;

import ca.coltip.data.dto.UserDto;
import ca.coltip.data.request.ForgetPasswordPayload;
import ca.coltip.data.request.PasswordChangePayload;
import ca.coltip.data.request.ResetPasswordPayload;
import ca.coltip.data.response.ApiResponse;
import ca.coltip.exception.*;
import ca.coltip.service.PasswordService;
import ca.coltip.service.UserTranslationService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@AllArgsConstructor
@RequestMapping("password")
public class PasswordController {
  private final PasswordService passwordService;
  private final UserTranslationService translate;

  @PostMapping("forget")
  public ApiResponse<String> forgetPassword(
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @RequestBody ForgetPasswordPayload payload
  ) throws MessagingException {
    try {
      passwordService.requestChange(payload);
      return ApiResponse.ok(translate.verificationMailSent(locale));
    } catch (UserNotFoundException e) {
      throw new NotFoundException(translate.userNotFound(locale));
    }
  }

  @PostMapping("reset")
  public ApiResponse<String> resetPassword(
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @RequestBody ResetPasswordPayload payload
  ) {
    try {
      passwordService.reset(payload);
      return ApiResponse.ok(translate.passwordSuccessReset(locale));
    } catch (UserNotFoundException e) {
      throw new NotFoundException(translate.userNotFound(locale));
    } catch (InvalidOtpCodeException e) {
      throw new BadRequestException(translate.invalidOtp(locale));
    }
  }

  @PostMapping("change")
  public ApiResponse<UserDto> changePassword(
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @AuthenticationPrincipal UserDetails userDetails,
    @RequestBody PasswordChangePayload payload
  ) {
    try {
      return ApiResponse.ok(
        translate.passwordSuccessReset(locale),
        passwordService.change(userDetails, payload)
      );
    } catch (PasswordMismatchException e) {
      throw new BadRequestException(translate.wrongPassword(locale));
    } catch (UserNotFoundException e) {
      throw new NotFoundException(translate.userNotFound(locale));
    }
  }
}
