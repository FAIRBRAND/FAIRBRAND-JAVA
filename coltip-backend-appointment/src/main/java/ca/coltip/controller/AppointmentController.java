package ca.coltip.controller;

import ca.coltip.data.dto.AppointmentRequestDto;
import ca.coltip.data.entity.AppointmentStatus;
import ca.coltip.data.request.AppointmentPayload;
import ca.coltip.data.request.AppointmentValidationRequest;
import ca.coltip.data.response.ApiResponse;
import ca.coltip.exception.*;
import ca.coltip.service.AppointmentRequestService;
import ca.coltip.service.AppointmentTranslate;
import ca.coltip.service.UserTranslationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Locale;

@RestController
@AllArgsConstructor
@RequestMapping("appointment_request")
public class AppointmentController {
  private final AppointmentRequestService service;
  private final AppointmentTranslate appointmentTranslate;
  private final UserTranslationService userTranslate;

  @PostMapping
  public ApiResponse<String> create(
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @AuthenticationPrincipal UserDetails userDetails,
    @RequestBody AppointmentPayload payload
  ) {
    try {
      service.create(userDetails, payload);
      return ApiResponse.ok(appointmentTranslate.appointmentRequestSent(locale));
    } catch (SlotUnavailableException e) {
      throw new BadRequestException(appointmentTranslate.slotUnavailable(locale));
    } catch (UserNotFoundException e) {
      throw new BadRequestException(userTranslate.userNotFound(locale));
    }
  }

  @PostMapping("validate/{id}")
  public ApiResponse<AppointmentRequestDto> validateAppointmentRequest(
    @PathVariable Long id,
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @RequestBody AppointmentValidationRequest payload
  ) {
    try {
      return ApiResponse.ok(service.validate(id, payload));
    } catch (GoogleCalendarClientException e) {
      throw new InternalServerError(
        appointmentTranslate.googleCalendarError(locale),
        e
      );
    } catch (SlotUnavailableException e) {
      throw new BadRequestException(appointmentTranslate.slotUnavailable(locale));
    } catch (AppointmentNotFoundException e) {
      throw new NotFoundException(appointmentTranslate.resourceNotFound(locale));
    }
  }

  @GetMapping
  public ApiResponse<Page<AppointmentRequestDto>> getAll(
    @RequestParam(required = false, defaultValue = "1") Integer page,
    @RequestParam(required = false, defaultValue = "10") Integer size,
    @RequestParam(required = false) AppointmentStatus status,
    @RequestParam(name = "start_date") LocalDate startDate,
    @RequestParam(name = "end_date") LocalDate endDate
  ) {
    final var pageable = Pageable.ofSize(size).withPage(page);
    return ApiResponse.ok(
      service.getAll(
        pageable,
        startDate,
        endDate,
        status
      )
    );
  }

  @GetMapping("{id}")
  public ApiResponse<AppointmentRequestDto> getById(
    @PathVariable Long id,
    @RequestHeader(name = "Accept-Language", required = false) Locale locale
  ) {
    try {
      return ApiResponse.ok(service.getById(id));
    } catch (AppointmentNotFoundException e) {
      throw new NotFoundException(appointmentTranslate.resourceNotFound(locale));
    }
  }
}
