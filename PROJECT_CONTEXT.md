# PROJECT_CONTEXT.md — Smart Attendance Management System

## What This Project Is
A web-based attendance management system built with Java Servlets/JSP, JDBC, and embedded H2 database. Features role-based access control (Teacher/Student), student management, attendance marking with status (Present/Absent/Late), analytics dashboard with risk assessment (Safe/Risk/Danger), consistency scoring, and attendance streak tracking. Designed to work out-of-the-box with zero database configuration.

## Current Status
- [x] H2 embedded database with automatic schema initialization on startup
- [x] RBAC: Teacher (add students, mark attendance) and Student (view own data) roles
- [x] Analytics dashboard with percentage, required classes, safe bunk, danger level
- [x] Color-coded risk assessment (Safe/Risk/Danger) with streak tracking
- [x] PostgreSQL support as optional alternative to H2
- [x] Auth filter with cache prevention headers
- [x] Works out-of-the-box with `mvn clean package` + Tomcat
- [ ] No test suite beyond DBConnectionTest
- [ ] Hardcoded demo credentials (teacher/teacher123, student/student123)
- [ ] No Docker/deployment config

## Architecture Overview
- Backend: Java 17+, Servlets (Jakarta EE/Javax), JSP, Maven build
- Frontend: HTML5, CSS3, JSP (server-rendered, no JS framework)
- Database: H2 2.2.224 (default, file-based) or PostgreSQL 42.7.1
- Deployment: WAR on Tomcat 10+ (or embedded via tomcat7-maven-plugin on port 8080)

## Key Files & Entry Points
- `src/main/java/com/attendance/servlet/LoginServlet.java` — Login with hardcoded credentials
- `src/main/java/com/attendance/servlet/DashboardServlet.java` — Builds analytics dashboard data
- `src/main/java/com/attendance/servlet/AttendanceServlet.java` — Mark attendance (Teacher only)
- `src/main/java/com/attendance/servlet/AddStudentServlet.java` — Add student (Teacher only)
- `src/main/java/com/attendance/dao/StudentDAO.java` — Student CRUD operations
- `src/main/java/com/attendance/dao/AttendanceDAO.java` — Attendance upsert (update-or-insert)
- `src/main/java/com/attendance/service/AnalyticsService.java` — Dashboard analytics engine
- `src/main/java/com/attendance/util/DBConnection.java` — DB connection manager with auto-schema init
- `src/main/webapp/dashboard.jsp` — Analytics dashboard view
- `src/main/resources/config.properties` — DB config (H2 default, PostgreSQL optional)
- `pom.xml` — Maven build (Java 17, WAR packaging)

## Environment & Setup
- Build: `./mvnw.cmd clean package`
- Deploy: copy target/smart-attendance-management.war to Tomcat webapps, or use `mvn tomcat7:run`
- Access: http://localhost:8080/smart-attendance-management
- Login: teacher / teacher123 or student / student123
- **Gotcha**: PostgreSQL password is hardcoded in config.properties (adnanshakil20) — not suitable for production
- **Gotcha**: Uses javax.servlet (not jakarta.servlet) — compatible with Tomcat 9, NOT Tomcat 10+

## Where I Left Off
- Last thing: Implemented RBAC with Teacher and Student roles (commit 6fe2717)
- Next: Add proper user registration, replace hardcoded credentials with DB-backed auth
- Known: No test suite, no Docker config, uses javax.servlet (not compatible with Tomcat 10+)

## Git & Deployment
- Remote: `https://github.com/Melvin-404/java-mini-project.git`
- Branch: main
- Last commit: "Implement RBAC system with Teacher and Student roles"

## Context for AI Assistants
- Uses vanilla Java Servlets + JSP — no Spring, no JSF, no modern Java web frameworks
- DB schema auto-initializes on first connection via synchronized block in DBConnection.getConnection()
- Uses "upsert" pattern for attendance: UPDATE first, INSERT if 0 rows affected
- Analytics in AnalyticsService.java is statistically meaningful: calculates required classes to reach 75% target, safe bunks, consistency scores, and weekly/monthly summaries
- Danger level logic: >75% Safe, 60-75% Risk, <60% Danger
- The analytics model (StudentDashboard.java) includes future prediction: "if you attend X more classes, what will your percentage be?"
- H2 runs in PostgreSQL compatibility mode (`MODE=PostgreSQL`) for portable SQL
- Build includes both H2 and PostgreSQL drivers — switching just requires changing config.properties
- The project was a collaboration: Melvin (original work) + Mohammad Adnan Shakil (RBAC implementation)
