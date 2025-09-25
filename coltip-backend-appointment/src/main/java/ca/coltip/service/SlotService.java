package ca.coltip.service;

import ca.coltip.data.dto.SlotDto;
import ca.coltip.data.entity.AppointmentRequest;
import ca.coltip.data.entity.AppointmentStatus;
import ca.coltip.data.entity.Slot;
import ca.coltip.data.request.AppointmentPayload;
import ca.coltip.exception.GoogleCalendarClientException;
import ca.coltip.exception.SlotNotFoundException;
import ca.coltip.exception.SlotOwnerException;
import ca.coltip.exception.SlotUnavailableException;
import ca.coltip.repository.AppointmentRequestRepository;
import ca.coltip.repository.SlotRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SlotService {
  private static boolean isNotSlotOwner(Slot slot, UserDetails user) {
    return !slot.getUser().getEmail().equals(user.getUsername());
  }

  private static AppointmentRequest createAppointmentFromSlot(Slot slot) {
    final var appointment = new AppointmentRequest();
    appointment.setUser(slot.getUser());
    appointment.setTitle(slot.getTitle());
    appointment.setDescription(slot.getDescription());
    appointment.setStartAt(slot.getStartAt());
    appointment.setEndAt(slot.getEndAt());
    appointment.setStatus(AppointmentStatus.PENDING);
    return appointment;
  }

  private static void mergePayload(Slot slot, AppointmentPayload payload) {
    Optional
      .ofNullable(payload.getTitle())
      .ifPresent(slot::setTitle);

    Optional
      .ofNullable(payload.getDescription())
      .ifPresent(slot::setDescription);

    Optional
      .ofNullable(payload.getStartAt())
      .ifPresent(slot::setStartAt);

    Optional
      .ofNullable(payload.getEndAt())
      .ifPresent(slot::setEndAt);
  }

  private final AppointmentMailer mailer;
  private final GoogleCalendarService googleCalendarService;
  private final SlotRepository slotRepository;
  private final AppointmentRequestRepository appointmentRepository;

  public boolean isUnavailable(
    Instant startAt,
    Instant endAt,
    String timezone
  ) {
    return isUnavailable(
      startAt,
      endAt,
      ZoneId.of(timezone)
    );
  }

  public boolean isUnavailable(
    Instant startAt,
    Instant endAt,
    ZoneId timezone
  ) {
    return slotRepository
      .findAllInRange(
        LocalDate.ofInstant(startAt, timezone),
        LocalDate.ofInstant(endAt, timezone)
      )
      .map(slot -> new SlotDto(slot, timezone))
      .anyMatch(slot ->
        slot.getStartAt().compareTo(startAt) >= 0 &&
        slot.getEndAt().compareTo(endAt) <= 0
      );
  }

  public SlotDto getById(
    Long id,
    String timezone
  ) throws SlotNotFoundException {
    final var slot = slotRepository
      .findById(id)
      .orElseThrow(SlotNotFoundException::new);
    return new SlotDto(slot, timezone);
  }

  public Page<SlotDto> getAll(
    Pageable pageable,
    String timezone,
    LocalDate startDate,
    LocalDate endDate
  ) {
    return slotRepository.findAllInRange(startDate, endDate, timezone, pageable);
  }

  @Transactional
  public SlotDto save(Slot value) throws SlotUnavailableException, GoogleCalendarClientException {
    if (isUnavailable(value.getStartAt(), value.getEndAt(), value.getTimezone())) {
      throw new SlotUnavailableException();
    }

    final var id = googleCalendarService.add(value);
    value.setGoogleCalendarId(id);
    final var slot = slotRepository.save(value);
    return new SlotDto(slot, slot.getTimezone());
  }

  @Transactional
  public void editById(
    Long id,
    String timezone,
    UserDetails userDetails,
    AppointmentPayload payload
  ) throws GoogleCalendarClientException,
    SlotUnavailableException,
    SlotNotFoundException,
    SlotOwnerException
  {
    final var slot = slotRepository
      .findById(id)
      .orElseThrow(SlotNotFoundException::new);

    if (isNotSlotOwner(slot, userDetails)) {
      throw new SlotOwnerException();
    }

    slot.setTimezone(timezone);
    mergePayload(slot, payload);

    if (isUnavailable(slot.getStartAt(), slot.getEndAt(), timezone)) {
      throw new SlotUnavailableException();
    }

    final var appointment = appointmentRepository.save(
      createAppointmentFromSlot(slot)
    );

    slotRepository.delete(slot);
    googleCalendarService.delete(slot);

    mailer.notifyAdminsForRequest(appointment);
  }

  @Transactional
  public SlotDto deleteById(
    Long id,
    String timezone,
    UserDetails userDetails
  ) throws GoogleCalendarClientException, SlotNotFoundException, SlotOwnerException {
    final var slot = slotRepository
      .findById(id)
      .orElseThrow(SlotNotFoundException::new);

    if (isNotSlotOwner(slot, userDetails)) {
      throw new SlotOwnerException();
    }

    slotRepository.delete(slot);
    googleCalendarService.delete(slot);
    mailer.notifyAdminsForCancellation(slot);
    return new SlotDto(slot, timezone);
  }
}
