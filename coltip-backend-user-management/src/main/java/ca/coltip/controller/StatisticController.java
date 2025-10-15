package ca.coltip.controller;

import ca.coltip.data.response.ApiResponse;
import ca.coltip.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("users/statistic")
public class StatisticController {
  private final UserService userService;

  @GetMapping("growth/percent")
  public ApiResponse<Double> getUserMonthGrowthPercentage() {
    return ApiResponse.ok(userService.getUserMonthlyGrowthPercentage());
  }

  @GetMapping("active")
  public ApiResponse<Long> getTotalActiveUsers() {
    return ApiResponse.ok(userService.getTotalUserActive());
  }
}
