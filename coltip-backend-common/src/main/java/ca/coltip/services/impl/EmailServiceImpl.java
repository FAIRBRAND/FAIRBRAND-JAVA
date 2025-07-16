package ca.coltip.services.impl;

import ca.coltip.services.EmailService;
import ca.coltip.strategy.EmailStrategy;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final EmailStrategy emailStrategy;

    public EmailServiceImpl(EmailStrategy emailStrategy) {
        this.emailStrategy = emailStrategy;
    }

    @Override
    public void sendEmail(String to, String subject, String text) {
        emailStrategy.sendEmail(to, subject, text);
    }
}
