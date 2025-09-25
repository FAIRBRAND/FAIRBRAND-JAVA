package ca.coltip.service;

import ca.coltip.data.entity.RecordStatus;
import ca.coltip.repository.UserRepository;
import ca.coltip.util.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
      .findTopByEmailAndRecordStatus(username, RecordStatus.AVAILABLE)
      .map(CustomUserDetails::new)
      .orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
