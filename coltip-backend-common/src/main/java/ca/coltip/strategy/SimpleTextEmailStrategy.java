package ca.coltip.strategy;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component("simpleEmail")
public class SimpleTextEmailStrategy implements EmailStrategy {

    private final JavaMailSender mailSender;

    public SimpleTextEmailStrategy(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("noreply@coltip.com");
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
