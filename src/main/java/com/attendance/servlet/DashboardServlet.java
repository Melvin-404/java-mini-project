package com.attendance.servlet;

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.StudentDAO;
import com.attendance.model.Student;
import com.attendance.model.StudentDashboard;
import com.attendance.service.AnalyticsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardServlet extends HttpServlet {
    private final StudentDAO studentDAO = new StudentDAO();
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();
    private final AnalyticsService analyticsService = new AnalyticsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int futureClasses = parseFutureClasses(request.getParameter("futureClasses"));

        try {
            List<Student> students = studentDAO.getAllStudents();
            List<StudentDashboard> dashboards = new ArrayList<>();
            for (Student student : students) {
                dashboards.add(analyticsService.buildDashboard(
                        student,
                        attendanceDAO.getAttendanceByStudent(student.getId()),
                        futureClasses
                ));
            }
            request.setAttribute("dashboards", dashboards);
            request.setAttribute("futureClasses", futureClasses);
            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
        } catch (SQLException ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
        }
    }

    private int parseFutureClasses(String value) {
        try {
            int parsed = Integer.parseInt(value);
            return Math.max(1, parsed);
        } catch (Exception ex) {
            return 5;
        }
    }
}
