package ca.coltip.data.dto;

import ca.coltip.data.entities.Appointment;

import java.time.Instant;
import java.time.ZonedDateTime;

public class AppointmentInValidation {
  public static AppointmentInValidation from(Appointment appointment) {
    return new AppointmentInValidation(
      appointment.getId(),
      appointment.getTitle(),
      appointment.getDescription(),
      appointment.getStartAt(),
      appointment.getEndAt(),
      appointment.getCreatedAt()
    );
  }

  private int id;
  private String title;
  private String description;
  private ZonedDateTime startAt;
  private ZonedDateTime endAt;
  private Instant createdAt;

  protected AppointmentInValidation(
    int id,
    String title,
    String description,
    ZonedDateTime startAt,
    ZonedDateTime endAt,
    Instant createdAt
  ) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.startAt = startAt;
    this.endAt = endAt;
    this.createdAt = createdAt;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ZonedDateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(ZonedDateTime startAt) {
    this.startAt = startAt;
  }

  public ZonedDateTime getEndAt() {
    return endAt;
  }

  public void setEndAt(ZonedDateTime endAt) {
    this.endAt = endAt;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
