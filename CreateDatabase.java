import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class CreateDatabase {
    public static void main(String[] args) {
        // First try to connect to PostgreSQL default database to create attendance_db
        String postgresUrl = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "adnanshakil20";
        
        try {
            Class.forName("org.postgresql.Driver");
            
            System.out.println("Connecting to PostgreSQL...");
            try (Connection conn = DriverManager.getConnection(postgresUrl, username, password)) {
                System.out.println("✓ Connected to PostgreSQL");
                
                try (Statement stmt = conn.createStatement()) {
                    // Drop database if exists (for clean state)
                    try {
                        stmt.executeUpdate("DROP DATABASE IF EXISTS attendance_db");
                        System.out.println("✓ Dropped existing database");
                    } catch (Exception e) {
                        System.out.println("(No existing database to drop)");
                    }
                    
                    // Create database
                    stmt.executeUpdate("CREATE DATABASE attendance_db");
                    System.out.println("✓ Database 'attendance_db' created successfully");
                }
            }
        } catch (ClassNotFoundException ex) {
            System.err.println("✗ PostgreSQL driver not found");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.err.println("✗ Database operation failed");
            ex.printStackTrace();
        }
    }
}
