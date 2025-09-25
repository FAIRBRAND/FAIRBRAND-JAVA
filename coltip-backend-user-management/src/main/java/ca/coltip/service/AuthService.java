package ca.coltip.service;

import ca.coltip.data.dto.UserDto;
import ca.coltip.data.request.LoginPayload;
import ca.coltip.data.request.RefreshTokenPayload;
import ca.coltip.data.request.SignupConfirmationPayload;
import ca.coltip.data.request.SignupPayload;
import ca.coltip.data.response.LoginResponse;
import ca.coltip.exception.*;
import ca.coltip.util.CustomUserDetails;
import ca.coltip.util.JwtUtil;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final RefreshTokenService refreshTokenService;
  private final UserService userService;

  private Authentication authenticate(String email, String password) throws AuthenticationException {
    return authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        email,
        password
      )
    );
  }

  public LoginResponse login(LoginPayload payload) throws AuthenticationException {
    var auth = authenticate(
      payload.getEmail(),
      payload.getPassword()
    );

    final var user = ((CustomUserDetails) auth.getPrincipal()).user();
    final var refreshToken = refreshTokenService.createRefreshToken(user);

    return new LoginResponse(
      new UserDto(user),
      jwtUtil.generateToken(user),
      refreshToken.getToken()
    );
  }

  public LoginResponse refreshToken(RefreshTokenPayload payload) throws OtpCodeExpirationException, TokenNotFoundException {
    final var refreshToken = refreshTokenService.getByToken(payload.getRefreshToken());

    if (refreshToken.isExpired()) {
      refreshTokenService.delete(refreshToken);
      throw new OtpCodeExpirationException();
    }

    final var user = refreshToken.getUser();

    return new LoginResponse(
      new UserDto(user),
      jwtUtil.generateToken(user),
      refreshToken.getToken()
    );
  }

  public void signup(SignupPayload payload) throws UserConflictException, MessagingException {
    userService.createPending(payload);
  }

  public LoginResponse confirmSignup(SignupConfirmationPayload payload)
    throws InvalidOtpCodeException, OtpCodeExpirationException, MessagingException, UserNotFoundException
  {
    final var user = userService.confirmCreation(payload);
    final var refreshToken = refreshTokenService.createRefreshToken(user);
    return new LoginResponse(
      new UserDto(user),
      jwtUtil.generateToken(user),
      refreshToken.getToken()
    );
  }
}
