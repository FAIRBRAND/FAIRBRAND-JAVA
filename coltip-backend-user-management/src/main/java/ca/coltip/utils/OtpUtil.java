package ca.coltip.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpUtil {
    @Value("${sign.up.otp.length}")
    private int otpLength;

    public String generateOtp() {
        int min = (int) Math.pow(10, otpLength - 1);
        int max = (int) Math.pow(10, otpLength) - 1;

        Random random = new Random();
        return String.valueOf(random.nextInt(max - min + 1) + min);
    }
}
