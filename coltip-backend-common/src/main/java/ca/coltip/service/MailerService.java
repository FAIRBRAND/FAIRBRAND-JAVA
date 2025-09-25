package ca.coltip.service;

import ca.coltip.util.TemplateMailParam;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
@AllArgsConstructor
public class MailerService {
  private final Environment environment;
  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;
  private final MessageSource messageSource;

  public String noReplyAddress() {
    return environment.getRequiredProperty("coltip.mailing.no_reply");
  }

  public final void sendTemplateMail(TemplateMailParam param) throws MessagingException {
    final var message = mailSender.createMimeMessage();

    final var helper = new MimeMessageHelper(message, true);
    helper.setFrom(noReplyAddress());
    helper.setTo(param.getTargetAddresses());

    final var subject = param.getSubject();
    helper.setSubject(
      messageSource.getMessage(
        subject.getCode(),
        subject.getArgs(),
        param.getLocale()
      )
    );

    helper.setText(
      templateEngine.process(
        param.getTemplate(),
        param
      ),
      true
    );

    mailSender.send(message);
  }
}
