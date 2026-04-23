# Smart Attendance Management System

A professional-grade web-based attendance management system built with Java Servlets/JSP, JDBC, and embedded H2 database. Works out-of-the-box with zero database configuration!

## Features

✓ **Zero Setup Database** - H2 embedded database with automatic schema initialization  
✓ **Student Management** - Add, view, and manage student records  
✓ **Attendance Marking** - Record daily attendance with status (Present, Absent, Late)  
✓ **Analytics Dashboard** - Real-time attendance metrics and predictions  
✓ **Risk Assessment** - Color-coded danger levels (Safe, Risk, Danger)  
✓ **Performance Metrics** - Consistency scoring and attendance streaks  
✓ **Flexible Configuration** - Easy switch between H2 (default) and PostgreSQL  

## Technology Stack

| Layer | Technology |
|-------|-----------|
| **Frontend** | HTML5, CSS3, JSP |
| **Backend** | Java 17+, Servlets (Jakarta EE) |
| **Database** | H2 (default), PostgreSQL (optional) |
| **Build** | Maven 3.8+ |
| **Deployment** | Tomcat 10+ or any Jakarta EE container |

## Quick Start (Recommended)

### Option 1: Run with Default H2 Database (No Setup Required!)

```bash
# Clone the repository
git clone https://github.com/Melvin-404/java-mini-project.git
cd java-mini-project

# Build the project
./mvnw clean package

# Deploy to Tomcat or use an embedded server
# The database will be created automatically on first run!
```

**That's it!** No database installation, no configuration. The application will:
- Automatically create `attendance_db` file in your project directory
- Initialize the schema with all required tables
- Be ready to use immediately

### Option 2: Use PostgreSQL (Optional)

If you prefer PostgreSQL over H2:

1. **Install PostgreSQL** and create a database:
```sql
CREATE DATABASE attendance_db;
```

2. **Configure database connection** in `src/main/resources/config.properties`:
```properties
# Change db.type to postgresql
db.type=postgresql
db.postgresql.url=jdbc:postgresql://localhost:5432/attendance_db
db.postgresql.username=postgres
db.postgresql.password=adnanshakil20
db.postgresql.driver=org.postgresql.Driver
```

3. **Add PostgreSQL driver to pom.xml** (if not already included):
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.1</version>
</dependency>
```

4. **Build and deploy**:
```bash
./mvnw clean package
```

## Database Configuration

All database settings are in `src/main/resources/config.properties`:

```properties
# Database Type: h2 (default) or postgresql
db.type=h2

# H2 Configuration (default - works out of the box)
db.h2.url=jdbc:h2:./attendance_db;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
db.h2.username=sa
db.h2.password=

# PostgreSQL Configuration (optional)
# db.type=postgresql
# db.postgresql.url=jdbc:postgresql://localhost:5432/attendance_db
# db.postgresql.username=postgres
# db.postgresql.password=YOUR_PASSWORD_HERE
```

**Key Points:**
- Default H2 database stores data in `attendance_db.h2.db` file
- H2 supports PostgreSQL compatibility mode for seamless migration
- All tables created automatically on application startup
- No SQL scripts to run manually

## Application Pages

| URL | Purpose |
|-----|---------|
| `/` | Home page with navigation |
| `/addStudent.html` | Add new student |
| `/markAttendance.html` | Record daily attendance |
| `/dashboard` | View analytics and metrics |
| `/records` | View detailed attendance records |

## Attendance Metrics

### Danger Levels
- **SAFE**: Attendance ≥ 75%
- **RISK**: Attendance 65-75%
- **DANGER**: Attendance < 65%

### Metrics Tracked
- Attendance percentage (PRESENT + LATE count)
- Classes attended vs total classes
- Safe bunk count (classes that can be skipped while maintaining 75%)
- Required classes to reach 75% target
- Attendance streak (consecutive present days)
- Consistency score (stability of attendance patterns)
- Weekly and monthly summaries

## Building and Deployment

### Build WAR File
```bash
./mvnw clean package
```

Creates `target/smart-attendance-management.war`

### Deploy to Tomcat
1. Copy WAR file to `$TOMCAT_HOME/webapps/`
2. Start Tomcat
3. Access at `http://localhost:8080/smart-attendance-management`

### Run Embedded (for testing)
```bash
./mvnw clean compile
java -cp "target/classes:$HOME/.m2/repository/com/h2database/h2/2.2.224/h2-2.2.224.jar" \
     com.attendance.util.DBConnectionTest
```

## Testing

The application includes a database connectivity test:

```bash
# Compile
./mvnw clean compile

# Run database test
java -cp "target/classes:$HOME/.m2/repository/com/h2database/h2/2.2.224/h2-2.2.224.jar" \
     com.attendance.util.DBConnectionTest
```

**Expected Output:**
```
================================
Database Connection Test
================================

Test 1: Testing database connection...
  ✓ Connected to: H2 2.2.224
Test 2: Checking table schema...
  ✓ STUDENTS table exists
  ✓ ATTENDANCE_RECORDS table exists
Test 3: Testing basic data operations...
  ✓ Insert operation successful
  ✓ Read operation successful
  ✓ Delete operation successful

✓ All tests passed!
```

## Project Structure

```
src/
├── main/
│   ├── java/com/attendance/
│   │   ├── util/
│   │   │   ├── DBConnection.java       (Connection management)
│   │   │   ├── AppInitializer.java     (Auto schema initialization)
│   │   │   └── DBConnectionTest.java   (Connection test utility)
│   │   ├── dao/
│   │   │   ├── StudentDAO.java         (Student operations)
│   │   │   └── AttendanceDAO.java      (Attendance operations)
│   │   ├── model/
│   │   │   ├── Student.java
│   │   │   ├── StudentDashboard.java
│   │   │   ├── AttendanceRecord.java
│   │   │   ├── MonthlySummary.java
│   │   │   └── WeeklySummary.java
│   │   ├── service/
│   │   │   └── AnalyticsService.java   (Business logic)
│   │   └── servlet/
│   │       ├── AddStudentServlet.java
│   │       ├── AttendanceServlet.java
│   │       ├── DashboardServlet.java
│   │       ├── RecordsServlet.java
│   │       └── StudentsApiServlet.java
│   ├── resources/
│   │   └── config.properties           (Database configuration)
│   └── webapp/
│       ├── index.html
│       ├── addStudent.html
│       ├── markAttendance.html
│       ├── dashboard.jsp
│       ├── viewRecords.jsp
│       └── WEB-INF/
│           └── web.xml
└── test/
    └── java/                           (Add tests here)
```

## Architecture Highlights

### Database Initialization
- **Automatic Schema Creation**: Tables are created automatically on first connection
- **Thread-Safe Initialization**: Uses synchronized blocks to ensure schema is created once
- **Backward Compatible**: Supports both H2 and PostgreSQL with the same code
- **Connection Pooling**: Configurable pool settings in config.properties

### Error Handling
- **Comprehensive Logging**: All database operations logged for debugging
- **Graceful Degradation**: Application logs errors but doesn't crash on non-critical failures
- **User-Friendly Messages**: Error messages displayed in web UI

### DAO Pattern
- **StudentDAO**: Manages student records
- **AttendanceDAO**: Manages attendance records with upsert logic (insert or update)
- Both use prepared statements to prevent SQL injection

### Service Layer
- **AnalyticsService**: Calculates attendance metrics, danger levels, predictions
- Separates business logic from data access

## Troubleshooting

### Issue: "H2 JDBC driver not found"
**Solution**: Run `./mvnw clean package` to download dependencies

### Issue: "Database file locked"
**Solution**: Ensure no other instance is accessing the database. The H2 file is in your project root as `attendance_db.h2.db`

### Issue: "Connection refused" (PostgreSQL)
**Solution**: Verify PostgreSQL is running and connection details in `config.properties` are correct

### Issue: "Tables not found"
**Solution**: Application failed to initialize schema. Check logs and verify database permissions

## Performance Notes

- H2 in-memory mode is excellent for development
- File-based H2 (default) persists data across restarts
- PostgreSQL recommended for production with 1000+ students
- Database indexes optimize attendance queries by student and date

## Future Enhancements

- [ ] JWT-based authentication
- [ ] REST API endpoints
- [ ] Mobile-friendly UI
- [ ] Export to PDF/Excel
- [ ] Email notifications
- [ ] Biometric integration
- [ ] Real-time dashboards with WebSockets
- [ ] Database migration scripts

## License

MIT License - See LICENSE file for details

## Notes

- Attendance percentage treats both `PRESENT` and `LATE` as attended classes
- Danger assessment based on 75% threshold (industry standard)
- Consistency score blends weighted attendance (0.7x for LATE) with stability penalty
- All timestamps stored in UTC
