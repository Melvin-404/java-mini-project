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
            <a class="nav-link" id="nav-add-student" href="<%= request.getContextPath() %>/addStudent.html">Add Student</a>
            <a class="nav-link" id="nav-mark-attendance" href="<%= request.getContextPath() %>/markAttendance.html">Mark Attendance</a>
            <a class="nav-link" href="<%= request.getContextPath() %>/dashboard">Dashboard</a>
            <a class="nav-link active" href="<%= request.getContextPath() %>/records">View Records</a>
            <a class="nav-link" href="<%= request.getContextPath() %>/LogoutServlet" style="margin-top: auto; color: var(--danger);">Logout</a>
        </nav>
        <div id="userInfo" style="margin-top: 20px; padding: 12px; background: var(--surface-soft); border-radius: 12px; font-size: 0.88rem;">
            <div id="usernameDisplay"></div>
            <div id="roleBadge" style="margin-top: 8px;"></div>
        </div>
    </aside>

    <main class="main-content">
        <header class="page-header">
            <h1>View Records</h1>
            <p>Browse complete attendance history</p>
        </header>
        <section class="grid-4">
            <div>
                <h1 class="page-title">Attendance History</h1>
                <p class="page-subtitle">Review all records, filter by student, and inspect attendance date-wise.</p>
            </div>
        </section>

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
                                <%
                                    String status = record.getStatus();
                                    String statusClass = "";
                                    if ("PRESENT".equals(status)) {
                                        statusClass = "badge badge-safe";
                                    } else if ("ABSENT".equals(status)) {
                                        statusClass = "badge badge-danger";
                                    } else if ("LATE".equals(status)) {
                                        statusClass = "badge badge-risk";
                                    }
                                %>
                                <tr>
                                    <td><%= record.getStudentName() %></td>
                                    <td><%= record.getUsn() %></td>
                                    <td><%= record.getAttendanceDate() %></td>
                                    <td><span class="<%= statusClass %>"><%= status %></span></td>
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

<script>
    async function checkRole() {
        try {
            const res = await fetch('<%= request.getContextPath() %>/SessionInfoServlet');
            const data = await res.json();
            window.userRole = data.role;
            window.username = data.username;
            applyRBAC(data.role, data.username);
        } catch (error) {
            console.error('Failed to fetch session info:', error);
        }
    }

    function applyRBAC(role, username) {
        // Display username and role badge
        const usernameDisplay = document.getElementById('usernameDisplay');
        const roleBadge = document.getElementById('roleBadge');
        
        if (username) {
            usernameDisplay.innerHTML = '<strong>' + username + '</strong>';
        }
        
        if (role) {
            const badgeColor = role === 'TEACHER' ? 'background: rgba(74, 144, 226, 0.16); color: #4a90e2;' : 'background: rgba(97, 112, 124, 0.16); color: #61707c;';
            roleBadge.innerHTML = '<span class="badge" style="' + badgeColor + '">' + role.charAt(0) + role.slice(1).toLowerCase() + '</span>';
        }
        
        // Hide sidebar navigation items for STUDENT
        if (role === 'STUDENT') {
            document.getElementById('nav-add-student').style.display = 'none';
            document.getElementById('nav-mark-attendance').style.display = 'none';
        }
    }

    checkRole();
</script>
</body>
</html>
