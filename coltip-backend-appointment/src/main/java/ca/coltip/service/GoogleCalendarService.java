package ca.coltip.service;

import ca.coltip.exception.CalendarEventCreationException;
import ca.coltip.exception.CalendarEventDeleteException;
import ca.coltip.exception.FreeBusyException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collections;

@Service
public class GoogleCalendarService {
  private static final String CALENDAR_ID = "primary";
  // ou l’ID de ton agenda (trouvé dans les paramètres Google Calendar)

  private final Calendar calendar;

  public GoogleCalendarService(Calendar calendar) {
    this.calendar = calendar;
  }

  private DateTime datetime(ZonedDateTime value) {
    return new DateTime(value.toString());
  }

  public String addEvent(
    String summary,
    String description,
    ZonedDateTime startDateTime,
    ZonedDateTime endDateTime
  ) throws CalendarEventCreationException {
    try {
      Event event = createEvent(summary, description, startDateTime, endDateTime);
      event = calendar.events().insert(CALENDAR_ID, event).execute();
      return event.getId();
    } catch (IOException e) {
      throw new CalendarEventCreationException(e.getMessage());
    }
  }

  public void deleteEvent(String id) throws CalendarEventDeleteException {
    try {
      calendar
        .events()
        .delete(CALENDAR_ID, id)
        .execute();
    } catch (IOException e) {
      throw new CalendarEventDeleteException(e.getMessage());
    }
  }

  private Event createEvent(String summary, String description, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
    Event event = new Event()
      .setSummary(summary)
      .setDescription(description);

    EventDateTime start = new EventDateTime()
      .setDateTime(datetime(startDateTime))
      .setTimeZone(startDateTime.getZone().getId());

    EventDateTime end = new EventDateTime()
      .setDateTime(datetime(endDateTime))
      .setTimeZone(endDateTime.getZone().getId());

    event.setStart(start);
    event.setEnd(end);

    return event;
  }

  public boolean isSlotAvailable(ZonedDateTime startDateTime, ZonedDateTime endDateTime) throws FreeBusyException {
    try {
      FreeBusyRequest request = new FreeBusyRequest()
        .setTimeMin(datetime(startDateTime))
        .setTimeMax(datetime(endDateTime))
        .setTimeZone(startDateTime.getZone().getId())
        .setItems(Collections.singletonList(new FreeBusyRequestItem().setId(CALENDAR_ID)));
      FreeBusyResponse response = calendar.freebusy().query(request).execute();
      var busyTimes = response.getCalendars().get(CALENDAR_ID).getBusy();
      return busyTimes.isEmpty();
    } catch (IOException e) {
      throw new FreeBusyException(e.getMessage());
    }
  }
}
