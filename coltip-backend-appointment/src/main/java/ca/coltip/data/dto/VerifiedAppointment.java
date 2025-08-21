package ca.coltip.data.dto;

import ca.coltip.data.entities.Appointment;

import java.time.Instant;
import java.time.ZonedDateTime;

public class VerifiedAppointment extends AppointmentInValidation {
  public static VerifiedAppointment from(Appointment appointment) {
    final var status = switch (appointment.getStatus()) {
      case BOOKED -> ValidationStatus.VALIDATED;
      case CANCELED -> ValidationStatus.REJECTED;
      case VERIFY -> throw new IllegalArgumentException("Should verify appointment");
    };
    return new VerifiedAppointment(
      appointment.getId(),
      appointment.getTitle(),
      appointment.getDescription(),
      status,
      appointment.getStartAt(),
      appointment.getEndAt(),
      appointment.getCreatedAt()
    );
  }

  private ValidationStatus validationStatus;

  protected VerifiedAppointment(
    int id,
    String title,
    String description,
    ValidationStatus validationStatus,
    ZonedDateTime startAt,
    ZonedDateTime endAt,
    Instant createdAt
  ) {
    super(id, title, description, startAt, endAt, createdAt);
    this.validationStatus = validationStatus;
  }

  public void setValidationStatus(ValidationStatus validationStatus) {
    this.validationStatus = validationStatus;
  }

  public ValidationStatus getValidationStatus() {
    return validationStatus;
  }
}
