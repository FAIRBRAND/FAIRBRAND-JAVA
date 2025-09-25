package ca.coltip.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateUtil {
  public static Instant translate(Instant instant, String zoneOrigin, String zoneTarget) {
    return translate(instant, ZoneId.of(zoneOrigin), ZoneId.of(zoneTarget));
  }

  public static Instant translate(Instant instant, ZoneId zoneOrigin, ZoneId zoneTarget) {
    final var datetime = ZonedDateTime.ofInstant(instant, zoneOrigin);
    return datetime.withZoneSameInstant(zoneTarget).toInstant();
  }
}
