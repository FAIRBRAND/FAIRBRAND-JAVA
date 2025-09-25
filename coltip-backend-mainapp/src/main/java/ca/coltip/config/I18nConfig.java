package ca.coltip.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class I18nConfig {
  @Bean
  public MessageSource messageSource() {
    final var messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setCacheSeconds(900); // 15 minutes of cache
    messageSource.setUseCodeAsDefaultMessage(true);
    messageSource.setBasename("classpath:messages/message");
    return messageSource;
  }
}
