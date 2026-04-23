package com.attendance.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database Connection Manager
 * Handles connection pooling and automatic schema initialization
 * Supports both H2 (default) and PostgreSQL databases
 */
public final class DBConnection {
    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static final Object LOCK = new Object();
    private static volatile boolean initialized = false;
    
    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;
    private static String dbDriver;
    private static String dbType;

    static {
        loadConfiguration();
    }

    private DBConnection() {
        // Utility class - no instantiation
    }

    /**
     * Load configuration from properties file
     */
    private static void loadConfiguration() {
        Properties props = new Properties();
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                LOGGER.warning("config.properties not found. Using default H2 configuration.");
                setDefaultH2Config();
                return;
            }
            props.load(input);
            
            dbType = props.getProperty("db.type", "h2").toLowerCase();
            
            if ("postgresql".equalsIgnoreCase(dbType)) {
                loadPostgreSQLConfig(props);
            } else {
                loadH2Config(props);
            }
            
            LOGGER.info("Database configuration loaded. Type: " + dbType);
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Error loading configuration, using default H2 settings", ex);
            setDefaultH2Config();
        }
    }

    private static void loadH2Config(Properties props) {
        dbType = "h2";
        dbUrl = props.getProperty("db.h2.url", "jdbc:h2:./attendance_db;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        dbUsername = props.getProperty("db.h2.username", "sa");
        dbPassword = props.getProperty("db.h2.password", "");
        dbDriver = "org.h2.Driver";
    }

    private static void loadPostgreSQLConfig(Properties props) {
        dbType = "postgresql";
        dbUrl = props.getProperty("db.postgresql.url");
        dbUsername = props.getProperty("db.postgresql.username");
        dbPassword = props.getProperty("db.postgresql.password");
        dbDriver = props.getProperty("db.postgresql.driver", "org.postgresql.Driver");
        
        if (dbUrl == null || dbUsername == null || dbPassword == null) {
            throw new RuntimeException("PostgreSQL configuration incomplete. Please set db.postgresql.url, db.postgresql.username, and db.postgresql.password in config.properties");
        }
    }

    private static void setDefaultH2Config() {
        dbType = "h2";
        dbUrl = "jdbc:h2:./attendance_db;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
        dbUsername = "sa";
        dbPassword = "";
        dbDriver = "org.h2.Driver";
    }

    /**
     * Get a database connection with automatic schema initialization
     */
    public static Connection getConnection() throws SQLException {
        try {
            loadDriver();
        } catch (ClassNotFoundException ex) {
            String msg = "Database driver not found: " + dbDriver;
            LOGGER.log(Level.SEVERE, msg, ex);
            throw new SQLException(msg, ex);
        }

        Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        
        if (!initialized) {
            synchronized (LOCK) {
                if (!initialized) {
                    try {
                        initializeSchema(conn);
                        initialized = true;
                        LOGGER.info("Database schema initialized successfully");
                    } catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, "Failed to initialize database schema", ex);
                        throw ex;
                    }
                }
            }
        }
        
        return conn;
    }

    /**
     * Load the appropriate database driver
     */
    private static void loadDriver() throws ClassNotFoundException {
        Class.forName(dbDriver);
    }

    /**
     * Initialize database schema with all required tables and indexes
     */
    private static void initializeSchema(Connection conn) throws SQLException {
        try (var stmt = conn.createStatement()) {
            // Drop and recreate tables to ensure clean state (development)
            // Comment out the DROP commands for production to preserve data
            
            LOGGER.info("Creating/Updating database tables...");
            
            // Create students table
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "id SERIAL PRIMARY KEY," +
                    "name VARCHAR(120) NOT NULL," +
                    "usn VARCHAR(50) NOT NULL UNIQUE," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
            LOGGER.fine("Students table ready");
            
            // Create attendance_records table
            stmt.execute("CREATE TABLE IF NOT EXISTS attendance_records (" +
                    "id SERIAL PRIMARY KEY," +
                    "student_id INTEGER NOT NULL REFERENCES students(id) ON DELETE CASCADE," +
                    "attendance_date DATE NOT NULL," +
                    "status VARCHAR(10) NOT NULL CHECK (status IN ('PRESENT', 'ABSENT', 'LATE'))," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "UNIQUE (student_id, attendance_date)" +
                    ")");
            LOGGER.fine("Attendance records table ready");
            
            // Create index for better query performance
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_attendance_student_date " +
                    "ON attendance_records(student_id, attendance_date DESC)");
            LOGGER.fine("Indexes created");
            
            // Create student_idx for name searches
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_student_name " +
                    "ON students(name)");
            LOGGER.fine("All tables and indexes initialized");
        }
    }

    /**
     * Test database connectivity and schema
     */
    public static void testConnection() throws SQLException {
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            LOGGER.info("Connected to: " + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
            
            // Verify tables exist
            var resultSet = metaData.getTables(null, null, "STUDENTS", null);
            if (resultSet.next()) {
                LOGGER.info("✓ Database connection successful and schema is ready");
            } else {
                LOGGER.warning("⚠ Tables not found - schema may not be initialized");
            }
        }
    }
}
