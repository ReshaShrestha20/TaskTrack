<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>TaskTrack - Registration</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background-color: #f2e8d5;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            color: #5a5a5a;
            margin-bottom: 30px;
        }
        .form-row {
            display: flex;
            flex-wrap: wrap;
            margin-bottom: 20px;
        }
        .form-group {
            flex: 1;
            margin-right: 20px;
            min-width: 300px;
        }
        .form-group:last-child {
            margin-right: 0;
        }
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }
        input, select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            background-color: #fffbf0;
            box-sizing: border-box;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #45a049;
        }
        .message {
            padding: 10px;
            margin-bottom: 20px;
            border-radius: 4px;
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
        .center-button {
            text-align: center;
            margin-top: 20px;
        }
        .small-text {
            font-size: 12px;
            color: #666;
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Registration Form</h1>
        
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
        
        <form method="POST" action="register" enctype="multipart/form-data">
            <div class="form-row">
                <div class="form-group">
                    <label for="firstName">First Name</label>
                    <input type="text" id="firstName" name="firstName" required>
                </div>
                <div class="form-group">
                    <label for="gender">Gender</label>
                    <select id="gender" name="gender" required>
                        <option value="Male">Male</option>
                        <option value="Female">Female</option>
                        <option value="Other">Other</option>
                    </select>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="lastName">Last Name</label>
                    <input type="text" id="lastName" name="lastName" required>
                </div>
                <div class="form-group">
                    <label for="phoneNumber">Phone Number</label>
                    <input type="tel" id="phoneNumber" name="phoneNumber" pattern="[0-9]{10}" required>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" required>
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" required>
                </div>
            </div>
            
            <div class="form-row">
    <div class="form-group">
        <label for="dob">Date of Birth</label>
        <input type="date" id="dob" name="dob" required>
        <div class="small-text">Accepted formats: YYYY-MM-DD, MM/DD/YYYY, DD/MM/YYYY, MM-DD-YYYY</div>
    </div>
    <div class="form-group">
        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>
    </div>
</div>
            
            <div class="form-row"> 
                <div class="form-group">
                    <label for="userType">User Type</label>
                    <select id="userType" name="userType" required>
                        <option value="User">User</option>
                        <option value="Admin">Admin</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="retypePassword">Retype Password</label>
                    <input type="password" id="retypePassword" name="retypePassword" required>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="image">Profile Image</label>
                    <input type="file" id="image" name="image" accept="image/*">
                    <div class="small-text">Supported formats: JPG, JPEG, PNG, GIF</div>
                </div>
                <div class="form-group">
                    <!-- Empty for layout balance -->
                </div>
            </div>
            
            <div class="center-button">
                <button type="submit">Submit</button>
            </div>
        </form>
    </div>
</body>
</html>