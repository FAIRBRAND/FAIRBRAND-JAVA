package ca.coltip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RgpdApplication {

    public static void main(String[] args) {
        SpringApplication.run(RgpdApplication.class, args);
        System.out.println("Application RGPD démarrée");
    }
}
