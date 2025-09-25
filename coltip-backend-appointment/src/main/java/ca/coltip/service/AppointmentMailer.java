package ca.coltip.service;

import ca.coltip.data.entity.AppointmentRequest;
import ca.coltip.data.entity.Slot;
import ca.coltip.data.request.ValidationStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppointmentMailer {
  private final MailerService mailer;

  /**
   * Notify client for validated appointment
   */
  public void notifyClientForValidation(
    AppointmentRequest appointment,
    ValidationStatus validationStatus
  ) {
    // TODO
    // mailSender.send(new SimpleMailMessage());
  }

  /**
   * Notify all admins about the pending appointment for validation
   */
  public void notifyAdminsForRequest(AppointmentRequest appointment) {
    // TODO
    // mailSender.send(new SimpleMailMessage());
  }

  public void notifyAdminsForCancellation(Slot slot) {
    // TODO
    // mailSender.send(new SimpleMailMessage());
  }
}
