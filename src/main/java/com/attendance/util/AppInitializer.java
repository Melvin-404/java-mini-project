package com.attendance.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application startup listener
 * Initializes the database schema when the application starts
 */
public class AppInitializer implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(AppInitializer.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("================================");
        LOGGER.info("Attendance Management System");
        LOGGER.info("Initializing application...");
        LOGGER.info("================================");
        
        try {
            // Test database connection and initialize schema
            DBConnection.testConnection();
            LOGGER.info("✓ Application initialization successful");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "✗ Failed to initialize application - Database connection failed", ex);
            // Log error but don't throw - allow app to start even if DB is temporarily unavailable
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Application shutdown");
    }
}
