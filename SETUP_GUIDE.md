# Setup & Configuration Guide

## For New Developers

### Prerequisites

- **Java 17+** (Check: `java -version`)
- **Maven 3.8+** (Use included wrapper `./mvnw` or `mvnw.cmd`)
- **Git** (for cloning)
- No database installation needed! (Uses embedded H2)

### First-Time Setup

1. **Clone the repository:**
```bash
git clone https://github.com/Melvin-404/java-mini-project.git
cd java-mini-project
```

2. **Build the project:**
```bash
# On Linux/Mac:
./mvnw clean package

# On Windows (PowerShell):
.\mvnw.cmd clean package
```

3. **Deploy to Tomcat:**
   - Copy `target/smart-attendance-management.war` to `$TOMCAT_HOME/webapps/`
   - Start Tomcat
   - Visit: `http://localhost:8080/smart-attendance-management/`

**That's it!** The database will be created automatically.

## Database Configuration

### Using H2 (Default - Zero Configuration)

No configuration needed. H2 is:
- ✓ Embedded in the application
- ✓ Automatically creates schema
- ✓ Persists data to `attendance_db.h2.db` file
- ✓ Perfect for development and testing

**File location:** Project root directory (`./attendance_db.h2.db`)

### Switching to PostgreSQL

If you want to use PostgreSQL instead:

1. **Install PostgreSQL:**
   - Download from https://www.postgresql.org/download/
   - Install and remember the password

2. **Create database:**
```bash
psql -U postgres
postgres=# CREATE DATABASE attendance_db;
postgres=# \q
```

3. **Update configuration file** `src/main/resources/config.properties`:
```properties
db.type=postgresql
db.postgresql.url=jdbc:postgresql://localhost:5432/attendance_db
db.postgresql.username=postgres
db.postgresql.password=YOUR_PASSWORD_HERE
db.postgresql.driver=org.postgresql.Driver
```

4. **Update pom.xml** to include PostgreSQL driver:

Find the `<dependencies>` section and add:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.1</version>
</dependency>
```

5. **Rebuild:**
```bash
./mvnw clean package
```

## Testing Database Connection

Verify your database setup works:

**On Linux/Mac:**
```bash
java -cp "target/classes:$HOME/.m2/repository/com/h2database/h2/2.2.224/h2-2.2.224.jar" \
     com.attendance.util.DBConnectionTest
```

**On Windows (PowerShell):**
```powershell
$h2Path = "$env:USERPROFILE\.m2\repository\com\h2database\h2\2.2.224\h2-2.2.224.jar"
java -cp "target/classes;$h2Path" com.attendance.util.DBConnectionTest
```

**Expected output:**
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
  ✓ Read operation successful - Student: Test Student
  ✓ Delete operation successful

✓ All tests passed!
```

## Troubleshooting

| Problem | Solution |
|---------|----------|
| "mvn command not found" | Use `./mvnw` (Mac/Linux) or `.\mvnw.cmd` (Windows) |
| Build fails - missing dependencies | Delete `.m2` cache and run `./mvnw clean install` |
| Port 8080 already in use | Change Tomcat port in `conf/server.xml` |
| Database file locked | Ensure no other app instance is running |
| Tables not found | Check `target/classes/config.properties` exists |
| PostgreSQL connection refused | Verify PostgreSQL service is running |

## Development Environment Setup

### IDE Configuration (IntelliJ IDEA / Eclipse)

1. **Open project** as Maven project
2. **Mark folders:**
   - Mark `src/main/java` as Sources
   - Mark `src/main/webapp` as Web Resources
   - Mark `src/test/java` as Test Sources
3. **Configure Tomcat:**
   - Run → Edit Configurations → Add Tomcat Server
   - Set deployment: `war exploded`
   - Application context: `/smart-attendance-management`

### VS Code Setup

1. **Install extensions:**
   - Extension Pack for Java
   - Maven for Java
   - Tomcat for Java

2. **Run/Debug:**
   - F5 to start debugging
   - Configuration in `.vscode/launch.json`

## Key Configuration Files

### `src/main/resources/config.properties`
```properties
# Database configuration
db.type=h2
db.h2.url=jdbc:h2:./attendance_db;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE

# Connection pool
db.pool.maxConnections=10

# Logging
logging.level=INFO
```

### `src/main/webapp/WEB-INF/web.xml`
- Application entry point configuration
- Servlet mappings
- Welcome files

### `pom.xml`
- Maven project configuration
- Dependencies
- Build plugins

## Common Tasks

### Build WAR file:
```bash
./mvnw clean package
```

### Clean project:
```bash
./mvnw clean
```

### Run tests:
```bash
./mvnw test
```

### Skip tests during build:
```bash
./mvnw package -DskipTests
```

### Check for dependency vulnerabilities:
```bash
./mvnw org.owasp:dependency-check-maven:check
```

## Database Schema

### Students Table
```sql
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    usn VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Attendance Records Table
```sql
CREATE TABLE attendance_records (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    attendance_date DATE NOT NULL,
    status VARCHAR(10) NOT NULL CHECK (status IN ('PRESENT', 'ABSENT', 'LATE')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (student_id, attendance_date)
);
```

### Indexes
```sql
CREATE INDEX idx_attendance_student_date ON attendance_records(student_id, attendance_date DESC);
CREATE INDEX idx_student_name ON students(name);
```

All tables and indexes are created automatically on application startup!

## Performance Tips

1. **Development**: Use default H2 (faster, no setup)
2. **Production**: Use PostgreSQL with proper indexing
3. **Large datasets**: Add database connection pooling
4. **Slow queries**: Check generated indexes on attendance_records

## Security Considerations

- [ ] Add authentication/authorization
- [ ] Implement CSRF protection
- [ ] Use HTTPS in production
- [ ] Validate all user inputs
- [ ] Implement rate limiting
- [ ] Use prepared statements (already done ✓)
- [ ] Sanitize database outputs
- [ ] Keep dependencies updated

## Next Steps

1. ✓ Build the project
2. ✓ Test database connection
3. → Deploy to Tomcat
4. → Add sample data
5. → Test features
6. → Add authentication
7. → Deploy to production

## Support

For issues or questions:
1. Check the [README](README.md)
2. Review application logs
3. Run the database test utility
4. Check Tomcat logs in `$TOMCAT_HOME/logs/`

## Additional Resources

- [Jakarta EE Documentation](https://jakarta.ee/)
- [H2 Database](https://www.h2database.com/html/main.html)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Maven Documentation](https://maven.apache.org/)
- [Apache Tomcat](https://tomcat.apache.org/)
