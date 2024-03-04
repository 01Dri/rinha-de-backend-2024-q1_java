package me.dri.config;


import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

public class DatabaseConfig {

    private final static HikariDataSource data;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            data = new HikariDataSource();
            data.setJdbcUrl("jdbc:postgresql://db:5432/rinha");
//            data.setJdbcUrl("jdbc:postgresql://localhost:5432/rinha");
            data.setUsername("admin");
            data.setPassword("admin");
            data.setMinimumIdle(5);
            data.setMaximumPoolSize(10);

//            data.setUsername("dridev");
//            data.setPassword("130722");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
        public static HikariDataSource getDataSource() throws SQLException {
            return data;
        }
}
