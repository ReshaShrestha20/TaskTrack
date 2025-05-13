<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tasktrack.model.UserModel" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit User - TaskTrack</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
            color: #333;
        }

        .banner-image {
            width: 100%;
            height: 200px;
            overflow: hidden;
            position: relative;
        }

        .banner-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .container {
            max-width: 900px;
            margin: -50px auto 30px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 30px;
            position: relative;
            z-index: 1;
        }

        .form-title {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
            font-size: 24px;
        }

        .message {
            padding: 12px;
            margin-bottom: 20px;
            border-radius: 5px;
            font-size: 14px;
        }

        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .user-form {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        .form-row {
            display: flex;
            gap: 20px;
            margin-bottom: 5px;
        }

        .form-field {
            flex: 1;
            display: flex;
            flex-direction: column;
        }

        .wide-field {
            flex: 2;
        }

        label {
            font-weight: bold;
            margin-bottom: 5px;
            font-size: 14px;
            color: #555;
        }

        input[type="text"],
        input[type="email"],
        input[type="password"],
        input[type="date"],
        select {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
        }

        input:focus,
        select:focus {
            outline: none;
            border-color: #4CAF50;
            box-shadow: 0 0 5px rgba(76, 175, 80, 0.3);
        }

        .button-row {
            display: flex;
            justify-content: space-between;
            gap: 15px;
            margin-top: 20px;
        }

        .update-btn, .reset-btn, .view-btn {
            flex: 1;
            padding: 12px 15px;
            border: none;
            border-radius: 4px;
            font-weight: bold;
            cursor: pointer;
            text-align: center;
            font-size: 16px;
        }

        .update-btn {
            background-color: #4CAF50;
            color: white;
        }

        .update-btn:hover {
            background-color: #45a049;
        }

        .reset-btn {
            background-color: #f44336;
            color: white;
        }

        .reset-btn:hover {
            background-color: #d32f2f;
        }

        .view-btn {
            background-color: #607d8b;
            color: white;
            text-decoration: none;
            display: block;
        }

        .view-btn:hover {
            background-color: #455a64;
        }

        .view-row {
            margin-top: 15px;
        }

        .not-found-message {
            text-align: center;
            padding: 30px;
        }

        .not-found-message p {
            font-size: 18px;
            color: #555;
            margin-bottom: 20px;
        }

        @media (max-width: 768px) {
            .container {
                padding: 20px;
                margin-top: -30px;
            }
            
            .form-row {
                flex-direction: column;
                gap: 15px;
            }
            
            .button-row {
                flex-direction: column;
            }
            
            .banner-image {
                height: 150px;
            }
        }
    </style>
</head>
<body>
    <div class="banner-image">
        <img src="${pageContext.request.contextPath}/resources/images/system/headBackground.jpg" alt="Desk Items" />
    </div>

    <main class="container">
        <%
            UserModel user = (UserModel) request.getAttribute("user");
            boolean isEditMode = (user != null);
            String pageTitle = isEditMode ? "Edit User" : "User Not Found";
        %>

        <h1 class="form-title"><%= pageTitle %></h1>

        <!-- Success and Error messages -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="message error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <% if (request.getAttribute("success") != null) { %>
            <div class="message success">
                <%= request.getAttribute("success") %>
            </div>
        <% } %>

        <% if (isEditMode) { %>
            <form class="user-form" method="post" action="${pageContext.request.contextPath}/editUser">
                <!-- Hidden field for user ID -->
                <input type="hidden" id="userId" name="userId" value="<%= user.getId() %>">
                <input type="hidden" name="action" value="update">

                <div class="form-row">
                    <div class="form-field">
                        <label for="firstName">First Name</label>
                        <input type="text" id="firstName" name="firstName" required 
                               value="<%= user.getFirstName() != null ? user.getFirstName() : "" %>">
                    </div>

                    <div class="form-field">
                        <label for="lastName">Last Name</label>
                        <input type="text" id="lastName" name="lastName" required 
                               value="<%= user.getLastName() != null ? user.getLastName() : "" %>">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-field">
                        <label for="userName">Username</label>
                        <input type="text" id="userName" name="userName" required 
                               value="<%= user.getUserName() != null ? user.getUserName() : "" %>">
                    </div>

                    <div class="form-field">
                        <label for="userType">User Type</label>
                        <select id="userType" name="userType">
                            <option value="Admin" <%= "Admin".equals(user.getUserType()) ? "selected" : "" %>>Admin</option>
                            <option value="User" <%= "User".equals(user.getUserType()) ? "selected" : "" %>>User</option>
                        </select>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-field">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" required
                               value="<%= user.getEmail() != null ? user.getEmail() : "" %>">
                    </div>

                    <div class="form-field">
                        <label for="phoneNumber">Phone Number</label>
                        <input type="text" id="phoneNumber" name="phoneNumber" 
                               value="<%= user.getNumber() != null ? user.getNumber() : "" %>">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-field">
                        <label for="dob">Date of Birth</label>
                        <input type="date" id="dob" name="dob"
                               value="<%= user.getDob() != null ? user.getDob() : "" %>">
                    </div>

                    <div class="form-field">
                        <label for="gender">Gender</label>
                        <select id="gender" name="gender">
                            <option value="Male" <%= "Male".equals(user.getGender()) ? "selected" : "" %>>Male</option>
                            <option value="Female" <%= "Female".equals(user.getGender()) ? "selected" : "" %>>Female</option>
                            <option value="Other" <%= "Other".equals(user.getGender()) ? "selected" : "" %>>Other</option>
                        </select>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-field wide-field">
                        <label for="password">Reset Password (leave empty to keep current password)</label>
                        <input type="password" id="password" name="password" placeholder="New password">
                    </div>
                </div>

                <div class="button-row">
                    <button type="submit" class="update-btn">Update User</button>
                    <button type="reset" class="reset-btn">Reset Form</button>
                </div>

                <div class="button-row view-row">
                    <a href="userList" class="view-btn">Go Back to User List</a>
                </div>
            </form>
        <% } else { %>
            <div class="not-found-message">
                <p>The requested user was not found or could not be loaded.</p>
                <a href="userList" class="view-btn">Go Back to User List</a>
            </div>
        <% } %>
    </main>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            console.log('Form loaded');
            
            document.querySelector('.user-form')?.addEventListener('submit', function(event) {
                console.log('Form submission triggered');
                
                var formData = {
                    userId: document.getElementById('userId').value,
                    firstName: document.getElementById('firstName').value,
                    lastName: document.getElementById('lastName').value,
                    userName: document.getElementById('userName').value,
                    userType: document.getElementById('userType').value,
                    email: document.getElementById('email').value,
                    phoneNumber: document.getElementById('phoneNumber').value,
                    dob: document.getElementById('dob').value,
                    gender: document.getElementById('gender').value,
                    password: document.getElementById('password').value,
                    action: 'update'
                };
                
                console.log('Form data:', formData);
            });
        });
    </script>
</body>
</html>