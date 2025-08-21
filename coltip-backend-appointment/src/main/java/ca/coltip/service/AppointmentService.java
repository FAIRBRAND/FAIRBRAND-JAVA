package ca.coltip.service;

import ca.coltip.data.dto.AppointmentDto;
import ca.coltip.data.dto.AppointmentInValidation;
import ca.coltip.data.dto.ValidationStatus;
import ca.coltip.data.dto.VerifiedAppointment;
import ca.coltip.data.entities.*;
import ca.coltip.data.repository.AppointmentHistoryRepository;
import ca.coltip.data.repository.AppointmentRepository;
import ca.coltip.data.request.AppointmentPayload;
import ca.coltip.data.request.VerificationPayload;
import ca.coltip.data.util.ServiceUtility;
import ca.coltip.exception.CalendarEventCreationException;
import ca.coltip.exception.CalendarEventDeleteException;
import ca.coltip.exception.FreeBusyException;
import ca.coltip.exception.UnavailableSlotException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Service
public class AppointmentService {
  private final AppointmentRepository repository;
  private final AppointmentHistoryRepository historyRepository;
  private final GoogleCalendarService calendarService;

  public AppointmentService(
    AppointmentRepository repository,
    AppointmentHistoryRepository historyRepository,
    GoogleCalendarService calendarService
  ) {
    this.repository = repository;
    this.historyRepository = historyRepository;
    this.calendarService = calendarService;
  }

  public AppointmentDto getById(Integer id) {
    return AppointmentDto.from(repository.getAppointmentById(id));
  }

  public Page<AppointmentDto> getAll(
    Pageable pageable,
    AppointmentStatus status,
    LocalDate start,
    LocalDate end
  ) {
    if (status == null) {
      return repository.findAllByStartAndEndDate(start, end, pageable);
    }
    return repository.findAllByStatusAndStartAndEndDate(status, start, end, pageable);
  }

  private User userFromId(int id) {
    final var user = new User();
    user.setId(id);
    return user;
  }

  private AppointmentStatus appointmentStatusFromValidation(ValidationStatus status) {
    return switch (status) {
      case VALIDATED -> AppointmentStatus.BOOKED;
      case REJECTED -> AppointmentStatus.CANCELED;
    };
  }

  private void preValidate(
    ZonedDateTime start,
    ZonedDateTime end
  ) throws FreeBusyException, UnavailableSlotException {
    if (!calendarService.isSlotAvailable(start, end)) {
      throw new UnavailableSlotException();
    }
  }

  public AppointmentInValidation create(
    AppointmentPayload payload,
    int userId
  ) throws FreeBusyException, UnavailableSlotException {
    preValidate(payload.getStartAt(), payload.getEndAt());
    final var appointment = new Appointment();
    appointment.setTitle(payload.getTitle());
    appointment.setDescription(payload.getDescription());
    appointment.setStatus(AppointmentStatus.VERIFY);
    appointment.setStartAt(payload.getStartAt());
    appointment.setEndAt(payload.getEndAt());
    appointment.setUser(userFromId(userId));
    return AppointmentInValidation.from(repository.save(appointment));
  }

  private AppointmentHistory history(Appointment appointment) {
    final var history = new AppointmentHistory();
    history.setHistoryState(HistoryState.UPDATE);
    history.setAppointment(appointment);
    history.setStatus(appointment.getStatus());
    history.setTitle(appointment.getTitle());
    history.setDescription(appointment.getDescription());
    history.setStartAt(appointment.getStartAt());
    history.setEndAt(appointment.getEndAt());
    return history;
  }

  private AppointmentHistory historyUpdate(Appointment appointment) {
    final var history = history(appointment);
    history.setHistoryState(HistoryState.UPDATE);
    return history;
  }

  private AppointmentHistory historyDelete(Appointment appointment) {
    final var history = history(appointment);
    history.setHistoryState(HistoryState.DELETE);
    return history;
  }

  @Transactional
  public AppointmentInValidation updateById(
    Integer id,
    AppointmentPayload data
  ) throws FreeBusyException, UnavailableSlotException, CalendarEventDeleteException {
    preValidate(data.getStartAt(), data.getEndAt());
    final var appointment = repository.getAppointmentById(id);
    appointment.setStatus(AppointmentStatus.VERIFY);

    calendarService.deleteEvent(appointment.getGoogleCalendarId());
    appointment.setGoogleCalendarId(null);

    ServiceUtility.modify(appointment::setTitle, data::getTitle);
    ServiceUtility.modify(appointment::setDescription, data::getDescription);
    ServiceUtility.modify(appointment::setStartAt, data::getStartAt);
    ServiceUtility.modify(appointment::setEndAt, data::getEndAt);
    final var update = repository.save(appointment);
    historyRepository.save(historyUpdate(appointment));
    return AppointmentInValidation.from(update);
  }

  @Transactional
  public VerifiedAppointment verify(VerificationPayload payload) throws CalendarEventCreationException {
    final var appointment = repository.getAppointmentById(payload.getAppointmentId());
    final var status = appointmentStatusFromValidation(payload.getStatus());
    appointment.setStatus(status);

    if (status == AppointmentStatus.BOOKED) {
      String id = calendarService.addEvent(
        appointment.getTitle(),
        appointment.getDescription(),
        appointment.getStartAt(),
        appointment.getEndAt()
      );

      appointment.setGoogleCalendarId(id);
    }

    final var update = repository.save(appointment);
    historyRepository.save(historyUpdate(appointment));
    return VerifiedAppointment.from(update);
  }

  @Transactional
  public AppointmentDto deleteById(Integer id) throws CalendarEventDeleteException {
    final var appointment = repository.getAppointmentById(id);
    repository.deleteById(id);
    calendarService.deleteEvent(appointment.getGoogleCalendarId());
    historyRepository.save(historyDelete(appointment));
    return AppointmentDto.from(appointment);
  }
}
