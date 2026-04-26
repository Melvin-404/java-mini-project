<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.attendance.model.StudentDashboard" %>
<%@ page import="com.attendance.model.AttendanceRecord" %>
<%@ page import="com.attendance.model.WeeklySummary" %>
<%@ page import="com.attendance.model.MonthlySummary" %>
<%
    List<StudentDashboard> dashboards = (List<StudentDashboard>) request.getAttribute("dashboards");
    Integer futureClasses = (Integer) request.getAttribute("futureClasses");
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (futureClasses == null) {
        futureClasses = 5;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Attendance Dashboard</title>
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
                <p>Analytics, warnings, and predictions.</p>
            </div>
        </div>
        <nav class="nav-menu">
            <a class="nav-link" href="<%= request.getContextPath() %>/index.html">Home</a>
            <a class="nav-link" id="nav-add-student" href="<%= request.getContextPath() %>/addStudent.html">Add Student</a>
            <a class="nav-link" id="nav-mark-attendance" href="<%= request.getContextPath() %>/markAttendance.html">Mark Attendance</a>
            <a class="nav-link active" href="<%= request.getContextPath() %>/dashboard">Dashboard</a>
            <a class="nav-link" href="<%= request.getContextPath() %>/records">View Records</a>
            <a class="nav-link" href="<%= request.getContextPath() %>/LogoutServlet" style="margin-top: auto; color: var(--danger);">Logout</a>
        </nav>
        <div id="userInfo" style="margin-top: 20px; padding: 12px; background: var(--surface-soft); border-radius: 12px; font-size: 0.88rem;">
            <div id="usernameDisplay"></div>
            <div id="roleBadge" style="margin-top: 8px;"></div>
        </div>
    </aside>

    <main class="main-content">
        <header class="page-header">
            <h1>Dashboard</h1>
            <p>Analytics, warnings, and predictions for student attendance</p>
        </header>
        <section class="grid-4">
            <div>
                <h1 class="page-title">Analytics Dashboard</h1>
                <p class="page-subtitle">
                    Review attendance percentage, required classes to reach 75%, bunk buffer, next-class impact,
                    and recent history for every student.
                </p>
            </div>
            <form action="<%= request.getContextPath() %>/dashboard" method="get" class="surface-card" style="padding: 14px; min-width: 260px; display: grid; gap: 12px;">
                <div class="input-row">
                    <label for="futureClasses">Future prediction classes</label>
                    <input id="futureClasses" name="futureClasses" type="number" min="1" value="<%= futureClasses %>">
                </div>
                <div style="margin-top: 12px;">
                    <button type="submit">Update Prediction</button>
                </div>
            </form>
        </div>

        <% if (errorMessage != null) { %>
            <div class="surface-card">
                <div class="alert alert-warning">Database error: <%= errorMessage %></div>
            </div>
        <% } else if (dashboards == null || dashboards.isEmpty()) { %>
            <div class="surface-card empty-state">
                No students found yet. Add students first and then begin marking attendance.
            </div>
        <% } else { %>
            <div class="dashboard-grid">
                <% for (StudentDashboard item : dashboards) { %>
                    <section class="surface-card">
                        <div class="topbar" style="margin-bottom: 16px;">
                            <div>
                                <h3 class="section-title" style="margin-bottom: 4px;"><%= item.getStudent().getName() %></h3>
                                <div class="muted"><%= item.getStudent().getUsn() %></div>
                            </div>
                            <%
                                String badgeClass = "badge badge-danger";
                                if ("SAFE".equals(item.getDangerLevel())) {
                                    badgeClass = "badge badge-safe";
                                } else if ("RISK".equals(item.getDangerLevel())) {
                                    badgeClass = "badge badge-risk";
                                }
                            %>
                            <span class="<%= badgeClass %>"><%= item.getDangerLevel() %></span>
                        </div>

                        <div class="grid-2">
                            <div class="metric-card">
                                <div class="metric-label">Attendance Percentage</div>
                                <p class="metric-value"><%= item.getAttendancePercentage() %>%</p>
                                <div class="progress-track">
                                    <div class="progress-fill" style="width: <%= Math.min(item.getAttendancePercentage(), 100.0) %>%;"></div>
                                </div>
                            </div>
                            <div class="metric-card">
                                <div class="metric-label">Required Classes</div>
                                <p class="metric-value"><%= item.getRequiredClasses() %></p>
                                <p class="muted">You need to attend next <%= item.getRequiredClasses() %> classes.</p>
                            </div>
                            <div class="metric-card">
                                <div class="metric-label">Safe Bunk</div>
                                <p class="metric-value"><%= item.getSafeBunk() %></p>
                                <p class="muted">You can miss <%= item.getSafeBunk() %> more classes.</p>
                            </div>
                            <div class="metric-card">
                                <div class="metric-label">Alerts</div>
                                <% if (item.getAttendancePercentage() < 75.0) { %>
                                    <div class="alert alert-warning">Warning: <%= item.getWarningMessage() %></div>
                                <% } else { %>
                                    <div class="alert alert-safe"><%= item.getWarningMessage() %></div>
                                <% } %>
                            </div>
                        </div>

                        <div class="grid-2" style="margin-top: 18px;">
                            <div class="surface-card" style="padding: 18px;">
                                <h4 class="section-title">Advisor Summary</h4>
                                <div class="info-list">
                                    <div class="info-line"><span>Your attendance is</span><strong><%= item.getAttendancePercentage() %>%</strong></div>
                                    <div class="info-line"><span>If next class is missed</span><strong><%= item.getNextClassMissedPercentage() %>%</strong></div>
                                    <div class="info-line"><span>If next <%= item.getFuturePredictionClasses() %> classes are attended</span><strong><%= item.getFuturePredictionPercentage() %>%</strong></div>
                                    <div class="info-line"><span>Attendance streak</span><strong><%= item.getAttendanceStreak() %></strong></div>
                                    <div class="info-line"><span>Late attendance count</span><strong><%= item.getLateCount() %></strong></div>
                                    <div class="info-line"><span>Consistency score</span><strong><%= item.getConsistencyScore() %>/100</strong></div>
                                </div>
                            </div>

                            <div class="surface-card" style="padding: 18px;">
                                <h4 class="section-title">Class Breakdown</h4>
                                <div class="info-list">
                                    <div class="info-line"><span>Total classes</span><strong><%= item.getTotalClasses() %></strong></div>
                                    <div class="info-line"><span>Present classes</span><strong><%= item.getPresentCount() %></strong></div>
                                    <div class="info-line"><span>Late classes</span><strong><%= item.getLateCount() %></strong></div>
                                    <div class="info-line"><span>Absent classes</span><strong><%= item.getAbsentCount() %></strong></div>
                                    <div class="info-line"><span>Attended classes</span><strong><%= item.getAttendedClasses() %></strong></div>
                                </div>
                            </div>
                        </div>

                        <div class="grid-2" style="margin-top: 18px;">
                            <div class="surface-card" style="padding: 18px;">
                                <h4 class="section-title">Last 5 Attendance Records</h4>
                                <div class="table-wrap">
                                    <table>
                                        <thead>
                                        <tr>
                                            <th>Date</th>
                                            <th>Status</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <% if (item.getLastFiveRecords().isEmpty()) { %>
                                            <tr><td colspan="2" class="muted">No attendance records available.</td></tr>
                                        <% } else { %>
                                            <% for (AttendanceRecord record : item.getLastFiveRecords()) { %>
                                                <tr>
                                                    <td><%= record.getAttendanceDate() %></td>
                                                    <td><%= record.getStatus() %></td>
                                                </tr>
                                            <% } %>
                                        <% } %>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <div class="surface-card" style="padding: 18px;">
                                <h4 class="section-title">Weekly Summary</h4>
                                <div class="table-wrap">
                                    <table>
                                        <thead>
                                        <tr>
                                            <th>Week</th>
                                            <th>Attended</th>
                                            <th>Total</th>
                                            <th>%</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <% if (item.getWeeklySummaries().isEmpty()) { %>
                                            <tr><td colspan="4" class="muted">No weekly summary yet.</td></tr>
                                        <% } else { %>
                                            <% for (WeeklySummary summary : item.getWeeklySummaries()) { %>
                                                <tr>
                                                    <td><%= summary.getLabel() %></td>
                                                    <td><%= summary.getAttended() %></td>
                                                    <td><%= summary.getTotal() %></td>
                                                    <td><%= summary.getPercentage() %>%</td>
                                                </tr>
                                            <% } %>
                                        <% } %>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                        <div class="surface-card" style="padding: 18px; margin-top: 18px;">
                            <h4 class="section-title">Monthly Summary</h4>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                    <tr>
                                        <th>Month</th>
                                        <th>Attended</th>
                                        <th>Total</th>
                                        <th>%</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <% if (item.getMonthlySummaries().isEmpty()) { %>
                                        <tr><td colspan="4" class="muted">No monthly summary yet.</td></tr>
                                    <% } else { %>
                                        <% for (MonthlySummary summary : item.getMonthlySummaries()) { %>
                                            <tr>
                                                <td><%= summary.getLabel() %></td>
                                                <td><%= summary.getAttended() %></td>
                                                <td><%= summary.getTotal() %></td>
                                                <td><%= summary.getPercentage() %>%</td>
                                            </tr>
                                        <% } %>
                                    <% } %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </section>
                <% } %>
            </div>
        <% } %>
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
