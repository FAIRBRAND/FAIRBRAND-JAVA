package ca.coltip.controller;

import ca.coltip.data.dto.SlotDto;
import ca.coltip.data.request.AppointmentPayload;
import ca.coltip.data.response.ApiResponse;
import ca.coltip.exception.*;
import ca.coltip.service.AppointmentTranslate;
import ca.coltip.service.SlotService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("slot")
public class SlotController {
  private final SlotService service;
  private final AppointmentTranslate translate;

  @GetMapping("{id}")
  public ApiResponse<SlotDto> getById(
    @PathVariable Long id,
    @RequestHeader(name = "Accept-Language", required = false) Locale locale
  ) {
    try {
      return ApiResponse.ok(service.getById(id));
    } catch (SlotNotFoundException e) {
      throw new NotFoundException(translate.resourceNotFound(locale));
    }
  }

  @GetMapping
  public ApiResponse<Page<SlotDto>> getAll(
    @RequestParam(required = false, defaultValue = "1") Integer page,
    @RequestParam(required = false, defaultValue = "10") Integer size,
    @RequestParam(name = "start_date") LocalDate startDate,
    @RequestParam(name = "end_date") LocalDate endDate
  ) {
    final var pageable = Pageable.ofSize(size).withPage(page);
    return ApiResponse.ok(
      service.getAll(
        pageable,
        startDate,
        endDate
      )
    );
  }

  @PutMapping("{id}")
  public ApiResponse<String> modifyById(
    @PathVariable Long id,
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @AuthenticationPrincipal UserDetails userDetails,
    @RequestBody AppointmentPayload payload
  ) {
    try {
      service.editById(
        id,
        Objects.requireNonNull(userDetails),
        payload
      );

      return ApiResponse.ok(translate.modificationPending(locale));
    } catch (GoogleCalendarClientException e) {
      throw new InternalServerError(
        translate.googleCalendarError(locale),
        e
      );
    } catch (SlotUnavailableException e) {
      throw new BadRequestException(translate.slotUnavailable(locale));
    } catch (SlotNotFoundException e) {
      throw new NotFoundException(translate.resourceNotFound(locale));
    } catch (SlotOwnerException e) {
      throw new BadRequestException(translate.notTheSlotOwner(locale));
    }
  }

  @DeleteMapping("{id}")
  public ApiResponse<SlotDto> deleteById(
    @PathVariable Long id,
    @RequestHeader(name = "Accept-Language", required = false) Locale locale,
    @AuthenticationPrincipal UserDetails userDetails
  ) {
    try {
      return ApiResponse.ok(
        service.deleteById(
          id,
          Objects.requireNonNull(userDetails)
        )
      );
    } catch (GoogleCalendarClientException e) {
      throw new InternalServerError(
        translate.googleCalendarError(locale),
        e
      );
    } catch (SlotNotFoundException e) {
      throw new NotFoundException(translate.resourceNotFound(locale));
    } catch (SlotOwnerException e) {
      throw new BadRequestException(translate.notTheSlotOwner(locale));
    }
  }
}
