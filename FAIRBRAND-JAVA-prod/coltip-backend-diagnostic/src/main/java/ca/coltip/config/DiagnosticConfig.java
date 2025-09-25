package ca.coltip.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"ca.coltip.service", "ca.coltip.controller"})
@EntityScan(basePackages = "ca.coltip.data.entities")
public class DiagnosticConfig {
    // Configuration for the diagnostic module
}