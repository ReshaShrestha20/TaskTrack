package com.tasktrack.controller;

import java.io.IOException;

import com.tasktrack.model.UserModel;
import com.tasktrack.service.UserService;
import com.tasktrack.util.ImageUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet(asyncSupported = true, urlPatterns = {"/adminProfile"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50)
public class AdminProfileController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private final UserService userService;
    private final ImageUtil imageUtil;
    
    public AdminProfileController() {
        this.userService = new UserService();
        this.imageUtil = new ImageUtil();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String username = (String) session.getAttribute("username");
        UserModel admin = userService.getUserByUsername(username);
        if (admin == null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String imagePath = userService.getUserImagePath(admin.getId());
        request.setAttribute("user", admin);
        request.setAttribute("imagePath", imagePath);
        request.getRequestDispatcher("/WEB-INF/pages/adminProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String username = request.getParameter("username");
            String dobStr = request.getParameter("dob");
            String gender = request.getParameter("gender");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phoneNumber");
            UserModel existingUser = userService.getUserById(userId);
            if (existingUser == null) {
                request.setAttribute("error", "User not found!");
                doGet(request, response);
                return;
            }
            if (existingUser.getUserType() == null || !existingUser.getUserType().equalsIgnoreCase("Admin")) {
                request.setAttribute("error", "This profile is not an admin profile!");
                doGet(request, response);
                return;
            }
            existingUser.setFirstName(firstName);
            existingUser.setLastName(lastName);
            existingUser.setUserName(username);
            
            if (dobStr != null && !dobStr.isEmpty()) {
                try {
                    existingUser.setDob(java.time.LocalDate.parse(dobStr));
                } catch (Exception e) {
                    System.err.println("Error parsing date: " + e.getMessage());
                }
            }
            
            existingUser.setGender(gender);
            existingUser.setEmail(email);
            existingUser.setNumber(phoneNumber);
            
            Part imagePart = request.getPart("profileImage");
            if (imagePart != null && imagePart.getSize() > 0) {
                
                if (!imageUtil.isValidImageExtension(imagePart)) {
                    request.setAttribute("error", "Invalid image format. Only jpg, jpeg, png, and gif are allowed.");
                    doGet(request, response);
                    return;
                }
                
                String imageName = imageUtil.getImageNameFromPart(imagePart);
                existingUser.setImageUrl(imageName);
                
                boolean imageUploaded = imageUtil.uploadImage(imagePart, "user", request.getServletContext());
                if (!imageUploaded) {
                    request.setAttribute("error", "Failed to upload the profile image.");
                    doGet(request, response);
                    return;
                }
            }
            
            boolean updated = userService.updateUser(existingUser);
            
            if (updated) {
                if (!existingUser.getUserName().equals(session.getAttribute("username"))) {
                    session.setAttribute("username", existingUser.getUserName());
                }
                
                request.setAttribute("success", "Profile updated successfully!");
                
                String imagePath = userService.getUserImagePath(userId);
                request.setAttribute("imagePath", imagePath);
            } else {
                request.setAttribute("error", "Failed to update profile. Please try again.");
            }
            
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/WEB-INF/pages/adminProfile.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error updating admin profile: " + e.getMessage());
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            doGet(request, response);
        }
    }
}