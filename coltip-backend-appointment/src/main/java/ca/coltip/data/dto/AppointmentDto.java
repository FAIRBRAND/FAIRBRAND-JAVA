package ca.coltip.data.dto;

import ca.coltip.data.entities.Appointment;
import ca.coltip.data.entities.AppointmentStatus;
import jakarta.persistence.Embeddable;

import java.time.Instant;
import java.time.ZonedDateTime;

@Embeddable
public class AppointmentDto {
  public static AppointmentDto from(Appointment appointment) {
    return new AppointmentDto(appointment);
  }

  private Integer id;
  private ZonedDateTime startAt;
  private ZonedDateTime endAt;
  private AppointmentStatus status;
  private Integer userId;
  private String title;
  private String description;
  private Instant createdAt;

  public AppointmentDto(Appointment appointment) {
    this.id = appointment.getId();
    this.title = appointment.getTitle();
    this.description = appointment.getDescription();
    this.startAt = appointment.getStartAt();
    this.endAt = appointment.getEndAt();
    this.userId = appointment.getUser().getId();
    this.status = appointment.getStatus();
    this.createdAt = appointment.getCreatedAt();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public AppointmentStatus getStatus() {
    return status;
  }

  public void setStatus(AppointmentStatus status) {
    this.status = status;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
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

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
