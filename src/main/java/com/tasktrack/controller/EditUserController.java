package com.tasktrack.controller;

import java.io.IOException;
import java.time.LocalDate;

import com.tasktrack.model.UserModel;
import com.tasktrack.service.UserService;
import com.tasktrack.util.ValidationUtil;

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
    private ValidationUtil validationUtil;
    
    @Override
    public void init() throws ServletException {
        userService = new UserService();
        validationUtil = new ValidationUtil();
    }
    
    /**
     * Handle GET requests - display user edit form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userIdParam = request.getParameter("id");
            
            if (!validationUtil.isNullOrEmpty(userIdParam)) {
                try {
                    int userId = Integer.parseInt(userIdParam);
                    UserModel user = userService.getUserById(userId);
                    
                    if (user != null) {
                        request.setAttribute("user", user);
                    } else {
                        request.setAttribute("error", "User not found. ID: " + userId);
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid user ID format.");
                }
            } else {
                request.setAttribute("error", "No user ID provided.");
            }
        } catch (Exception e) {
            System.err.println("Error in EditUserController.doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Failed to load user data: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/pages/editUser.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - process form submission
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("update".equals(action)) {
            try {
                UserModel updatedUser = extractUserFromRequest(request);
                String validationError = validateUserData(updatedUser);
                if (validationError != null) {
                    request.setAttribute("error", validationError);
                    request.setAttribute("user", updatedUser);
                    request.getRequestDispatcher("/WEB-INF/pages/editUser.jsp").forward(request, response);
                    return;
                }
                boolean success = userService.updateUser(updatedUser);
                if (success) {
                    request.getSession().setAttribute("successMessage", "User updated successfully!");
                    response.sendRedirect("userList");
                } else {
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
                    if (!validationUtil.isNullOrEmpty(userIdParam)) {
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
            response.sendRedirect("userList");
        }
    }
    
    /**
     * Extract user data from request parameters
     */
    private UserModel extractUserFromRequest(HttpServletRequest request) {
        UserModel user = new UserModel();
        
        String userIdParam = request.getParameter("userId");
        if (!validationUtil.isNullOrEmpty(userIdParam)) {
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
        if (!validationUtil.isNullOrEmpty(password)) {
            user.setPassword(password);
        }
        String dobString = request.getParameter("dob");
        if (!validationUtil.isNullOrEmpty(dobString)) {
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
        if (validationUtil.isNullOrEmpty(user.getFirstName())) {
            return "First name is required";
        }
        
        if (validationUtil.isNullOrEmpty(user.getLastName())) {
            return "Last name is required";
        }
        
        if (validationUtil.isNullOrEmpty(user.getUserName())) {
            return "Username is required";
        }
        
        if (validationUtil.isNullOrEmpty(user.getEmail())) {
            return "Email is required";
        }
        
        if (!validationUtil.isValidEmail(user.getEmail())) {
            return "Invalid email format";
        }
        
        if (user.getNumber() != null && !user.getNumber().isEmpty() && 
            !validationUtil.isValidPhoneNumber(user.getNumber())) {
            return "Phone number should be 10 digits and start with 98";
        }
        
        return null; 
    }
}