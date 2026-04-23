# ⚡ QUICK REFERENCE GUIDE

## Smart Attendance Management System

### TL;DR - Get It Running in 2 Minutes

```bash
git clone https://github.com/Melvin-404/java-mini-project.git
cd java-mini-project
./mvnw clean package  # or mvnw.cmd on Windows
# Deploy target/smart-attendance-management.war to Tomcat
# DONE! Database auto-initialized ✓
```

---

## 🔗 Key Files

| File | Purpose | Edit For |
|------|---------|----------|
| `src/main/resources/config.properties` | Database config | Changing DB, password |
| `src/main/java/com/attendance/util/DBConnection.java` | DB management | DB logic changes |
| `src/main/java/com/attendance/util/AppInitializer.java` | Startup hook | Init process changes |
| `src/main/webapp/WEB-INF/web.xml` | Web config | Servlet mappings |
| `pom.xml` | Build config | Dependencies |

---

## 🎯 Common Tasks

### Build Project
```bash
./mvnw clean package
# Output: target/smart-attendance-management.war
```

### Test Database
```bash
java -cp "target/classes;$HOME/.m2/repository/com/h2database/h2/2.2.224/h2-2.2.224.jar" \
     com.attendance.util.DBConnectionTest
```

### Switch to PostgreSQL
```properties
# Edit src/main/resources/config.properties
db.type=postgresql
db.postgresql.url=jdbc:postgresql://localhost:5432/attendance_db
db.postgresql.username=postgres
db.postgresql.password=adnanshakil20
```

### Clean All Data
```bash
rm attendance_db.h2.db  # H2 file
# OR
DROP DATABASE attendance_db;  # PostgreSQL
```

### Run Tests
```bash
./mvnw test
```

### Skip Tests During Build
```bash
./mvnw package -DskipTests
```

---

## 📋 Configuration Quick Ref

**File:** `src/main/resources/config.properties`

```properties
# Database Type
db.type=h2                          # h2 or postgresql

# H2 Settings
db.h2.url=jdbc:h2:./attendance_db;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
db.h2.username=sa
db.h2.password=

# PostgreSQL Settings
db.postgresql.url=jdbc:postgresql://localhost:5432/attendance_db
db.postgresql.username=postgres
db.postgresql.password=adnanshakil20
db.postgresql.driver=org.postgresql.Driver

# Connection Pool
db.pool.maxConnections=10
db.pool.minConnections=5
db.pool.connectionTimeout=30000

# Logging
logging.level=INFO
```

---

## 📍 Application URLs

```
http://localhost:8080/smart-attendance-management/
├── /                          → Home page
├── /addStudent.html           → Add new student
├── /markAttendance.html       → Mark attendance
├── /dashboard                 → View analytics
└── /records                   → View attendance records
```

---

## 🗄️ Database Schema

### STUDENTS
```sql
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    usn VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### ATTENDANCE_RECORDS
```sql
CREATE TABLE attendance_records (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL REFERENCES students(id),
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

---

## 🐛 Troubleshooting

| Problem | Solution |
|---------|----------|
| Build fails | `./mvnw clean install` |
| "Driver not found" | Run `./mvnw clean compile` first |
| Port 8080 in use | Change Tomcat port in `server.xml` |
| DB file locked | Stop Tomcat and remove `attendance_db.h2.db` |
| "Tables not found" | Check `target/classes/config.properties` exists |
| PostgreSQL errors | Verify DB exists: `createdb attendance_db` |

---

## 📊 Status Indicators

✓ = Good  
⚠ = Warning  
✗ = Error  

Expected startup logs:
```
INFO: Database configuration loaded. Type: h2
INFO: Creating/Updating database tables...
INFO: Database schema initialized successfully
✓ Application initialization successful
```

---

## 🔒 Password Reference

Default passwords:
- **H2:** `sa` (username) / `` (password)
- **PostgreSQL:** `postgres` (username) / `adnanshakil20` (password)

Changed in `config.properties`

---

## 📞 Need Help?

1. Read: [README.md](README.md)
2. Setup: [SETUP_GUIDE.md](SETUP_GUIDE.md)
3. Deploy: [DEPLOYMENT_SUMMARY.md](DEPLOYMENT_SUMMARY.md)
4. Details: [COMPLETION_REPORT.md](COMPLETION_REPORT.md)

---

## ✅ Before You Deploy

- [ ] `./mvnw clean package` succeeds
- [ ] `target/smart-attendance-management.war` exists
- [ ] Database test passes
- [ ] config.properties is correct
- [ ] Tomcat is running
- [ ] Port 8080 is available

---

## 🚀 Deployment Checklist

```bash
# 1. Build
./mvnw clean package

# 2. Verify
ls -la target/smart-attendance-management.war

# 3. Deploy
cp target/smart-attendance-management.war $TOMCAT_HOME/webapps/

# 4. Start
$TOMCAT_HOME/bin/startup.sh

# 5. Test
curl http://localhost:8080/smart-attendance-management/

# 6. Check logs
tail -f $TOMCAT_HOME/logs/catalina.out
```

---

## 📝 Code Structure Overview

```
Servlet Layer
    ↓ (HTTP Requests)
Controller/Service Layer
    ↓ (Business Logic)
DAO Layer (StudentDAO, AttendanceDAO)
    ↓ (SQL Queries)
Database Connection (DBConnection)
    ↓ (JDBC)
Database (H2 or PostgreSQL)
```

---

## ⚡ Performance Tips

- Use H2 for: Development, testing, small deployments
- Use PostgreSQL for: Production, large data, multiple users
- Indexes are auto-created on: `(student_id, attendance_date)`
- Max pool connections: 10 (configurable)

---

## 🎓 Tech Stack

- **Java:** 17+
- **Frontend:** HTML5, CSS3, JSP
- **Database:** H2 (default) or PostgreSQL
- **Build:** Maven 3.8+
- **Server:** Tomcat 10+
- **Framework:** Jakarta EE 6.0

---

## 📌 Important Notes

- ✓ Database created automatically
- ✓ No SQL scripts to run
- ✓ Supports H2 and PostgreSQL
- ✓ Thread-safe initialization
- ✓ Comprehensive logging
- ✓ Production-ready code

**Status: READY TO USE! 🚀**

---

Last Updated: April 23, 2026
