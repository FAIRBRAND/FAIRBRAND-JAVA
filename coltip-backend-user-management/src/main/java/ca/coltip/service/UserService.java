package ca.coltip.service;

import ca.coltip.data.dto.UserDto;
import ca.coltip.data.entity.PendingUserRegistration;
import ca.coltip.data.entity.RecordStatus;
import ca.coltip.data.entity.SubGroup;
import ca.coltip.data.entity.User;
import ca.coltip.data.request.SignupConfirmationPayload;
import ca.coltip.data.request.SignupPayload;
import ca.coltip.data.request.UserUpdatePayload;
import ca.coltip.exception.*;
import ca.coltip.repository.*;
import ca.coltip.util.OtpUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@AllArgsConstructor
@Service
public class UserService {
  private static <T> void updateMap(
    Supplier<T> nullable,
    Consumer<T> setter
  ) {
    Optional.ofNullable(nullable.get()).ifPresent(setter);
  }

  private static <R> void updateMap(
    Supplier<Long> zeroable,
    Function<Long, Optional<R>> mapper,
    Consumer<R> setter,
    String field
  ) throws FieldUpdateNotFound {
    final var value = zeroable.get();
    if (value == null) return;

    var mappedValue = mapper.apply(value);
    if (mappedValue.isPresent()) {
      setter.accept(mappedValue.get());
    } else {
      throw new FieldUpdateNotFound(field);
    }
  }

  private final OtpUtil otpUtil;
  private final PasswordEncoder passwordEncoder;
  private final UserMailerService mailer;
  private final LanguageService languageService;

  private final DomainRepository domainRepository;
  private final CountryRepository countryRepository;
  private final PendingUserRepository pendingUserRepository;
  private final UserRepository userRepository;
  private final SubgroupRepository subgroupRepository;

  public User whoami(UserDetails userDetails) throws UserNotFoundException {
    return userRepository
      .findTopByEmailAndRecordStatus(
        userDetails.getUsername(),
        RecordStatus.AVAILABLE
      )
      .orElseThrow(UserNotFoundException::new);
  }

  public boolean createAdmin(String email, String password) {
    if (userRepository.existsByEmail(email)) return false;

    final var user = new User();
    user.setFirstname("Admin");
    user.setLastname("");
    user.setEmail(email);
    user.setRecordStatus(RecordStatus.AVAILABLE);
    user.setPassword(passwordEncoder.encode(password));
    user.setLanguage(languageService.getByLocale(Locale.ENGLISH));
    user.setSubGroups(Set.of(
      subgroupRepository
        .findFirstByName(SubGroup.DEFAULT_ADMIN)
        .orElseThrow(NullPointerException::new)
    ));
    userRepository.save(user);
    return true;
  }

  public void createPending(SignupPayload payload) throws UserConflictException, MessagingException {
    final var email = payload.getEmail();

    if (userRepository.existsByEmail(email)) {
      throw new UserConflictException();
    }

    var pendingUser = pendingUserRepository
      .findTopByEmailOrderByExpiresAtDesc(email)
      .orElseGet(() -> {
        final var pu = new PendingUserRegistration();
        pu.setFirstname(payload.getFirstname());
        pu.setLastname(payload.getLastname());
        pu.setEmail(email);
        pu.setPassword(passwordEncoder.encode(payload.getPassword()));

        final var language = languageService.getById(payload.getLanguageId());

        pu.setLanguage(language);
        return pu;
      });

    pendingUser.setOtp(otpUtil.generate());
    pendingUser.setExpiresAt(otpUtil.expiration());

    pendingUser = pendingUserRepository.save(pendingUser);
    mailer.sendOtpVerification(pendingUser);
  }

  @Transactional
  public User confirmCreation(SignupConfirmationPayload payload)
    throws UserNotFoundException, OtpCodeExpirationException, InvalidOtpCodeException, MessagingException
  {
    final var pendingUser = pendingUserRepository
      .findTopByEmailOrderByExpiresAtDesc(payload.getEmail())
      .orElseThrow(UserNotFoundException::new);

    if (pendingUser.isExpired()) {
      throw new OtpCodeExpirationException();
    }

    if (!pendingUser.getOtp().equals(payload.getOtpCode())) {
      throw new InvalidOtpCodeException();
    }

    var user = new User();
    user.setFirstname(pendingUser.getFirstname());
    user.setLastname(pendingUser.getLastname());
    user.setEmail(pendingUser.getEmail());
    user.setPassword(pendingUser.getPassword());
    user.setCreatedAt(pendingUser.getCreatedAt());
    user.setLanguage(pendingUser.getLanguage());
    user.setRecordStatus(RecordStatus.AVAILABLE);
    user.setSubGroups(Set.of(
      subgroupRepository
        .findFirstByName(SubGroup.DEFAULT_USER)
        .orElseThrow(NullPointerException::new)
    ));

    user =  userRepository.save(user);
    pendingUserRepository.deleteAllByEmail(payload.getEmail());
    mailer.sendWelcome(user);
    return user;
  }

  public UserDto updateProfile(UserDetails userDetails, UserUpdatePayload payload) throws UserNotFoundException, FieldUpdateNotFound {
    final var user = whoami(userDetails);

    user.setDescription(payload.getDescription());
    updateMap(payload::getFirstname, user::setFirstname);
    updateMap(payload::getLastname, user::setLastname);
    updateMap(payload::getPhoneNumber, user::setPhoneNumber);
    updateMap(payload::getPhoneNumber, user::setPhoneNumber);
    updateMap(payload::getDomainId, domainRepository::findById, user::setDomain, "domain");
    updateMap(payload::getLanguageId, languageService::findById, user::setLanguage, "language");
    updateMap(payload::getCountryId, countryRepository::findById, user::setCountry, "country");

    return userRepository.save(user).toDto();
  }

  public UserDto disableProfile(UserDetails userDetails) throws UserNotFoundException {
    final var user = whoami(userDetails);
    user.setRecordStatus(RecordStatus.DELETED);
    return userRepository.save(user).toDto();
  }

  public double getUserMonthlyGrowthPercentage() {
    final var now = LocalDate.now();
    final int currentMonth = now.getMonthValue();
    final int currentYear = now.getYear();

    final var previous = now.minusMonths(1);
    final int previousMonth = previous.getMonthValue();
    final int previousYear = previous.getYear();

    final int status = RecordStatus.AVAILABLE.ordinal();

    final long currentCount = userRepository.countUsersByMonthAndYearAndStatus(currentMonth, currentYear, status);
    final long previousCount = userRepository.countUsersByMonthAndYearAndStatus(previousMonth, previousYear, status);

    if (previousCount == 0) {
      return 100.0;
    }

    return ((currentCount - previousCount) * 100.0) / previousCount;
  }

  public long getTotalUserActive() {
    return userRepository.countUserByRecordStatus(RecordStatus.AVAILABLE);
  }
}
