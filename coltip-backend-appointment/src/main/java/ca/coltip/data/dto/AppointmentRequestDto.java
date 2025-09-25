package ca.coltip.data.dto;

import ca.coltip.data.entity.AppointmentRequest;
import ca.coltip.data.entity.AppointmentStatus;
import ca.coltip.util.DateUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppointmentRequestDto {
  private Long id;
  private Instant startAt;
  private Instant endAt;
  private AppointmentStatus status;
  private UserDto user;
  private String title;
  private String description;

  public AppointmentRequestDto(
    AppointmentRequest appointmentRequest,
    String timezone
  ) {
    this(
      appointmentRequest,
      ZoneId.of(timezone)
    );
  }

  public AppointmentRequestDto(
    AppointmentRequest appointmentRequest,
    ZoneId timezone
  ) {
    this.id = appointmentRequest.getId();
    this.status = appointmentRequest.getStatus();
    this.user = new UserDto(appointmentRequest.getUser());
    this.title = appointmentRequest.getTitle();
    this.description = appointmentRequest.getDescription();

    final var originZoneId = ZoneId.of(appointmentRequest.getTimezone());

    this.startAt = DateUtil.translate(
      appointmentRequest.getStartAt(),
      originZoneId,
      timezone
    );

    this.endAt = DateUtil.translate(
      appointmentRequest.getEndAt(),
      originZoneId,
      timezone
    );
  }
}
