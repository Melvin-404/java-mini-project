package com.attendance.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    // Hardcoded credentials for demo
    private static final String TEACHER_USERNAME = "teacher";
    private static final String TEACHER_PASSWORD = "teacher123";
    private static final String STUDENT_USERNAME = "student";
    private static final String STUDENT_PASSWORD = "student123";
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String usn = request.getParameter("usn");
        
        // Validate credentials
        boolean isAuthenticated = false;
        
        if ("TEACHER".equals(role)) {
            if (TEACHER_USERNAME.equals(username) && TEACHER_PASSWORD.equals(password)) {
                isAuthenticated = true;
            }
        } else if ("STUDENT".equals(role)) {
            if (STUDENT_USERNAME.equals(username) && STUDENT_PASSWORD.equals(password)) {
                isAuthenticated = true;
                // Use provided USN or default to 22CS001 for demo
                if (usn == null || usn.trim().isEmpty()) {
                    usn = "22CS001";
                }
            }
        }
        
        if (isAuthenticated) {
            // Create session and store user info
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("role", role);
            
            if ("STUDENT".equals(role)) {
                session.setAttribute("usn", usn);
            }
            
            // Redirect to index.html after successful login
            response.sendRedirect("index.html");
        } else {
            // Redirect back to login page with error
            response.sendRedirect("login.html?error=true");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET requests to login page
        response.sendRedirect("login.html");
    }
}
