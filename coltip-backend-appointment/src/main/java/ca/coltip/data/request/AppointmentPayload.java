package ca.coltip.data.request;

import java.time.ZonedDateTime;

public class AppointmentPayload {
  private String title;
  private String description;
  private ZonedDateTime startAt;
  private ZonedDateTime endAt;

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public ZonedDateTime getStartAt() {
    return startAt;
  }

  public ZonedDateTime getEndAt() {
    return endAt;
  }
}
