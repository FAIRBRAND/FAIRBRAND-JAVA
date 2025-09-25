package ca.coltip.controller;

import ca.coltip.service.DummyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("health")
public class HealthController {
  private final DummyService dummyService;

  @GetMapping("ping")
  public String ping() {
    return "pong";
  }

  @GetMapping("dummy")
  public String dummy() {
    return dummyService.getLatest().toString();
  }
}
