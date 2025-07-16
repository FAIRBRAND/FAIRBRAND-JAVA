package ca.coltip.services.impl;

import ca.coltip.services.IHealthCheckService;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service("DBHealthCheckService")
public class DBHealthCheckService implements IHealthCheckService {

    public boolean testHealth() {
        String url = "jdbc:postgresql://localhost:5432/coltip_db";
        String user = "postgres";
        String password = "admin";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("Connected to the database!");
                return true;
            } else {
                System.out.println("Failed to make connection!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
            return false;
        }
    }
}
