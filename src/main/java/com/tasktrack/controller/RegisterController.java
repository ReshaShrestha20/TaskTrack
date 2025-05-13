package com.tasktrack.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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

            req.getParameterMap().forEach((key, value) -> {
            });
            
            UserModel userModel = extractUserModel(req);
            
            Boolean isAdded = registerService.addUser(userModel);

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
        LocalDate dob;
        try {
            if (dobStr == null || dobStr.isEmpty()) {
                throw new Exception("Date of birth is required");
            }
            
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                sdf.setLenient(false); 
                Date date = sdf.parse(dobStr);
                dob = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (Exception e1) {
                
                String[] dateParts = dobStr.split("/");
                if (dateParts.length != 3) {
                    throw new Exception("Date must be in MM/DD/YYYY format");
                }
                
                int month = Integer.parseInt(dateParts[0]);
                int day = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                
                dob = LocalDate.of(year, month, day);
            }
        } catch (Exception e) {
            System.err.println("Error parsing date: " + dobStr + " - " + e.getMessage());
            throw new Exception("Invalid date format. Please use MM/DD/YYYY format.");
        }
        
        String gender = req.getParameter("gender");
        String email = req.getParameter("email");
        String phoneNumber = req.getParameter("phoneNumber");
        String userType = req.getParameter("userType");
        
        String projectName = req.getParameter("subject");
        if (projectName == null || projectName.isEmpty()) {
            projectName = "Dynamic Web Application"; 
        }

        String password = req.getParameter("password");
        String retypePassword = req.getParameter("retypePassword");

        if (password == null || !password.equals(retypePassword)) {
            throw new Exception("Passwords do not match or are invalid.");
        }

        String encryptedPassword = PasswordUtil.encrypt(username, password);
        
        ProjectModel projectModel = new ProjectModel(projectName);
        UserModel user = new UserModel(firstName, lastName, username, dob,
                gender, email, phoneNumber, encryptedPassword, userType, projectModel);
        
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