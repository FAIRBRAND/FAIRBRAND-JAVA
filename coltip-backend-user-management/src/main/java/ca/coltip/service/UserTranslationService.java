package ca.coltip.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@AllArgsConstructor
@Service
public class UserTranslationService {
  private final MessageSource messageSource;

  public String verificationMailSent(Locale locale) {
    return messageSource.getMessage("auth.ok.verification_mail_sent", null, locale);
  }

  public String passwordSuccessReset(Locale locale) {
    return messageSource.getMessage("auth.ok.password.reset", null, locale);
  }

  public String couldNotLogin(Locale locale) {
    return messageSource.getMessage("auth.error.could_not_login", null, locale);
  }

  public String tokenExpired(Locale locale) {
    return messageSource.getMessage("auth.error.token.expired", null, locale);
  }

  public String invalidToken(Locale locale) {
    return messageSource.getMessage("auth.error.token.invalid", null, locale);
  }

  public String invalidOtp(Locale locale) {
    return messageSource.getMessage("auth.error.otp.invalid", null, locale);
  }

  public String otpExpired(Locale locale) {
    return messageSource.getMessage("auth.error.otp.expired", null, locale);
  }

  public String couldNotResetPass(Locale locale) {
    return messageSource.getMessage("auth.error.password.unable", null, locale);
  }

  public String wrongPassword(Locale locale) {
    return messageSource.getMessage("auth.error.password.wrong", null, locale);
  }

  public String userNotFound(Locale locale) {
    return messageSource.getMessage("user.error.not_found", null, locale);
  }

  public String noField(Locale locale, String field) {
    return messageSource.getMessage("user.error.update.not_found." + field, null, locale);
  }

  public String alreadyExits(Locale locale) {
    return messageSource.getMessage("user.error.conflict.exists", null, locale);
  }

  public String mailUpdateSent(Locale locale) {
    return messageSource.getMessage("user.ok.update.mail.sent", null, locale);
  }
}
