package ca.coltip;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@OpenAPIDefinition(
  info = @Info(
    title = "Fairbrand API",
    version = "1.0.0"
  )
)
@ComponentScan(basePackages = {"ca"})
@EntityScan(basePackages = {"ca"})
@EnableJpaRepositories(basePackages = {"ca"})
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@SpringBootApplication
public class ColtipApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(ColtipApplication.class);
  }

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ColtipApplication.class);
	}
}
