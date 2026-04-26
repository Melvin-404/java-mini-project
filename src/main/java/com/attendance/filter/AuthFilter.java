package com.attendance.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String relativePath = path.substring(contextPath.length());
        
        // Allow access to login page and login servlet without authentication
        if (relativePath.equals("/login.html") || 
            relativePath.equals("/LoginServlet") ||
            relativePath.equals("/LogoutServlet") ||
            relativePath.equals("/SessionInfoServlet") ||
            relativePath.startsWith("/assets/") ||
            relativePath.endsWith(".css") ||
            relativePath.endsWith(".js")) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is authenticated
        HttpSession session = httpRequest.getSession(false);
        
        if (session == null || session.getAttribute("role") == null) {
            // Not authenticated, redirect to login
            httpResponse.sendRedirect(contextPath + "/login.html");
            return;
        }
        
        // Add cache prevention headers
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);
        
        // User is authenticated, continue with the request
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}
