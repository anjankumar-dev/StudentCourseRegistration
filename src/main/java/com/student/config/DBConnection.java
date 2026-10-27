package com.student.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class DBConnection {
    private static final HikariDataSource DATA_SOURCE = createDataSource();

    private DBConnection() {}

    private static HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(System.getenv().getOrDefault(
                "DB_URL", "jdbc:mysql://localhost:3306/student_course_db"));
        config.setUsername(System.getenv().getOrDefault("DB_USER", "root"));
        config.setPassword(System.getenv().getOrDefault("DB_PASSWORD", "anjanGemini24"));

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(10000);
        config.setPoolName("StudentCoursePool");
        return new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    public static void closePool() {
        if (!DATA_SOURCE.isClosed()) {
            DATA_SOURCE.close();
        }
    }
}
