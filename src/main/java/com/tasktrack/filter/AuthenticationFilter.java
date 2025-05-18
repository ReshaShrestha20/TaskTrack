package com.tasktrack.filter;

import java.io.IOException;

import com.tasktrack.service.LoginService;
import com.tasktrack.util.CookiesUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Authentication filter that handles role-based access control.
 * This filter verifies user authentication and proper role authorization
 * for protected resources in the Task Track application.
 */
@WebFilter(urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {
    private static final String[] PUBLIC_URLS = {
        "/login", "/register", "/aboutUs", "/contactUs", "/css/", "/js/", "/resources/", "/images/","/logout"
    };
    
    // URLs that require admin role
    private static final String[] ADMIN_URLS = {
        "/adminProfile", "/adminDashboard", "/userList", "/editUser"
    };
    
    // URLs that require user role
    private static final String[] USER_URLS = {
        "/home", "/viewTask", "/userProfile", "/taskDataForm"
    };
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthenticationFilter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
                
        if (isPublicResource(path)) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("username") != null);
        if (!isLoggedIn) {
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }
        String userRole = getUserRole(httpRequest);
        if (userRole != null) {
            CookiesUtil.addCookie(httpResponse, "role", userRole, 30 * 24 * 60 * 60);
        }
        if (path.equals("/") || path.isEmpty()) {
            if ("admin".equals(userRole)) {
                httpResponse.sendRedirect(contextPath + "/adminDashboard");
                return;
            } else {
                httpResponse.sendRedirect(contextPath + "/home");
                return;
            }
        }
        if ("admin".equals(userRole)) {
            if (!isAdminResource(path)) {
                httpResponse.sendRedirect(contextPath + "/adminDashboard");
                return;
            }
        } else {
            if (!isUserResource(path)) {
                httpResponse.sendRedirect(contextPath + "/home");
                return;
            }
        }
        chain.doFilter(request, response);
    }
    
    /**
     * Checks if the requested resource is public (accessible without authentication).
     * 
     * @param uri The requested URI path
     * @return true if the resource is public, false otherwise
     */
    private boolean isPublicResource(String uri) {
        for (String publicUrl : PUBLIC_URLS) {
            if (uri.equals(publicUrl) || uri.startsWith(publicUrl)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if the requested resource requires admin role.
     * 
     * @param uri The requested URI path
     * @return true if the resource requires admin role, false otherwise
     */
    private boolean isAdminResource(String uri) {
        for (String adminUrl : ADMIN_URLS) {
            if (uri.equals(adminUrl) || uri.startsWith(adminUrl)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if the requested resource is for regular users.
     * 
     * @param uri The requested URI path
     * @return true if the resource is for regular users, false otherwise
     */
    private boolean isUserResource(String uri) {
        for (String userUrl : USER_URLS) {
            if (uri.equals(userUrl) || uri.startsWith(userUrl)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets the role of the current user from cookies or session.
     * First checks cookies, then falls back to determining role from username via database.
     * 
     * @param request The HTTP request
     * @return The role of the user ("admin" or "user"), or null if not determined
     */
    private String getUserRole(HttpServletRequest request) {
        Cookie roleCookie = CookiesUtil.getCookie(request, "role");
        if (roleCookie != null && roleCookie.getValue() != null) {
            String cookieValue = roleCookie.getValue();
            return cookieValue;
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = (String) session.getAttribute("username");
            if (username != null) {
            	LoginService loginService = new LoginService();
                String userRole = loginService.getUserRole(username);
                return (userRole != null && userRole.equalsIgnoreCase("Admin")) ? "admin" : "user";
            }
        }
        return null;
    }
    
    @Override
    public void destroy() {
        System.out.println("AuthenticationFilter destroyed");
    }
}