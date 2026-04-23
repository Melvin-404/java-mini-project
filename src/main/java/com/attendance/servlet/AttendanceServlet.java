package com.attendance.servlet;

import com.attendance.dao.AttendanceDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;

public class AttendanceServlet extends HttpServlet {
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message;
        try {
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            LocalDate attendanceDate = LocalDate.parse(request.getParameter("attendanceDate"));
            String status = request.getParameter("status");

            attendanceDAO.markAttendance(studentId, attendanceDate, status);
            message = "Attendance saved successfully.";
        } catch (SQLException ex) {
            message = "Could not save attendance: " + ex.getMessage();
        } catch (Exception ex) {
            message = "Invalid attendance input.";
        }

        response.sendRedirect(request.getContextPath() + "/markAttendance.html?message=" +
                URLEncoder.encode(message, StandardCharsets.UTF_8));
    }
}
