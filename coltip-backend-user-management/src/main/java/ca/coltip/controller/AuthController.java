package ca.coltip.controller;

import ca.coltip.data.request.LoginPayload;
import ca.coltip.data.request.RefreshTokenPayload;
import ca.coltip.data.request.SignupConfirmationPayload;
import ca.coltip.data.request.SignupPayload;
import ca.coltip.data.response.ApiResponse;
import ca.coltip.data.response.LoginResponse;
import ca.coltip.exception.*;
import ca.coltip.service.AuthService;
import ca.coltip.service.UserTranslationService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@AllArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthController {
  private final AuthService service;
  private final UserTranslationService translate;

  @PostMapping("login")
  public ApiResponse<LoginResponse> login(
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @RequestBody @Validated LoginPayload payload
  ) {
    try {
      return ApiResponse.ok(service.login(payload));
    } catch (AuthenticationException e) {
      throw new UnauthorizedException(translate.couldNotLogin(locale));
    }
  }

  @PostMapping("refresh_token")
  public ApiResponse<LoginResponse> refreshToken(
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @RequestBody RefreshTokenPayload payload
  ) {
    try {
      return ApiResponse.ok(service.refreshToken(payload));
    } catch (OtpCodeExpirationException e) {
      throw new BadRequestException(translate.tokenExpired(locale));
    } catch (TokenNotFoundException e) {
      throw new BadRequestException(translate.invalidToken(locale));
    }
  }

  @PostMapping("signup")
  public ApiResponse<String> signup(
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @RequestBody SignupPayload payload
  ) throws MessagingException {
    try {
      service.signup(payload);
      return ApiResponse.ok(translate.verificationMailSent(locale));
    } catch (UserConflictException e) {
      throw new HttpException(HttpStatus.CONFLICT, translate.alreadyExits(locale));
    }
  }

  @PostMapping("signup/verify")
  public ApiResponse<LoginResponse> confirmSignup(
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @RequestBody SignupConfirmationPayload payload
  ) throws MessagingException {
    try {
      return ApiResponse.ok(service.confirmSignup(payload));
    } catch (InvalidOtpCodeException e) {
      throw new BadRequestException(translate.invalidOtp(locale));
    } catch (OtpCodeExpirationException e) {
      throw new BadRequestException(translate.otpExpired(locale));
    } catch (UserNotFoundException e) {
      throw new BadRequestException(translate.userNotFound(locale));
    }
  }
}