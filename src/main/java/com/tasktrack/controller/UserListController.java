package com.tasktrack.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import com.tasktrack.model.UserModel;
import com.tasktrack.service.UserService;

/**
 * Controller for displaying and managing users
 */
@WebServlet("/userList")
public class UserListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }
    
    /**
     * Handle GET requests to display user list
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {        
        try {
            String search = request.getParameter("search");
            String filter = request.getParameter("filter");
            
            List<UserModel> userList;
            
            if (search != null && !search.trim().isEmpty()) {
                userList = userService.searchUsersSafe(search);
                request.setAttribute("searchQuery", search);
            } else if (filter != null && !filter.trim().isEmpty() && !filter.equals("all")) {
                userList = userService.filterUsersByTypeSafe(filter);
                request.setAttribute("filterType", filter);
            } else {
                userList = userService.getAllUsersSafe();
            }
            
            request.setAttribute("userList", userList);
            
        } catch (Exception e) {
            System.err.println("Error in UserListController.doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Failed to retrieve user data: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/pages/userList.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests for search, filter, and actions
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if ("search".equals(action)) {
                String searchInput = request.getParameter("searchInput");
                List<UserModel> userList;
                if (searchInput != null && !searchInput.trim().isEmpty()) {
                    userList = userService.searchUsersSafe(searchInput);
                    request.setAttribute("searchQuery", searchInput);
                } else {
                    userList = userService.getAllUsersSafe();
                }
                request.setAttribute("userList", userList);
                
            } else if ("filter".equals(action)) {
                String filterType = request.getParameter("filterType");
                
                List<UserModel> userList;
                if (filterType != null && !filterType.equals("all")) {
                    userList = userService.filterUsersByTypeSafe(filterType);
                    request.setAttribute("filterType", filterType);
                } else {
                    userList = userService.getAllUsersSafe();
                }
                
                request.setAttribute("userList", userList);
                
            } 
            else if ("delete".equals(action)) {
                String userIdParam = request.getParameter("userId");
                
                if (userIdParam != null && !userIdParam.isEmpty()) {
                    try {
                        int userId = Integer.parseInt(userIdParam);
                        boolean success = userService.deleteUser(userId);
                        
                        if (success) {
                            request.setAttribute("success", "User deleted successfully");
                        } else {
                            request.setAttribute("error", "Failed to delete user");
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("error", "Invalid user ID");
                    }
                } else {
                    request.setAttribute("error", "User ID not provided");
                }
           
                List<UserModel> userList = userService.getAllUsersSafe();
                request.setAttribute("userList", userList);
            } 
            else {
                List<UserModel> userList = userService.getAllUsersSafe();
                request.setAttribute("userList", userList);
            }
        } catch (Exception e) {
            System.err.println("Error processing request: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error processing request: " + e.getMessage());
            
            List<UserModel> userList = userService.getAllUsersSafe();
            request.setAttribute("userList", userList);
        }
        
        request.getRequestDispatcher("/WEB-INF/pages/userList.jsp").forward(request, response);
    }
}