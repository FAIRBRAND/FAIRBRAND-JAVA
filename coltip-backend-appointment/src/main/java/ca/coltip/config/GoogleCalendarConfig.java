package ca.coltip.config;

import com.google.api.services.calendar.Calendar;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleCalendarConfig {
  @Bean
  public Calendar getCalendar() throws IOException, GeneralSecurityException {
    final var credentials = ServiceAccountCredentials.fromStream(new FileInputStream(
      "src/main/resources/credentials.json"
    )).createScoped(Collections.singleton("https://www.googleapis.com/auth/calendar"));

    return new Calendar.Builder(
      com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport(),
      com.google.api.client.json.gson.GsonFactory.getDefaultInstance(),
      new HttpCredentialsAdapter(credentials))
      .setApplicationName("Spring Google Calendar Integration")
      .build();
  }
}
