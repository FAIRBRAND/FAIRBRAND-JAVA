package ca.coltip.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "mainDataSource")
    @Primary
    public DataSource mainDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/coltip_db")
                .username("app")
                .password("hjra0yvwWw")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "flywayDataSource")
    public DataSource flywayDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/coltip_db")
                .username("postgres")
                .password("admin")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(@Qualifier("flywayDataSource") DataSource flywayDataSource) {
        return Flyway.configure()
                .dataSource(flywayDataSource)
                .locations("classpath:db/migration") // adjust path as needed
                .baselineOnMigrate(true)
                .load();
    }
}
