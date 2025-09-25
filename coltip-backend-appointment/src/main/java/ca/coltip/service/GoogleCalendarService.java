package ca.coltip.service;

import ca.coltip.data.entity.Slot;
import ca.coltip.exception.GoogleCalendarClientException;
import ca.coltip.exception.SlotUnavailableException;
import ca.coltip.service.calendar.GoogleCalendar;
import ca.coltip.service.calendar.GoogleCalendarDisable;
import ca.coltip.service.calendar.GoogleCalendarEnable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class GoogleCalendarService implements GoogleCalendar {
  private final GoogleCalendar googleCalendar;

  public GoogleCalendarService(Environment env) throws GeneralSecurityException, IOException {
    final var enabled = env.getProperty("google.calendar.enable", Boolean.class, true);
    this.googleCalendar = enabled ? new GoogleCalendarEnable(env) : new GoogleCalendarDisable();
  }

  @Override
  public String add(Slot slot) throws GoogleCalendarClientException, SlotUnavailableException {
    return googleCalendar.add(slot);
  }

  @Override
  public void delete(Slot slot) throws GoogleCalendarClientException {
    googleCalendar.delete(slot);
  }
}
