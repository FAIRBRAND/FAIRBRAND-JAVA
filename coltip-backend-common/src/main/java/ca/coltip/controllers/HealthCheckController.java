package ca.coltip.controllers;

import ca.coltip.services.IHealthCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthCheckController {

    @Autowired
    IHealthCheckService dbHealthCheckService;

    @GetMapping("/health-check")
    public String performHealthCheck(){
        boolean isHealthy = dbHealthCheckService.testHealth();
        if(isHealthy){
            return "test ok";
        }
        return "test ko";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminEndpoint() {
        return "this is admin endpoint";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String simpleUserEndpoint() {
        return "this is simple user endpoint";
    }
}
