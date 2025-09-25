package ca.coltip.service.calendar;

import ca.coltip.data.entity.Slot;
import ca.coltip.exception.GoogleCalendarClientException;
import ca.coltip.exception.SlotUnavailableException;

public interface GoogleCalendar {
  String add(Slot slot) throws GoogleCalendarClientException, SlotUnavailableException;
  void delete(Slot slot) throws GoogleCalendarClientException;
}
