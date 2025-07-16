package ca.coltip.config;

import ca.coltip.services.impl.VaultService;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Autowired
    VaultService vaultService;

    @Value("db.user.general")
    String userGeneral;

    @Value("db.user.flyway")
    String userFlyway;

    @Value("path.to.database")
    String pathToDb;

    @Value("diver.to.database")
    String driverToDb;

    @Bean(name = "mainDataSource")
    @Primary
    public DataSource mainDataSource() {
        return DataSourceBuilder.create()
                .url(pathToDb)
                .username(userGeneral)
                .password(vaultService.getDBGeneralPass())
                .driverClassName(driverToDb)
                .build();
    }

    @Bean(name = "flywayDataSource")
    public DataSource flywayDataSource() {
        return DataSourceBuilder.create()
                .url(pathToDb)
                .username(userFlyway)
                .password(vaultService.getDbFlywayPass())
                .driverClassName(driverToDb)
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
