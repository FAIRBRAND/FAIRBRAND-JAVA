package ca.coltip;

import ca.coltip.utils.CertificateGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.vault.repository.configuration.EnableVaultRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan(basePackages = {"ca"})
@EntityScan(basePackages = {"ca"})
@EnableJpaRepositories(basePackages = {"ca"})
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class ColtipApplication extends SpringBootServletInitializer{
	private static final Logger log = LogManager.getLogger(ColtipApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(ColtipApplication.class);
		log.info("Coltip backend application started.");
    }

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ColtipApplication.class);
	}
}
