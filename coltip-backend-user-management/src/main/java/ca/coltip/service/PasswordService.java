package ca.coltip.service;

import ca.coltip.data.dto.UserDto;
import ca.coltip.data.entity.ForgotPasswordRequest;
import ca.coltip.data.entity.RecordStatus;
import ca.coltip.data.request.ForgetPasswordPayload;
import ca.coltip.data.request.PasswordChangePayload;
import ca.coltip.data.request.ResetPasswordPayload;
import ca.coltip.exception.InvalidOtpCodeException;
import ca.coltip.exception.PasswordMismatchException;
import ca.coltip.exception.UserNotFoundException;
import ca.coltip.repository.ForgotPasswordRepository;
import ca.coltip.repository.UserRepository;
import ca.coltip.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasswordService {
  private final OtpUtil otpUtil;
  private final UserMailerService mailer;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final ForgotPasswordRepository forgotPasswordRepository;

  public UserDto change(
    UserDetails userDetails,
    PasswordChangePayload payload
  ) throws PasswordMismatchException, UserNotFoundException {
    if (!passwordEncoder.matches(payload.getOldPassword(), userDetails.getPassword())) {
      throw new PasswordMismatchException();
    }

    final var user = userRepository
      .findTopByEmailAndRecordStatus(
        userDetails.getUsername(),
        RecordStatus.AVAILABLE
      )
      .orElseThrow(UserNotFoundException::new);

    user.setPassword(passwordEncoder.encode(payload.getNewPassword()));
    return userRepository.save(user).toDto();
  }

  public void requestChange(ForgetPasswordPayload payload) throws UserNotFoundException, MessagingException {
    final var email = payload.getEmail();

    final var user = userRepository
      .findTopByEmailAndRecordStatus(email, RecordStatus.AVAILABLE)
      .orElseThrow(UserNotFoundException::new);

    var forgotPass = forgotPasswordRepository
      .findTopByEmailOrderByCreatedAtDesc(email)
      .orElseGet(ForgotPasswordRequest::new);

    forgotPass.setEmail(email);
    forgotPass.setExpiration(otpUtil.expiration());

    final var code = otpUtil.generate();
    forgotPass.setVerificationCode(code);

    forgotPasswordRepository.save(forgotPass);

    mailer.sendOtpVerification(user, code);
  }

  public void reset(ResetPasswordPayload payload)
    throws InvalidOtpCodeException, UserNotFoundException
  {
    final var email = payload.getEmail();

    final var forgotPass = forgotPasswordRepository
      .findTopByEmailOrderByCreatedAtDesc(email)
      .orElseThrow(UserNotFoundException::new);

    if (!payload.getVerificationCode().equals(forgotPass.getVerificationCode())) {
      throw new InvalidOtpCodeException();
    }

    final var user = userRepository
      .findTopByEmailAndRecordStatus(email, RecordStatus.AVAILABLE)
      .orElseThrow(UserNotFoundException::new);

    user.setPassword(passwordEncoder.encode(payload.getNewPassword()));
    userRepository.save(user);
    forgotPasswordRepository.delete(forgotPass);
  }
}
