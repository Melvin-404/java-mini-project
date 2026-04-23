package com.attendance.servlet;

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.StudentDAO;
import com.attendance.model.AttendanceRecord;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecordsServlet extends HttpServlet {
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();
    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentIdParam = request.getParameter("studentId");
        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");

        try {
            List<AttendanceRecord> records;
            if (studentIdParam != null && !studentIdParam.isBlank()) {
                records = attendanceDAO.getAttendanceByStudent(Integer.parseInt(studentIdParam));
            } else {
                records = attendanceDAO.getAllAttendanceRecords();
            }

            LocalDate startDate = parseDate(startDateParam);
            LocalDate endDate = parseDate(endDateParam);
            List<AttendanceRecord> filteredRecords = new ArrayList<>();

            for (AttendanceRecord record : records) {
                boolean matchesStart = startDate == null || !record.getAttendanceDate().isBefore(startDate);
                boolean matchesEnd = endDate == null || !record.getAttendanceDate().isAfter(endDate);
                if (matchesStart && matchesEnd) {
                    filteredRecords.add(record);
                }
            }

            request.setAttribute("records", filteredRecords);
            request.setAttribute("students", studentDAO.getAllStudents());
            request.setAttribute("selectedStudentId", studentIdParam == null ? "" : studentIdParam);
            request.setAttribute("startDate", startDateParam == null ? "" : startDateParam);
            request.setAttribute("endDate", endDateParam == null ? "" : endDateParam);
            request.getRequestDispatcher("/viewRecords.jsp").forward(request, response);
        } catch (SQLException ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher("/viewRecords.jsp").forward(request, response);
        }
    }

    private LocalDate parseDate(String value) {
        try {
            return (value == null || value.isBlank()) ? null : LocalDate.parse(value);
        } catch (Exception ex) {
            return null;
        }
    }
}
