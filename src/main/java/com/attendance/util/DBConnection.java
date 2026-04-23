package com.attendance.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private static final String URL = "jdbc:h2:mem:attendance_db;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";
    private static boolean initialized = false;

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("H2 JDBC driver not found.", ex);
        }
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        
        if (!initialized) {
            initializeSchema(conn);
            initialized = true;
        }
        
        return conn;
    }

    private static void initializeSchema(Connection conn) throws SQLException {
        try (var stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "id SERIAL PRIMARY KEY," +
                    "name VARCHAR(120) NOT NULL," +
                    "usn VARCHAR(50) NOT NULL UNIQUE," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS attendance_records (" +
                    "id SERIAL PRIMARY KEY," +
                    "student_id INTEGER NOT NULL REFERENCES students(id) ON DELETE CASCADE," +
                    "attendance_date DATE NOT NULL," +
                    "status VARCHAR(10) NOT NULL CHECK (status IN ('PRESENT', 'ABSENT', 'LATE'))," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "UNIQUE (student_id, attendance_date)" +
                    ")");
            
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_attendance_student_date " +
                    "ON attendance_records(student_id, attendance_date DESC)");
        }
    }
}
