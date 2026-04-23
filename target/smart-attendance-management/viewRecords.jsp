<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.attendance.model.AttendanceRecord" %>
<%@ page import="com.attendance.model.Student" %>
<%
    List<AttendanceRecord> records = (List<AttendanceRecord>) request.getAttribute("records");
    List<Student> students = (List<Student>) request.getAttribute("students");
    String selectedStudentId = (String) request.getAttribute("selectedStudentId");
    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (selectedStudentId == null) {
        selectedStudentId = "";
    }
    if (startDate == null) {
        startDate = "";
    }
    if (endDate == null) {
        endDate = "";
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Attendance Records</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Poppins:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
</head>
<body>
<div class="app-shell">
    <aside class="sidebar">
        <div class="brand">
            <div class="brand-badge">SA</div>
            <div>
                <h2>Student Attendance Advisor</h2>
                <p>Browse complete attendance history.</p>
            </div>
        </div>
        <nav class="nav-menu">
            <a class="nav-link" href="<%= request.getContextPath() %>/index.html">Home</a>
            <a class="nav-link" href="<%= request.getContextPath() %>/addStudent.html">Add Student</a>
            <a class="nav-link" href="<%= request.getContextPath() %>/markAttendance.html">Mark Attendance</a>
            <a class="nav-link" href="<%= request.getContextPath() %>/dashboard">Dashboard</a>
            <a class="nav-link active" href="<%= request.getContextPath() %>/records">View Records</a>
        </nav>
    </aside>

    <main class="main-content">
        <div class="topbar">
            <div>
                <h1 class="page-title">Attendance History</h1>
                <p class="page-subtitle">Review all records, filter by student, and inspect attendance date-wise.</p>
            </div>
        </div>

        <section class="surface-card" style="margin-bottom: 20px;">
            <h3 class="section-title">Filters</h3>
            <form action="<%= request.getContextPath() %>/records" method="get" style="display: grid; grid-template-columns: 1fr 1fr 1fr auto; gap: 16px; align-items: end;">
                <div class="input-row">
                    <label for="studentId">Student</label>
                    <select id="studentId" name="studentId">
                        <option value="">All Students</option>
                        <% if (students != null) { %>
                            <% for (Student student : students) { %>
                                <option value="<%= student.getId() %>" <%= String.valueOf(student.getId()).equals(selectedStudentId) ? "selected" : "" %>>
                                    <%= student.getName() %> (<%= student.getUsn() %>)
                                </option>
                            <% } %>
                        <% } %>
                    </select>
                </div>
                <div class="input-row">
                    <label for="startDate">Start Date</label>
                    <input id="startDate" name="startDate" type="date" value="<%= startDate %>">
                </div>
                <div class="input-row">
                    <label for="endDate">End Date</label>
                    <input id="endDate" name="endDate" type="date" value="<%= endDate %>">
                </div>
                <div class="input-row" style="align-self: end;">
                    <button type="submit">Apply Filter</button>
                </div>
            </form>
        </section>

        <section class="surface-card">
            <h3 class="section-title">Attendance Records</h3>
            <% if (errorMessage != null) { %>
                <div class="alert alert-warning">Database error: <%= errorMessage %></div>
            <% } else { %>
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Student</th>
                            <th>USN</th>
                            <th>Date</th>
                            <th>Status</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% if (records == null || records.isEmpty()) { %>
                            <tr>
                                <td colspan="4" class="muted">No attendance history found for the selected filters.</td>
                            </tr>
                        <% } else { %>
                            <% for (AttendanceRecord record : records) { %>
                                <tr>
                                    <td><%= record.getStudentName() %></td>
                                    <td><%= record.getUsn() %></td>
                                    <td><%= record.getAttendanceDate() %></td>
                                    <td><%= record.getStatus() %></td>
                                </tr>
                            <% } %>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        </section>
    </main>
</div>
</body>
</html>
