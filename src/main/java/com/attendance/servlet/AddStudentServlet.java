package com.attendance.servlet;

import com.attendance.dao.StudentDAO;
import com.attendance.model.Student;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class AddStudentServlet extends HttpServlet {
    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Server-side RBAC check: Only TEACHER can add students
        HttpSession session = request.getSession(false);
        if (session == null || !"TEACHER".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/access-denied.html");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String usn = request.getParameter("usn");

        String message;
        try {
            if (name == null || name.isBlank() || usn == null || usn.isBlank()) {
                message = "Name and USN are required.";
            } else {
                studentDAO.addStudent(new Student(0, name.trim(), usn.trim().toUpperCase()));
                message = "Student added successfully.";
            }
        } catch (SQLException ex) {
            message = "Could not add student: " + ex.getMessage();
        }

        response.sendRedirect(request.getContextPath() + "/addStudent.html?message=" +
                URLEncoder.encode(message, StandardCharsets.UTF_8));
    }
}
