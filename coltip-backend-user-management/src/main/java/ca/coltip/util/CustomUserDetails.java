package ca.coltip.util;

import ca.coltip.data.entity.RecordStatus;
import ca.coltip.data.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public record CustomUserDetails(User user) implements UserDetails {
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getSubGroups()
      .stream()
      .flatMap(subgroup -> subgroup.getAbilities().stream())
      .map(ability -> new SimpleGrantedAuthority("ROLE_" + ability.getName().toUpperCase()))
      .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.getRecordStatus() == RecordStatus.AVAILABLE;
  }
}
