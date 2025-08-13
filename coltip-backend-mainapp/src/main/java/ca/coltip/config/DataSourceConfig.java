package ca.coltip.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

//@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String mainUrl;
    @Value("${spring.datasource.username}")
    private String mainUsername;
    @Value("${spring.datasource.password}")
    private String mainPassword;

    @Value("${flyway.datasource.url}")
    private String flywayUrl;
    @Value("${flyway.datasource.username}")
    private String flywayUsername;
    @Value("${flyway.datasource.password}")
    private String flywayPassword;

    @Bean(name = "mainDataSource")
    @Primary
    public DataSource mainDataSource() {
        return DataSourceBuilder.create()
                .url(mainUrl)
                .username(mainUsername)
                .password(mainPassword)
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "flywayDataSource")
    public DataSource flywayDataSource() {
        return DataSourceBuilder.create()
                .url(flywayUrl)
                .username(flywayUsername)
                .password(flywayPassword)
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(@Qualifier("flywayDataSource") DataSource flywayDataSource) {
        return Flyway.configure()
                .dataSource(flywayDataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
    }
}