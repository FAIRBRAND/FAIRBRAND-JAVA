package ca.coltip.service.impl;

import ca.coltip.data.dto.*;
import ca.coltip.data.requests.AuthenticationRequest;
import ca.coltip.data.requests.RefreshTokenRequest;
import ca.coltip.data.responses.AuthenticationResponse;
import ca.coltip.data.responses.RefreshTokenResponse;
import ca.coltip.exceptions.BadCredentialsException;
import ca.coltip.exceptions.TokenException;
import ca.coltip.security.CustomUserDetails;
import ca.coltip.service.AuthenticationService;
import ca.coltip.service.RefreshTokenService;
import ca.coltip.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RefreshTokenService refreshTokenService, UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.userDetailsService = userDetailsService;
    }
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getEmail(), authenticationRequestDTO.getPassword()));
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            RefreshTokenDTO refreshToken = refreshTokenService.createRefreshToken(userDetails.user());
            return new AuthenticationResponse(token, refreshToken.getToken());
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequestDTO) throws Exception {
        String refreshToken = refreshTokenRequestDTO.getToken();
        RefreshTokenDTO refreshTokenDTO = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new TokenException("Refresh token not found. Please log in again"));

        if (refreshTokenService.isRefreshTokenExpired(refreshTokenDTO)) {
            throw new TokenException("Invalid or expired refresh token. Please log in again");
        }

        String username = refreshTokenDTO.getUser().getEmail();
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.generateToken(userDetails);

        return new RefreshTokenResponse(newAccessToken);
    }
}
