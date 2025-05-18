package com.tasktrack.controller;

import java.io.IOException;
import java.time.LocalDate;

import com.tasktrack.model.UserModel;
import com.tasktrack.model.ProjectModel;
import com.tasktrack.service.RegisterService;
import com.tasktrack.util.DateUtil;
import com.tasktrack.util.ImageUtil;
import com.tasktrack.util.PasswordUtil;
import com.tasktrack.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * RegisterController handles user registration requests and processes form
 * submissions. It also manages account creation and image uploads.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/register" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final RegisterService registerService = new RegisterService();
    private final ImageUtil imageUtil = new ImageUtil();
    private final ValidationUtil validator = new ValidationUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.getParameterMap().forEach((key, value) -> {
                if (!key.contains("password") && !key.contains("Password")) {
                    System.out.println(key + ": " + (value != null && value.length > 0 ? value[0] : "null"));
                } else {
                    System.out.println(key + ": [PROTECTED]");
                }
            });
            
            // Validate all form fields
            validateRegistrationForm(req);
            
            // Check for duplicate username and email
            String username = req.getParameter("username");
            String email = req.getParameter("email");
            
            if (registerService.usernameExists(username)) {
                handleError(req, resp, "Username already exists. Please choose a different username.");
                return;
            }
            
            if (registerService.emailExists(email)) {
                handleError(req, resp, "Email already exists. Please use a different email address.");
                return;
            }
            
            Part imagePart = req.getPart("image");
            if (imagePart != null && imagePart.getSize() > 0) {
                if (!imageUtil.isValidImageExtension(imagePart)) {
                    handleError(req, resp, "Invalid image format. Only jpg, jpeg, png, and gif are allowed.");
                    return;
                }
            } else {
                System.out.println("No image uploaded or image size is 0");
            }
            
            UserModel userModel = extractUserModel(req);
            
            Boolean isAdded = registerService.addUser(userModel);

            if (isAdded == null) {
                System.err.println("Database operation returned null - connection issue");
                handleError(req, resp, "Our server is under maintenance. Please try again later!");
            } 
            else if (isAdded) {
                if (imagePart != null && imagePart.getSize() > 0) {
                    boolean imageUploaded = imageUtil.uploadImage(imagePart, "user", req.getServletContext());
                    if (!imageUploaded) {
                        handleSuccess(req, resp, "Your account is created, but we couldn't upload your profile image.", "/WEB-INF/pages/login.jsp");
                        return;
                    }
                }
                handleSuccess(req, resp, "Your account is successfully created!", "/WEB-INF/pages/login.jsp");
            } 
            else {
                System.err.println("Database operation returned false - insert failed");
                handleError(req, resp, "Could not register your account. Please try again later!");
            }
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            handleError(req, resp, "An unexpected error occurred: " + e.getMessage());
        }
    }
    
    private void validateRegistrationForm(HttpServletRequest req) throws Exception {
        // First name validation
        String firstName = req.getParameter("firstName");
        if (validator.isNullOrEmpty(firstName)) {
            throw new Exception("First name is required.");
        }
        if (validator.hasSpecialCharacters(firstName)) {
            throw new Exception("First name cannot contain special characters or numbers.");
        }
        
        // Last name validation
        String lastName = req.getParameter("lastName");
        if (validator.isNullOrEmpty(lastName)) {
            throw new Exception("Last name is required.");
        }
        if (validator.hasSpecialCharacters(lastName)) {
            throw new Exception("Last name cannot contain special characters or numbers.");
        }
        
        // Username validation
        String username = req.getParameter("username");
        if (validator.isNullOrEmpty(username)) {
            throw new Exception("Username is required.");
        }
        if (!validator.isAlphanumericStartingWithLetter(username)) {
            throw new Exception("Username must start with a letter and contain only letters and numbers.");
        }
        
        // Email validation
        String email = req.getParameter("email");
        if (!validator.isValidEmail(email)) {
            throw new Exception("Please enter a valid email address.");
        }
        
        // Phone number validation
        String phoneNumber = req.getParameter("phoneNumber");
        if (!validator.isValidPhoneNumber(phoneNumber)) {
            throw new Exception("Phone number must be 10 digits.");
        }
        
        // DOB validation
        String dobStr = req.getParameter("dob");
        if (validator.isNullOrEmpty(dobStr)) {
            throw new Exception("Date of birth is required.");
        }
        
        LocalDate dob;
        try {
            dob = DateUtil.parseDate(dobStr);
            
            // Check if DOB is in the future
            if (validator.isFutureDate(dob)) {
                throw new Exception("Date of birth cannot be in the future.");
            }
            
            // Check if age is at least 16
            if (!validator.isAgeAtLeast16(dob)) {
                throw new Exception("You must be at least 16 years old to register.");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("future")) {
                throw e; // Re-throw our custom future date message
            }
            throw new Exception("Please enter a valid date of birth in the format MM/DD/YYYY.");
        }
        
        // Password validation
        String password = req.getParameter("password");
        String retypePassword = req.getParameter("retypePassword");
        
        if (validator.isNullOrEmpty(password)) {
            throw new Exception("Password is required.");
        }
        
        /*
        // Uncomment this if you want strict password validation
        if (!validator.isValidPassword(password)) {
            throw new Exception("Password must contain at least one capital letter, one number, and one symbol.");
        }
        */
        
        if (!validator.doPasswordsMatch(password, retypePassword)) {
            throw new Exception("Passwords do not match.");
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
            dob = DateUtil.parseDate(dobStr);
            if (dob.equals(LocalDate.now()) && !dobStr.contains(LocalDate.now().toString())) {
                throw new Exception("Could not parse the provided date");
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
        String imageUrl = "default-avatar.png"; // Default image
        try {
            Part imagePart = req.getPart("image");
            if (imagePart != null && imagePart.getSize() > 0) {
                imageUrl = imageUtil.getImageNameFromPart(imagePart);
            }
        } catch (Exception e) {
            System.err.println("Error processing image: " + e.getMessage());
        }
        ProjectModel projectModel = new ProjectModel(projectName);
        UserModel user = new UserModel(firstName, lastName, username, dob,
                gender, email, phoneNumber, encryptedPassword, userType, projectModel, imageUrl);
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