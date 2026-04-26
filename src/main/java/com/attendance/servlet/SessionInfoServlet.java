package com.attendance.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionInfoServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        
        if (session == null) {
            out.print("{\"username\":null,\"role\":null,\"usn\":null}");
            return;
        }
        
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        String usn = (String) session.getAttribute("usn");
        
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"username\":").append(username != null ? "\"" + username + "\"" : "null").append(",");
        json.append("\"role\":").append(role != null ? "\"" + role + "\"" : "null").append(",");
        json.append("\"usn\":").append(usn != null ? "\"" + usn + "\"" : "null");
        json.append("}");
        
        out.print(json.toString());
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
