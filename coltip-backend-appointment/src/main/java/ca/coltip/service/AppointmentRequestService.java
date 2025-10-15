package ca.coltip.service;

import ca.coltip.data.dto.AppointmentRequestDto;
import ca.coltip.data.entity.*;
import ca.coltip.data.request.AppointmentPayload;
import ca.coltip.data.request.AppointmentValidationRequest;
import ca.coltip.data.request.ValidationStatus;
import ca.coltip.exception.AppointmentNotFoundException;
import ca.coltip.exception.GoogleCalendarClientException;
import ca.coltip.exception.SlotUnavailableException;
import ca.coltip.exception.UserNotFoundException;
import ca.coltip.repository.AppointmentRequestRepository;
import ca.coltip.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Service
@AllArgsConstructor
public class AppointmentRequestService {
  private static AppointmentRequest create(
    AppointmentPayload payload,
    User user
  ) {
    final var appointmentRequest = new AppointmentRequest();
    appointmentRequest.setUser(user);
    appointmentRequest.setTitle(payload.getTitle());
    appointmentRequest.setDescription(payload.getDescription());
    appointmentRequest.setStartAt(payload.getStartAt());
    appointmentRequest.setEndAt(payload.getEndAt());
    appointmentRequest.setStatus(AppointmentStatus.PENDING);
    return appointmentRequest;
  }

  private static Slot createSlotFromAppointment(AppointmentRequest appointment) {
    final var slot = new Slot();
    slot.setUser(appointment.getUser());
    slot.setTitle(appointment.getTitle());
    slot.setDescription(appointment.getDescription());
    slot.setStartAt(appointment.getStartAt());
    slot.setEndAt(appointment.getEndAt());
    return slot;
  }

  private final AppointmentMailer mailer;
  private final SlotService slotService;
  private final UserRepository userRepository;
  private final AppointmentRequestRepository appointmentRepository;

  private User getUser(UserDetails userDetails) throws UserNotFoundException {
    return userRepository
      .findTopByEmailAndRecordStatus(
        userDetails.getUsername(),
        RecordStatus.AVAILABLE
      )
      .orElseThrow(UserNotFoundException::new);
  }

  @Transactional
  public void create(
    UserDetails userDetails,
    AppointmentPayload payload
  ) throws SlotUnavailableException, UserNotFoundException {
    if (slotService.isUnavailable(payload.getStartAt(), payload.getEndAt())) {
      throw new SlotUnavailableException();
    }

    final var appointmentRequest = create(payload, getUser(userDetails));
    final var appointment = appointmentRepository.save(appointmentRequest);
    mailer.notifyAdminsForRequest(appointment);
  }

  @Transactional
  public AppointmentRequestDto validate(
    Long id,
    AppointmentValidationRequest payload
  ) throws GoogleCalendarClientException, SlotUnavailableException, AppointmentNotFoundException {
    final var appointment = appointmentRepository
      .findById(id)
      .orElseThrow(AppointmentNotFoundException::new);

    final var validationStatus = payload.getValidationStatus();
    if (validationStatus == ValidationStatus.REJECT) {
      appointment.setStatus(AppointmentStatus.REJECTED);
      appointmentRepository.save(appointment);
    } else if (validationStatus == ValidationStatus.CONFIRM) {
      slotService.save(createSlotFromAppointment(appointment));
      appointmentRepository.delete(appointment);
    }

    mailer.notifyClientForValidation(appointment, validationStatus);

    return new AppointmentRequestDto(appointment);
  }

  public AppointmentRequestDto getById(Long id) throws AppointmentNotFoundException {
    final var appointment = appointmentRepository
      .findById(id)
      .orElseThrow(AppointmentNotFoundException::new);
    return new AppointmentRequestDto(appointment);
  }

  public Page<AppointmentRequestDto> getAll(
    @NonNull Pageable pageable,
    @NonNull LocalDate startDate,
    @NonNull LocalDate endDate,
    @Nullable AppointmentStatus status
  ) {
    final var start = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
    final var end = endDate.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

    if (status == null) {
      return appointmentRepository.findAllInRange(start, end, pageable);
    }

    return appointmentRepository.findAllInRangeAndStatus(
      start, end,
      status,
      pageable
    );
  }
}
