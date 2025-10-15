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
import java.time.ZoneOffset;
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
    Instant endAt
  ) {
    return slotRepository
      .findAllInRange(startAt, endAt)
      .anyMatch(slot ->
        slot.getStartAt().compareTo(startAt) >= 0 &&
        slot.getEndAt().compareTo(endAt) <= 0
      );
  }

  public SlotDto getById(Long id) throws SlotNotFoundException {
    final var slot = slotRepository
      .findById(id)
      .orElseThrow(SlotNotFoundException::new);
    return new SlotDto(slot);
  }

  public Page<SlotDto> getAll(
    Pageable pageable,
    LocalDate startDate,
    LocalDate endDate
  ) {
    final var start = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
    final var end = endDate.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
    return slotRepository.findAllInRange(start, end, pageable);
  }

  @Transactional
  public SlotDto save(Slot value) throws SlotUnavailableException, GoogleCalendarClientException {
    if (isUnavailable(value.getStartAt(), value.getEndAt())) {
      throw new SlotUnavailableException();
    }

    final var id = googleCalendarService.add(value);
    value.setGoogleCalendarId(id);
    final var slot = slotRepository.save(value);
    return new SlotDto(slot);
  }

  @Transactional
  public void editById(
    Long id,
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

    mergePayload(slot, payload);

    if (isUnavailable(slot.getStartAt(), slot.getEndAt())) {
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
    return new SlotDto(slot);
  }
}
