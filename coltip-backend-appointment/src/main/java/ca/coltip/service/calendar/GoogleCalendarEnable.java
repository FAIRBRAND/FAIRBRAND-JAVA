package ca.coltip.service.calendar;

import ca.coltip.data.entity.Slot;
import ca.coltip.data.entity.User;
import ca.coltip.exception.GoogleCalendarClientException;
import ca.coltip.exception.SlotUnavailableException;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class GoogleCalendarEnable implements GoogleCalendar {
  private static DateTime dateTimeFrom(Instant instant) {
    return new DateTime(Date.from(instant));
  }

  private static EventDateTime eventDateTimeFrom(Instant instant) {
    return new EventDateTime()
      .setDateTime(dateTimeFrom(instant));
  }

  private static EventAttendee eventAttendeeFrom(User user) {
    final var attendee = new EventAttendee();
    attendee.setEmail(user.getEmail());
    return attendee;
  }

  private static Event createFromSlot(Slot slot) {
    return new Event()
      .setSummary(slot.getTitle())
      .setDescription(slot.getDescription())
      .setStart(eventDateTimeFrom(slot.getStartAt()))
      .setEnd(eventDateTimeFrom(slot.getEndAt()))
      .setKind("Appointment")
      .setAttendees(List.of(
        eventAttendeeFrom(slot.getUser())
      ));
  }

  private final Calendar calendar;
  private final String calendarId;

  public GoogleCalendarEnable(Environment env) throws IOException, GeneralSecurityException {
    this.calendarId = env.getRequiredProperty("google.calendar.id");

    final var resource = new ClassPathResource("credentials.json");
    final var credentials = ServiceAccountCredentials
      .fromStream(resource.getInputStream())
      .createScoped(Collections.singleton("https://www.googleapis.com/auth/calendar"));

    this.calendar = new Calendar.Builder(
      GoogleNetHttpTransport.newTrustedTransport(),
      GsonFactory.getDefaultInstance(),
      new HttpCredentialsAdapter(credentials)
    )
      .setApplicationName("Fairbrand's Spring Google Calendar Integration")
      .build();
  }

  private boolean isSlotUnavailable(Slot slot) throws GoogleCalendarClientException {
    try {
      final var request = new FreeBusyRequest()
        .setTimeMin(dateTimeFrom(slot.getStartAt()))
        .setTimeMax(dateTimeFrom(slot.getStartAt()))
        .setItems(Collections.singletonList(new FreeBusyRequestItem().setId(calendarId)));

      return calendar
        .freebusy()
        .query(request)
        .execute()
        .getCalendars()
        .get(calendarId)
        .getBusy()
        .isEmpty();
    } catch (IOException e) {
      throw new GoogleCalendarClientException(e);
    }
  }

  @Override
  public String add(Slot slot) throws GoogleCalendarClientException, SlotUnavailableException {
    if (isSlotUnavailable(slot)) {
      throw new SlotUnavailableException();
    }

    try {
      return calendar
        .events()
        .insert(calendarId, createFromSlot(slot))
        .execute()
        .getId();
    } catch (IOException e) {
      throw new GoogleCalendarClientException(e);
    }
  }

  @Override
  public void delete(Slot slot) throws GoogleCalendarClientException {
    try {
      calendar
        .events()
        .delete(calendarId, slot.getGoogleCalendarId())
        .execute();
    } catch (IOException e) {
      throw new GoogleCalendarClientException(e);
    }
  }
}
