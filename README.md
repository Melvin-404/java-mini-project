# Smart Attendance Management System

Student-focused attendance advisor built with HTML, CSS, Java Servlet/JSP, JDBC, and PostgreSQL.

## Stack

- Frontend: HTML, CSS, JSP
- Backend: Java Servlets
- Database: PostgreSQL via JDBC

## Database

Connection details used in the app:

- URL: `jdbc:postgresql://localhost:5432/attendance_db`
- Username: `postgre`
- Password: `Zoro@1234`

Create the schema with:

```sql
\i database/schema.sql
```

## Pages

- `/index.html`
- `/addStudent.html`
- `/markAttendance.html`
- `/dashboard`
- `/records`

## Run

1. Create the PostgreSQL database `attendance_db`.
2. Execute [`database/schema.sql`](/C:/Users/melvi/Documents/Codex/2026-04-23-build-a-web-based-smart-attendance/database/schema.sql).
3. Deploy the project as a WAR in Tomcat 10+.
4. Ensure the PostgreSQL JDBC driver is available if you are not using Maven packaging.

## Notes

- Attendance percentage treats `PRESENT` and `LATE` as attended classes.
- Danger bands:
  - Safe: above 75%
  - Risk: 65% to 75%
  - Danger: below 65%
- Consistency score blends weighted attendance with day-to-day stability.
