package ca.coltip.service.calendar;

import ca.coltip.data.entity.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class GoogleCalendarDisable implements GoogleCalendar {
  private static final Logger LOG = LoggerFactory.getLogger(GoogleCalendarDisable.class);

  @Override
  public String add(Slot slot) {
    LOG.warn("(Disabled feature): Adding slot to Google Calendar");
    return UUID.randomUUID().toString();
  }

  @Override
  public void delete(Slot slot) {
    LOG.warn("(Disabled feature): Deleting slot to Google Calendar");
  }
}
