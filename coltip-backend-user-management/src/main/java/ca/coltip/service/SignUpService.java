package ca.coltip.service;

import ca.coltip.data.requests.OtpVerificationRequest;
import ca.coltip.data.requests.SignUpRequest;

public interface SignUpService {
    void requestOtp(SignUpRequest signUpRequest);
    void verifyOtp(OtpVerificationRequest otpVerificationRequest);
}
