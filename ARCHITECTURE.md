# Architecture

## Component Breakdown

### Servlet Layer (Controllers)
- **Role:** HTTP request handling, form processing, session management
- **Tech:** Java Servlets (javax.servlet 3.1)
- **Location:** src/main/java/com/attendance/servlet/ (9 servlets)

### DAO Layer (Data Access)
- **Role:** CRUD operations with prepared statements for SQL injection prevention
- **Tech:** Java JDBC with PostgreSQL-compatible H2 database
- **Location:** src/main/java/com/attendance/dao/ (StudentDAO, AttendanceDAO)

### Service Layer
- **Role:** Business logic for attendance analytics, predictions, streak calculations
- **Tech:** Plain Java with configurable thresholds
- **Location:** src/main/java/com/attendance/service/AnalyticsService.java

### Model Layer
- **Role:** POJO data carriers
- **Tech:** Java POJOs
- **Location:** src/main/java/com/attendance/model/ (5 models)

### Utility Layer
- **Role:** Database connection pooling, startup initialization
- **Tech:** Java with synchronized singleton pattern
- **Location:** src/main/java/com/attendance/util/ (DBConnection, AppInitializer)

### Frontend
- **Role:** Dynamic UI with JSP pages, role-aware button visibility
- **Tech:** HTML5 + CSS3 + JavaScript + JSP
- **Location:** src/main/webapp/

## Key Architectural Decisions

### Decision 1: Vanilla Java Servlets over Spring Boot
**What:** Uses plain javax.servlet with JSP instead of Spring MVC/Boot
**Why:** Academic project requirement. Focuses on Servlet fundamentals, web.xml configuration, and MVC patterns without framework abstraction.
**Tradeoff:** More boilerplate code. No built-in dependency injection, validation, or security features.

### Decision 2: H2 with PostgreSQL Compatibility Mode
**What:** Defaults to H2 embedded database running in PostgreSQL compatibility mode with optional PostgreSQL switch
**Why:** Zero-configuration development (H2 file-based) but seamless migration to production PostgreSQL. H2's MODE=PostgreSQL accepts PostgreSQL syntax.
**Tradeoff:** Some PostgreSQL-specific features may not work in H2 mode. Slight behavioral differences.

### Decision 3: Custom RBAC Filter over Framework Security
**What:** AuthFilter intercepts all requests, checks session role attribute, redirects to access-denied
**Why:** Simpler than integrating Spring Security. Direct mapping of filter to role-based access rules without annotations.
**Tradeoff:** Hardcoded credentials (teacher/teacher123, student/student123). No password hashing.

### Decision 4: Analytics Dashboard with Custom Prediction Engine
**What:** AnalyticsService calculates percentages, danger levels, consistency scores, and future predictions
**Why:** Avoids database-level analytics. Allows complex business logic (weighted attendance, stability penalties) that SQL alone cannot express cleanly.
**Tradeoff:** All data must be loaded into memory for calculations.

## Data Flow
1. User visits login.html → submits credentials → LoginServlet validates hardcoded credentials, creates session
2. AuthFilter checks session on every request; teacher-only URLs blocked for students
3. Teacher adds student via addStudent.html → AddStudentServlet → StudentDAO
4. Teacher marks attendance via markAttendance.html → AttendanceServlet → AttendanceDAO (upsert pattern)
5. Dashboard loads: DashboardServlet queries DAOs → AnalyticsService computes metrics → renders dashboard.jsp
6. Records page: RecordsServlet fetches attendance with JOINs → renders viewRecords.jsp

## Known Limitations
- Hardcoded demo credentials (teacher/teacher123, student/student123)
- No proper user registration or DB-backed authentication
- javax.servlet 3.1 — not compatible with Tomcat 10+/Jakarta EE 9+
- No HTTPS, CSRF protection, or password hashing
- PostgreSQL password hardcoded in config.properties
- No Docker support

## Future Considerations
- Migrate to Jakarta EE for Tomcat 10+ compatibility
- Add DB-backed authentication with password hashing
- Add CSRF tokens and input sanitization
- Add Dockerfile and docker-compose for containerized deployment
