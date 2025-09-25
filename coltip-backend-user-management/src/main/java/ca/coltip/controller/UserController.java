package ca.coltip.controller;

import ca.coltip.data.dto.UserDto;
import ca.coltip.data.request.MailUpdatePayload;
import ca.coltip.data.request.UserUpdatePayload;
import ca.coltip.data.response.ApiResponse;
import ca.coltip.exception.BadRequestException;
import ca.coltip.exception.FieldUpdateNotFound;
import ca.coltip.exception.NotFoundException;
import ca.coltip.exception.UserNotFoundException;
import ca.coltip.service.UserMailerService;
import ca.coltip.service.UserService;
import ca.coltip.service.UserTranslationService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@AllArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
  private final UserService userService;
  private final UserTranslationService userTranslate;
  private final UserMailerService mailer;

  @GetMapping("me")
  public ApiResponse<UserDto> whoami(
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @AuthenticationPrincipal UserDetails userDetails
  ) {
    try {
      return ApiResponse.ok(userService.whoami(userDetails).toDto());
    } catch (UserNotFoundException e) {
      throw new NotFoundException(userTranslate.userNotFound(locale));
    }
  }

  @PutMapping
  public ApiResponse<UserDto> updateProfile(
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @AuthenticationPrincipal UserDetails userDetails,
    @RequestBody UserUpdatePayload payload
  ) {
    try {
      return ApiResponse.ok(userService.updateProfile(userDetails, payload));
    } catch (UserNotFoundException e) {
      throw new NotFoundException(userTranslate.userNotFound(locale));
    } catch (FieldUpdateNotFound e) {
      throw new BadRequestException(userTranslate.noField(locale, e.getField()));
    }
  }

  @PutMapping("change_mail")
  public ApiResponse<String> changeMail(
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @AuthenticationPrincipal UserDetails userDetails,
    @RequestBody MailUpdatePayload payload
  ) throws MessagingException {
    try {
      final var user = userService.whoami(userDetails);
      mailer.requestMailUpdate(user, payload);
      return ApiResponse.ok(userTranslate.mailUpdateSent(locale));
    } catch (UserNotFoundException e) {
      throw new NotFoundException(userTranslate.userNotFound(locale));
    }
  }

  @DeleteMapping
  public ApiResponse<UserDto> disableProfile(
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @AuthenticationPrincipal UserDetails userDetails
  ) {
    try {
      return ApiResponse.ok(userService.disableProfile(userDetails));
    } catch (UserNotFoundException e) {
      throw new NotFoundException(userTranslate.userNotFound(locale));
    }
  }
}
