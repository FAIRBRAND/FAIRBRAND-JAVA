package ca.coltip.service;

import ca.coltip.exception.UserNotFoundException;
import ca.coltip.security.CustomUserDetails;
import ca.coltip.service.impl.UserDetailsServiceImpl;
import ca.coltip.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final JwtUtil jwtUtil;
  private final UserDetailsServiceImpl userDetailsServiceImpl;

  public UserService(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsServiceImpl) {
    this.jwtUtil = jwtUtil;
    this.userDetailsServiceImpl = userDetailsServiceImpl;
  }

  public int getUserId(String authorization) throws UserNotFoundException {
    if (authorization.startsWith("Bearer ")) {
      String token = authorization.substring(7);
      String username = jwtUtil.extractUsername(token);

      if (username != null) {
        CustomUserDetails user = userDetailsServiceImpl.loadUserByUsername(username);

        if (jwtUtil.validateToken(token, user)) {
          return user.user().getId();
        }
      }
    }

    throw new UserNotFoundException();
  }
}
