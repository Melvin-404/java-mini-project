package com.attendance.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database Connection Test Utility
 * Verifies that database connection and schema initialization work correctly
 */
public class DBConnectionTest {
    
    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("Database Connection Test");
        System.out.println("================================\n");
        
        try {
            testDatabaseConnection();
            testTableCreation();
            testDataOperations();
            
            System.out.println("\n================================");
            System.out.println("✓ All tests passed!");
            System.out.println("================================");
        } catch (SQLException ex) {
            System.err.println("\n✗ Test failed: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void testDatabaseConnection() throws SQLException {
        System.out.println("Test 1: Testing database connection...");
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Connection is null");
            }
            DatabaseMetaData metaData = conn.getMetaData();
            String productName = metaData.getDatabaseProductName();
            String productVersion = metaData.getDatabaseProductVersion();
            
            System.out.println("  ✓ Connected to: " + productName + " " + productVersion);
        }
    }
    
    private static void testTableCreation() throws SQLException {
        System.out.println("\nTest 2: Checking table schema...");
        try (Connection conn = DBConnection.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            // For PostgreSQL, table names are lowercase unless quoted
            // Try lowercase first (PostgreSQL default)
            boolean studentTableExists = false;
            ResultSet studentsTable = metaData.getTables(null, "public", "students", null);
            if (studentsTable.next()) {
                studentTableExists = true;
                System.out.println("  ✓ STUDENTS table exists");
            }
            
            if (!studentTableExists) {
                // Try uppercase
                studentsTable = metaData.getTables(null, "public", "STUDENTS", null);
                if (studentsTable.next()) {
                    System.out.println("  ✓ STUDENTS table exists");
                } else {
                    throw new SQLException("STUDENTS table not found");
                }
            }
            
            // Check attendance_records table
            boolean attendanceTableExists = false;
            ResultSet attendanceTable = metaData.getTables(null, "public", "attendance_records", null);
            if (attendanceTable.next()) {
                attendanceTableExists = true;
                System.out.println("  ✓ ATTENDANCE_RECORDS table exists");
            }
            
            if (!attendanceTableExists) {
                attendanceTable = metaData.getTables(null, "public", "ATTENDANCE_RECORDS", null);
                if (attendanceTable.next()) {
                    System.out.println("  ✓ ATTENDANCE_RECORDS table exists");
                } else {
                    throw new SQLException("ATTENDANCE_RECORDS table not found");
                }
            }
        }
    }
    
    private static void testDataOperations() throws SQLException {
        System.out.println("\nTest 3: Testing basic data operations...");
        try (Connection conn = DBConnection.getConnection()) {
            // Test insert
            var stmt = conn.prepareStatement("INSERT INTO students(name, usn) VALUES (?, ?)");
            stmt.setString(1, "Test Student");
            stmt.setString(2, "TST001");
            stmt.executeUpdate();
            System.out.println("  ✓ Insert operation successful");
            
            // Test read
            var query = conn.prepareStatement("SELECT id, name, usn FROM students WHERE usn = ?");
            query.setString(1, "TST001");
            var rs = query.executeQuery();
            if (rs.next()) {
                System.out.println("  ✓ Read operation successful - Student: " + rs.getString("name"));
            }
            
            // Test delete
            var delete = conn.prepareStatement("DELETE FROM students WHERE usn = ?");
            delete.setString(1, "TST001");
            delete.executeUpdate();
            System.out.println("  ✓ Delete operation successful");
        }
    }
}
