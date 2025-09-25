package ca.coltip.component;

import ca.coltip.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CreateAdminRunner implements ApplicationRunner {
  private static final Logger Log = LoggerFactory.getLogger(CreateAdminRunner.class);

  private final Environment env;
  private final UserService userService;

  @Override
  public void run(ApplicationArguments args) {
    final var email = env.getRequiredProperty("coltip.admin.mail");
    final var password = UUID.randomUUID().toString();

    final var created = userService.createAdmin(email, password);
    if (created) {
      Log.info("Admin has been created");
      Log.info(
        "\nYour password is: {}\nNB: This password is only available once.\nNo other password will be sent.\nOnce you're connected, change your password!",
        password
      );
    }
  }
}
