package ca.coltip.service.impl;

import ca.coltip.data.entities.User;
import ca.coltip.data.repository.UserRepository;
import ca.coltip.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(value = "UserService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("🔍 Loading user by email: {}", email);

        try {
            logger.debug("🔄 Searching user in repository");
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

            logger.debug("✅ User found: ID={}, Name={} {}", user.getId(), user.getFirstName(), user.getSurname());
            logger.debug("🔑 User password length: {}",
                    user.getPassword() != null ? user.getPassword().length() : "null");

            logger.debug("🔧 Creating CustomUserDetails");
            CustomUserDetails customUserDetails = new CustomUserDetails(user);
            logger.debug("✅ CustomUserDetails created successfully");

            return customUserDetails;

        } catch (UsernameNotFoundException e) {
            logger.error("❌ User not found: {}", email);
            throw e;
        } catch (Exception e) {
            logger.error("💥 Error loading user by email: {} - Error: {}", email, e.getMessage());
            logger.error("💥 Exception type: {}", e.getClass().getSimpleName());
            logger.error("💥 Stack trace: ", e);
            throw new RuntimeException("Error loading user details", e);
        }
    }
}
