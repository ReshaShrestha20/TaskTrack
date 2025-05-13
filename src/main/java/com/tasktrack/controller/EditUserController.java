package com.tasktrack.controller;

import java.io.IOException;
import java.time.LocalDate;

import com.tasktrack.model.UserModel;
import com.tasktrack.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller for handling user edit operations
 */
@WebServlet("/editUser")
public class EditUserController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        userService = new UserService();
        System.out.println("EditUserController initialized with URL pattern: /editUser");
    }
    
    /**
     * Handle GET requests - display user edit form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userIdParam = request.getParameter("id");
            System.out.println("GET request received for editUser with ID: " + userIdParam);
            
            if (userIdParam != null && !userIdParam.isEmpty()) {
                try {
                    int userId = Integer.parseInt(userIdParam);
                    UserModel user = userService.getUserById(userId);
                    
                    if (user != null) {
                        request.setAttribute("user", user);
                        System.out.println("User loaded successfully: " + user.getFirstName() + " " + user.getLastName());
                    } else {
                        request.setAttribute("error", "User not found. ID: " + userId);
                        System.out.println("User not found with ID: " + userId);
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid user ID format.");
                    System.out.println("Invalid user ID format: " + userIdParam);
                }
            } else {
                request.setAttribute("error", "No user ID provided.");
                System.out.println("No user ID provided in request");
            }
        } catch (Exception e) {
            System.err.println("Error in EditUserController.doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Failed to load user data: " + e.getMessage());
        }
        
        System.out.println("Forwarding to editUser.jsp");
        request.getRequestDispatcher("/WEB-INF/pages/editUser.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - process form submission
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        System.out.println("POST request received for editUser with action: " + action);
        
        if ("update".equals(action)) {
            try {
                UserModel updatedUser = extractUserFromRequest(request);
                
                String validationError = validateUserData(updatedUser);
                if (validationError != null) {
                    System.out.println("Validation error: " + validationError);
                    request.setAttribute("error", validationError);
                    request.setAttribute("user", updatedUser);
                    request.getRequestDispatcher("/WEB-INF/pages/editUser.jsp").forward(request, response);
                    return;
                }
                
                boolean success = userService.updateUser(updatedUser);
                
                if (success) {
                    System.out.println("User updated successfully: " + updatedUser.getId());
                    request.getSession().setAttribute("successMessage", "User updated successfully!");
                    response.sendRedirect("userList");
                } else {
                    System.out.println("Failed to update user: " + updatedUser.getId());
                    request.setAttribute("error", "Failed to update user. Please try again.");
                    request.setAttribute("user", updatedUser);
                    request.getRequestDispatcher("/WEB-INF/pages/editUser.jsp").forward(request, response);
                }
            } catch (Exception e) {
                System.err.println("Error updating user: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "Error updating user: " + e.getMessage());
                try {
                    String userIdParam = request.getParameter("userId");
                    if (userIdParam != null && !userIdParam.isEmpty()) {
                        int userId = Integer.parseInt(userIdParam);
                        UserModel user = userService.getUserById(userId);
                        if (user != null) {
                            request.setAttribute("user", user);
                        }
                    }
                } catch (Exception ex) {
                }
                
                request.getRequestDispatcher("/WEB-INF/pages/editUser.jsp").forward(request, response);
            }
        } else {
            System.out.println("Invalid action or no action specified. Redirecting to user list.");
            response.sendRedirect("userList");
        }
    }
    
    /**
     * Extract user data from request parameters
     */
    private UserModel extractUserFromRequest(HttpServletRequest request) {
        UserModel user = new UserModel();
        
        String userIdParam = request.getParameter("userId");
        if (userIdParam != null && !userIdParam.isEmpty()) {
            try {
                user.setId(Integer.parseInt(userIdParam));
            } catch (NumberFormatException e) {
                System.err.println("Invalid user ID: " + userIdParam);
            }
        }
        
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setUserName(request.getParameter("userName"));
        user.setEmail(request.getParameter("email"));
        user.setNumber(request.getParameter("phoneNumber"));
        user.setGender(request.getParameter("gender"));
        user.setUserType(request.getParameter("userType"));
        
        String password = request.getParameter("password");
        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(password);
        }
        
        String dobString = request.getParameter("dob");
        if (dobString != null && !dobString.isEmpty()) {
            try {
                user.setDob(LocalDate.parse(dobString));
            } catch (Exception e) {
                System.err.println("Error parsing date of birth: " + e.getMessage());
            }
        }
        
        return user;
    }
    
    /**
     * Validate user data
     * @return null if valid, error message if invalid
     */
    private String validateUserData(UserModel user) {
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            return "First name is required";
        }
        
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            return "Last name is required";
        }
        
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            return "Username is required";
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return "Email is required";
        }
        
        if (!user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return "Invalid email format";
        }
        
        if (user.getNumber() != null && !user.getNumber().isEmpty() && 
            !user.getNumber().matches("^[0-9]{10}$")) {
            return "Phone number should be 10 digits";
        }
        
        return null; 
    }
}