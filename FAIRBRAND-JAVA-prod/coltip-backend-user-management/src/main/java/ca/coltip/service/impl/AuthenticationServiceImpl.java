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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
            RefreshTokenService refreshTokenService, UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequestDTO) {
        logger.debug("🔐 Starting authentication for email: {}", authenticationRequestDTO.getEmail());

        try {
            // 1. Créer le token d'authentification
            logger.debug("🔧 Creating UsernamePasswordAuthenticationToken");
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    authenticationRequestDTO.getEmail(),
                    authenticationRequestDTO.getPassword());

            // 2. Appeler l'AuthenticationManager
            logger.debug("🔄 Calling authenticationManager.authenticate()");
            Authentication authentication = authenticationManager.authenticate(authToken);
            logger.debug("✅ authenticationManager.authenticate() completed successfully");

            // 3. Récupérer les détails de l'utilisateur
            logger.debug("🔧 Getting CustomUserDetails from authentication");
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            logger.debug("✅ CustomUserDetails retrieved: {}", userDetails.getUsername());

            // 4. Générer le token JWT
            logger.debug("🔄 Generating JWT token");
            String token = jwtUtil.generateToken(userDetails);
            logger.debug("✅ JWT token generated successfully");

            // 5. Créer le refresh token
            logger.debug("🔄 Creating refresh token");
            RefreshTokenDTO refreshToken = refreshTokenService.createRefreshToken(userDetails.user());
            logger.debug("✅ Refresh token created successfully");

            logger.debug("✅ Authentication completed successfully for email: {}", authenticationRequestDTO.getEmail());
            return new AuthenticationResponse(token, refreshToken.getToken());

        } catch (AuthenticationException e) {
            logger.error("❌ AuthenticationException for email: {} - Error: {}", authenticationRequestDTO.getEmail(),
                    e.getMessage());
            logger.error("❌ AuthenticationException stack trace: ", e);
            throw new BadCredentialsException("Invalid email or password");
        } catch (Exception e) {
            logger.error("💥 Unexpected exception for email: {} - Error: {}", authenticationRequestDTO.getEmail(),
                    e.getMessage());
            logger.error("💥 Exception type: {}", e.getClass().getSimpleName());
            logger.error("💥 Stack trace: ", e);
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
