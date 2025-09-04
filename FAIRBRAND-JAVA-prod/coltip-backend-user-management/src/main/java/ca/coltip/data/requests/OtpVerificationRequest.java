package ca.coltip.data.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OtpVerificationRequest {
    @JsonProperty("email")
    public String email;
    @JsonProperty("otp")
    public String otp;
}
