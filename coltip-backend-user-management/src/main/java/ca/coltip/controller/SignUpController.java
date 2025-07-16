package ca.coltip.controller;

import ca.coltip.data.requests.OtpVerificationRequest;
import ca.coltip.data.requests.SignUpRequest;
import ca.coltip.apiData.response.ApiResponse;
import ca.coltip.service.SignUpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/signup")
public class SignUpController {
    private final SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping("/otp")
    public ResponseEntity<ApiResponse<String>> requestOtp(@RequestBody SignUpRequest request) {
        signUpService.requestOtp(request);
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "OTP sent successfully", "Check your email for the otp validation");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@RequestBody OtpVerificationRequest otpVerificationRequest) {
        signUpService.verifyOtp(otpVerificationRequest);
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "User registered successfully", null);
        return ResponseEntity.ok(response);
    }
}
