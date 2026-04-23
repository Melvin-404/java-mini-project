import java.sql.*;

public class CheckTables {
    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/attendance_db";
        
        try (Connection conn = DriverManager.getConnection(url, "postgres", "adnanshakil20")) {
            System.out.println("Connected to database");
            
            // List all tables
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, "public", "%", new String[]{"TABLE"});
            
            System.out.println("\nExisting tables:");
            if (!tables.next()) {
                System.out.println("  (No tables found)");
            } else {
                System.out.println("  - " + tables.getString("TABLE_NAME"));
                while (tables.next()) {
                    System.out.println("  - " + tables.getString("TABLE_NAME"));
                }
            }
            
            // Try to create tables manually
            System.out.println("\nCreating tables...");
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                        "id SERIAL PRIMARY KEY," +
                        "name VARCHAR(120) NOT NULL," +
                        "usn VARCHAR(50) NOT NULL UNIQUE," +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")");
                System.out.println("✓ Students table created");
                
                stmt.execute("CREATE TABLE IF NOT EXISTS attendance_records (" +
                        "id SERIAL PRIMARY KEY," +
                        "student_id INTEGER NOT NULL REFERENCES students(id) ON DELETE CASCADE," +
                        "attendance_date DATE NOT NULL," +
                        "status VARCHAR(10) NOT NULL CHECK (status IN ('PRESENT', 'ABSENT', 'LATE'))," +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "UNIQUE (student_id, attendance_date)" +
                        ")");
                System.out.println("✓ Attendance records table created");
            }
            
            // List tables again
            System.out.println("\nTables after creation:");
            tables = meta.getTables(null, "public", "%", new String[]{"TABLE"});
            while (tables.next()) {
                System.out.println("  - " + tables.getString("TABLE_NAME"));
            }
        }
    }
}
