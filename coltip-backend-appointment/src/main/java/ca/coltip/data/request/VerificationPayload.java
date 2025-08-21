package ca.coltip.data.request;

import ca.coltip.data.dto.ValidationStatus;

public class VerificationPayload {
  private Integer appointmentId;
  private ValidationStatus status;

  public Integer getAppointmentId() {
    return appointmentId;
  }

  public ValidationStatus getStatus() {
    return status;
  }
}
