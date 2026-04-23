package com.attendance.servlet;

import com.attendance.dao.StudentDAO;
import com.attendance.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class StudentsApiServlet extends HttpServlet {
    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<Student> students = studentDAO.getAllStudents();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                if (i > 0) {
                    json.append(",");
                }
                json.append("{")
                        .append("\"id\":").append(student.getId()).append(",")
                        .append("\"name\":\"").append(escapeJson(student.getName())).append("\",")
                        .append("\"usn\":\"").append(escapeJson(student.getUsn())).append("\"")
                        .append("}");
            }
            json.append("]");
            response.getWriter().write(json.toString());
        } catch (SQLException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + escapeJson(ex.getMessage()) + "\"}");
        }
    }

    private String escapeJson(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
