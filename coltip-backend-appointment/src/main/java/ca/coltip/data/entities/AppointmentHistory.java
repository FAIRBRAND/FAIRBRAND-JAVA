package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.ZonedDateTime;

@Table
@Entity
public class AppointmentHistory {
  @Id
  @Column(name = "id_appointment_history")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column
  private HistoryState historyState;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_appointment")
  private Appointment appointment;

  @Column(name = "start_at", nullable = false)
  private ZonedDateTime startAt;

  @Column(name = "end_at", nullable = false)
  private ZonedDateTime endAt;

  @Column(nullable = false)
  private AppointmentStatus status;

  @Column
  private String title;

  @Column
  private String description;

  @Column
  @CreationTimestamp
  private Instant createdAt;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public HistoryState getHistoryState() {
    return historyState;
  }

  public void setHistoryState(HistoryState state) {
    this.historyState =  state;
  }

  public Appointment getAppointment() {
    return appointment;
  }

  public void setAppointment(Appointment appointment) {
    this.appointment = appointment;
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
