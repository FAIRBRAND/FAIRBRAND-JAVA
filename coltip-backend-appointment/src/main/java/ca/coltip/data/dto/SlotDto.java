package ca.coltip.data.dto;

import ca.coltip.data.entity.Slot;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SlotDto {
  private Long id;
  private Instant startAt;
  private Instant endAt;
  private UserDto user;
  private String title;
  private String description;

  public SlotDto(Slot slot) {
    this.id = slot.getId();
    this.user = new UserDto(slot.getUser());
    this.title = slot.getTitle();
    this.description = slot.getDescription();
    this.startAt = slot.getStartAt();
    this.endAt = slot.getEndAt();
  }
}
