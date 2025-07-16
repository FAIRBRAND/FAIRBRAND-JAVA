package ca.coltip.strategy;

public interface EmailStrategy {
    void sendEmail(String to, String subject, String text);
}
