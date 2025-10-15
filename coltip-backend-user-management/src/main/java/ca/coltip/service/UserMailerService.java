package ca.coltip.service;

import ca.coltip.data.entity.PendingUserRegistration;
import ca.coltip.data.entity.User;
import ca.coltip.data.request.MailUpdatePayload;
import ca.coltip.util.TemplateMailParam;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserMailerService {
  private final MailerService mailer;

  public void sendOtpVerification(PendingUserRegistration user) throws MessagingException {
    final var param = new TemplateMailParam(
      user.getEmail(),
      user.getLanguage().toLocale(),
      "mail/otp_verification",
      new TemplateMailParam.Subject("auth.mail.otp.subject")
    );
    param.setVariable("name", user.getFirstname());
    param.setVariable("otp", user.getOtp());
    mailer.sendTemplateMail(param);
  }

  public void sendOtpVerification(
    User user,
    String code
  ) throws MessagingException {
    final var param = new TemplateMailParam(
      user.getEmail(),
      user.getLanguage().toLocale(),
      "mail/otp_verification",
      new TemplateMailParam.Subject("auth.mail.otp.subject")
    );
    param.setVariable("name", user.getFirstname());
    param.setVariable("otp", code);
    mailer.sendTemplateMail(param);
  }

  public void sendWelcome(User user) throws MessagingException {
    final var param = new TemplateMailParam(
      user.getEmail(),
      user.getLanguage().toLocale(),
      "mail/welcome",
      new TemplateMailParam.Subject("auth.mail.welcome.subject")
    );
    param.setVariable("name", user.getFirstname());
    mailer.sendTemplateMail(param);
  }

  public void requestMailUpdate(
    User user,
    MailUpdatePayload payload
  ) throws MessagingException {
    final var oldMail = user.getEmail();
    final var newMail = payload.getEmail();

    if (oldMail.equals(newMail)) {
      return;
    }

    final var param = new TemplateMailParam(
      newMail,
      user.getLanguage().toLocale(),
      "mail/mail_change",
      new TemplateMailParam.Subject("user.write.update.mail.subject")
    );

    // TODO: add params
    mailer.sendTemplateMail(param);
  }
}
