package ca.coltip.service.impl;

import ca.coltip.data.entities.PendingUserRegistration;
import ca.coltip.data.entities.SubGroup;
import ca.coltip.data.entities.User;
import ca.coltip.data.requests.OtpVerificationRequest;
import ca.coltip.data.requests.SignUpRequest;
import ca.coltip.exceptions.BadCredentialsException;
import ca.coltip.exceptions.OtpException;
import ca.coltip.repository.PendingUserRegistrationRepository;
import ca.coltip.repository.SubgroupRepository;
import ca.coltip.repository.UserRepository;
import ca.coltip.service.SignUpService;
import ca.coltip.strategy.EmailStrategy;
import ca.coltip.utils.OtpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class SignUpServiceImpl implements SignUpService {
    @Value("${sign.up.otp.expiration}")
    private int otpExpirationMs;

    private final OtpUtil otpUtil;
    private final EmailStrategy emailStrategy;
    private final PendingUserRegistrationRepository pendingUserRegistrationRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SubgroupRepository subgroupRepository;

    public SignUpServiceImpl(OtpUtil otpUtil, EmailStrategy emailStrategy, PendingUserRegistrationRepository pendingUserRegistrationRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, SubgroupRepository subgroupRepository) {
        this.otpUtil = otpUtil;
        this.emailStrategy = emailStrategy;
        this.pendingUserRegistrationRepository = pendingUserRegistrationRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.subgroupRepository = subgroupRepository;
    }

    @Override
    public void requestOtp(SignUpRequest signUpRequest) {
        // generate otp
        String otp = otpUtil.generateOtp();
        // save temporarily
        PendingUserRegistration pendingRegistration = new PendingUserRegistration();
        pendingRegistration.firstname = signUpRequest.firstname;
        pendingRegistration.lastname = signUpRequest.lastname;
        pendingRegistration.email = signUpRequest.email;
        pendingRegistration.password = passwordEncoder.encode(signUpRequest.password); // hash in production
        pendingRegistration.otp = otp;
        pendingRegistration.expiresAt = Instant.now().plusMillis(otpExpirationMs);
        pendingUserRegistrationRepository.save(pendingRegistration);
        // send otp by email
        String emailText = "Your otp is "+otp;
        emailStrategy.sendEmail(signUpRequest.email, "Validation", emailText);
    }

    @Override
    public void verifyOtp(OtpVerificationRequest otpVerificationRequest) {
            Optional<PendingUserRegistration> optionalPending = pendingUserRegistrationRepository.findTopByEmailOrderByExpiresAtDesc(otpVerificationRequest.email);
            if (optionalPending.isEmpty()) {
                throw new BadCredentialsException("Registration not found for email: " + otpVerificationRequest.email);
            }

            PendingUserRegistration latestPending = optionalPending.get();

            // Validate the OTP against the latest request
            if (!latestPending.otp.equals(otpVerificationRequest.otp)) {
                throw new OtpException("Invalid OTP");
            }

            // Check if OTP has expired
            if ((latestPending.expiresAt.compareTo(Instant.now()) < 0)) {
                throw new OtpException("OTP expired please request a new OTP");
            }

            // OTP is valid: create the final User record
            User newUser = new User();
            newUser.setFirstName(latestPending.firstname);
            newUser.setSurname(latestPending.lastname);
            newUser.setEmail(latestPending.email);
            newUser.setPassword(latestPending.password);
            newUser.setDateCreated(LocalDateTime.now());

            // Find subgroup by default
            Optional<SubGroup> subGroup = subgroupRepository.findBySubGroupName("simple_user");
            Set<SubGroup> subGroups = new HashSet<>();
            subGroup.ifPresent(subGroups::add);
            newUser.setListSubgroups(subGroups);

            userRepository.save(newUser);

            // Delete all pending registrations for that email
            pendingUserRegistrationRepository.deleteAllByEmail(otpVerificationRequest.email);
    }
}
