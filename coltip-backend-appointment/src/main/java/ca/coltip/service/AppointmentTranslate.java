package ca.coltip.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@AllArgsConstructor
public class AppointmentTranslate {
  private final MessageSource messageSource;

  public String slotUnavailable(Locale locale) {
    return messageSource.getMessage("appointment.error.slot.unavailable", null, locale);
  }

  public String googleCalendarError(Locale locale) {
    return messageSource.getMessage("appointment.error.google_calendar", null, locale);
  }

  public String resourceNotFound(Locale locale) {
    return messageSource.getMessage("appointment.error.slot.not_found", null, locale);
  }

  public String notTheSlotOwner(Locale locale) {
    return messageSource.getMessage("appointment.error.slot.not_owner", null, locale);
  }

  public String modificationPending(Locale locale) {
    return messageSource.getMessage("appointment.ok.modification.pending", null, locale);
  }

  public String appointmentRequestSent(Locale locale) {
    return messageSource.getMessage("appointment.ok.request_send", null, locale);
  }
}
