package ca.coltip.controller;

import ca.coltip.data.dto.AppointmentDto;
import ca.coltip.data.dto.VerifiedAppointment;
import ca.coltip.data.dto.AppointmentInValidation;
import ca.coltip.data.entities.AppointmentStatus;
import ca.coltip.data.request.AppointmentPayload;
import ca.coltip.data.request.VerificationPayload;
import ca.coltip.exception.*;
import ca.coltip.service.AppointmentService;
import ca.coltip.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
  private final AppointmentService service;
  private final UserService userService;

  public AppointmentController(
    AppointmentService service,
    UserService userService
  ) {
    this.service = service;
    this.userService = userService;
  }

  @PostMapping("/verify")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<VerifiedAppointment> verifyAppointmentRequest(
    @RequestBody VerificationPayload data
  ) throws CalendarEventCreationException {
    VerifiedAppointment appointment = service.verify(data);
    return ResponseEntity.ok(appointment);
  }

  @PostMapping
  public ResponseEntity<AppointmentInValidation> createAnAppointment(
    @RequestHeader(name = "Authorization") String token,
    @RequestBody AppointmentPayload data
  ) throws UserNotFoundException, UnavailableSlotException, FreeBusyException {
    AppointmentInValidation appointment = service.create(data,userService.getUserId(token));
    return ResponseEntity.ok(appointment);
  }

  @GetMapping
  public ResponseEntity<Page<AppointmentDto>> getAllAppointments(
    @RequestParam(required = false, defaultValue = "1") Integer page,
    @RequestParam(required = false, defaultValue = "10") Integer size,
    @RequestParam(required = false) AppointmentStatus status,
    @RequestParam LocalDate startDate,
    @RequestParam LocalDate endDate
  ) {
    Pageable pageable = Pageable.ofSize(size).withPage(page);
    return ResponseEntity.ok(service.getAll(pageable, status, startDate, endDate));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AppointmentDto> getAppointmentById(
    @PathVariable Integer id
  ) {
    return ResponseEntity.ok(service.getById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> editAppointmentById(
    @PathVariable Integer id,
    @RequestBody AppointmentPayload data
  ) throws UnavailableSlotException, FreeBusyException, CalendarEventDeleteException {
    AppointmentInValidation appointment = service.updateById(id, data);
    return ResponseEntity.ok(appointment);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<AppointmentDto> cancelAppointment(
    @PathVariable Integer id
  ) throws CalendarEventDeleteException {
    return ResponseEntity.ok(service.deleteById(id));
  }
}
