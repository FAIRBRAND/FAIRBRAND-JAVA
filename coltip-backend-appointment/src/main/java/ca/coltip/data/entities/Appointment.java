package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.ZonedDateTime;

@Table
@Entity
public class Appointment {
  @Id
  @Column(name="id_appointment")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "google_calendar_id", nullable = false)
  private String googleCalendarId;

  @Column(name = "start_at", nullable = false)
  private ZonedDateTime startAt;

  @Column(name = "end_at", nullable = false)
  private ZonedDateTime endAt;

  @Column(nullable = false)
  private AppointmentStatus status;

  @ManyToOne
  @JoinColumn(name="id_user")
  private User user;

  @Column
  private String title;

  @Column
  private String description;

  @Column
  @CreationTimestamp
  private Instant createdAt;

  public void setUser(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public int getId() {
    return id;
  }

  public String getGoogleCalendarId() {
    return googleCalendarId;
  }

  public void setGoogleCalendarId(String googleCalendarId) {
    this.googleCalendarId = googleCalendarId;
  }

  public void setId(int id) {
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
