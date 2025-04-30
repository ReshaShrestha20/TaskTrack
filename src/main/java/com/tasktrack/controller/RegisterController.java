package com.tasktrack.controller;

import java.io.IOException;
import java.time.LocalDate;

import com.tasktrack.model.UserModel;
import com.tasktrack.model.ProjectModel;
import com.tasktrack.service.RegisterService;
import com.tasktrack.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * RegisterController handles user registration requests and processes form
 * submissions. It also manages account creation.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/register" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final RegisterService registerService = new RegisterService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Starting registration process");
            UserModel userModel = extractUserModel(req);
            System.out.println("User model created: " + userModel.getUserName());
            
            Boolean isAdded = registerService.addUser(userModel);
            System.out.println("Registration result: " + isAdded);

            if (isAdded == null) {
                handleError(req, resp, "Our server is under maintenance. Please try again later!");
            } 
            else if (isAdded) {
                handleSuccess(req, resp, "Your account is successfully created!", "/WEB-INF/pages/login.jsp");
            } 
            else {
                handleError(req, resp, "Could not register your account. Please try again later!");
            }
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            handleError(req, resp, "An unexpected error occurred: " + e.getMessage());
        }
    }

    private UserModel extractUserModel(HttpServletRequest req) throws Exception {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String username = req.getParameter("username");
        
        String dobStr = req.getParameter("dob");
        LocalDate dob = LocalDate.parse(dobStr);
        
        String gender = req.getParameter("gender");
        String email = req.getParameter("email");
        String phoneNumber = req.getParameter("phoneNumber");
        String userType = req.getParameter("userType");
        String projectName = req.getParameter("subject");
        if (projectName == null || projectName.isEmpty()) {
            projectName = "Default";
        }

        String password = req.getParameter("password");
        String retypePassword = req.getParameter("retypePassword");

        if (password == null || !password.equals(retypePassword)) {
            throw new Exception("Passwords do not match or are invalid.");
        }

        String encryptedPassword = PasswordUtil.encrypt(username, password);
        
        ProjectModel projectModel = new ProjectModel(projectName);
        UserModel user = new UserModel(firstName, lastName, username, dob,
                gender, email, phoneNumber, encryptedPassword, projectModel);
        
        return user;
    }

    private void handleSuccess(HttpServletRequest req, HttpServletResponse resp, String message, String redirectPage)
            throws ServletException, IOException {
        req.setAttribute("success", message);
        req.getRequestDispatcher(redirectPage).forward(req, resp);
    }

    private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("error", message);
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }
}