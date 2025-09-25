package ca.coltip.data.dto;

import ca.coltip.data.entity.Slot;
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
public class SlotDto {
  private Long id;
  private Instant startAt;
  private Instant endAt;
  private UserDto user;
  private String title;
  private String description;

  public SlotDto(Slot slot, String timezone) {
    this(slot, ZoneId.of(timezone));
  }

  public SlotDto(Slot slot, ZoneId timezone) {
    this.id = slot.getId();
    this.user = new UserDto(slot.getUser());
    this.title = slot.getTitle();
    this.description = slot.getDescription();

    final var originZoneId = ZoneId.of(slot.getTimezone());

    this.startAt = DateUtil.translate(
      slot.getStartAt(),
      originZoneId,
      timezone
    );

    this.endAt = DateUtil.translate(
      slot.getEndAt(),
      originZoneId,
      timezone
    );
  }
}
