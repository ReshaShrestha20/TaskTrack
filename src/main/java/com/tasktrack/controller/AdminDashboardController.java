package com.tasktrack.controller;

import java.io.IOException;
import java.util.List;

import com.tasktrack.model.UserModel;
import com.tasktrack.service.AdminDashboardService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = "/adminDashboard")
public class AdminDashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private AdminDashboardService adminDashboardService;
    
    @Override
    public void init() throws ServletException {
        this.adminDashboardService = new AdminDashboardService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int userCount = adminDashboardService.getUserCountSafe();
            int adminCount = adminDashboardService.getAdminCountSafe();
            int projectCount = adminDashboardService.getProjectCountSafe();
            int taskCount = adminDashboardService.getTaskCountSafe();
            
            List<UserModel> recentUsers = adminDashboardService.getRecentUsersSafe(5);
            
            request.setAttribute("userCount", userCount);
            request.setAttribute("adminCount", adminCount);
            request.setAttribute("projectCount", projectCount);
            request.setAttribute("taskCount", taskCount);
            request.setAttribute("recentUsers", recentUsers);
            
        } catch (Exception e) {
            request.setAttribute("dbError", "Failed to retrieve dashboard data: " + e.getMessage());
            
            request.setAttribute("userCount", 105);
            request.setAttribute("adminCount", 20);
            request.setAttribute("projectCount", 12);
            request.setAttribute("taskCount", 80);
            
            e.printStackTrace();
        }
        
        request.getRequestDispatcher("WEB-INF/pages/adminDashboard.jsp").forward(request, response);
    }
}