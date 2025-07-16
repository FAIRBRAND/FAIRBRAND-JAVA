package ca.coltip.service;

import ca.coltip.data.requests.AuthenticationRequest;
import ca.coltip.data.responses.AuthenticationResponse;
import ca.coltip.data.requests.RefreshTokenRequest;
import ca.coltip.data.responses.RefreshTokenResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequestDTO);
    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequestDTO) throws Exception;
}
