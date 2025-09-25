package ca.coltip.util;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Random;

@Component
public class OtpUtil {
  private final int otpLength;
  private final long expirationMs;

  public OtpUtil(Environment environment) {
    this.expirationMs = environment.getRequiredProperty(
      "coltip.otp.expiration_ms",
      Long.class
    );
    this.otpLength = environment.getRequiredProperty(
      "coltip.otp.length",
      Integer.class
    );
  }

  public Instant expiration() {
    return Instant.now().plusMillis(expirationMs);
  }

  public String generate() {
    int min = (int) Math.pow(10, otpLength - 1);
    int max = (int) Math.pow(10, otpLength) - 1;
    return String.valueOf(new Random().nextInt(max - min + 1) + min);
  }
}
