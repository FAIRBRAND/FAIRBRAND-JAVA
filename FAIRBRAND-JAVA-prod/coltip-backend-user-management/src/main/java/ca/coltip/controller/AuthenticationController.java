package ca.coltip.controller;

import ca.coltip.data.requests.AuthenticationRequest;
import ca.coltip.data.responses.AuthenticationResponse;
import ca.coltip.data.requests.RefreshTokenRequest;
import ca.coltip.data.responses.RefreshTokenResponse;
import ca.coltip.data.response.ApiResponse;
import ca.coltip.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>(HttpStatus.OK.value(), "User logged in successfully", authenticationResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequestDTO) throws Exception {
        RefreshTokenResponse refreshTokenResponse = authenticationService.refreshToken(refreshTokenRequestDTO);
        ApiResponse<RefreshTokenResponse> response = new ApiResponse<>(HttpStatus.OK.value(), "New access token generated successfully", refreshTokenResponse);
        return ResponseEntity.ok(response);
    }
}