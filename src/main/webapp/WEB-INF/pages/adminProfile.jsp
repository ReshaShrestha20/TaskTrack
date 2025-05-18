<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Profile - Connect.Track.Conquer</title>
    <style>
        /* Reset and general styles */
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }
        
        body {
            font-family: Arial, sans-serif;
            background-color: #e6f2f2;
        }
        
        /* Banner image */
        .banner-image {
            width: 100%;
            height: 150px;
            overflow: hidden;
        }
        
        .banner-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        /* Navigation */
        .navbar {
            background-color: #f5f5f5; /* Light gray background */
            padding: 10px 0;
            border-bottom: 1px solid #ddd;
        }
        
        .nav-links {
            display: flex;
            padding-left: 20px;
        }
        
        .nav-links a {
            display: inline-block;
            color: #333;
            text-decoration: none;
            padding: 5px 15px;
            margin-right: 5px;
            border-radius: 4px;
        }
        
        .nav-links a.active {
            background-color: rgba(0, 0, 0, 0.1);
            font-weight: bold;
        }
        
        /* Profile container */
        .profile-container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        
        /* Admin badge */
        .admin-badge {
            background-color: #343a40;
            color: white;
            font-size: 14px;
            padding: 4px 12px;
            border-radius: 20px;
            display: inline-block;
            margin: 10px auto;
        }
        
        .admin-badge-container {
            text-align: center;
            margin-bottom: 20px;
        }
        
        /* Profile image */
        .profile-image {
            width: 200px;
            height: 200px;
            border-radius: 50%;
            overflow: hidden;
            background-color: #ddd;
            margin: 20px auto;
            display: block;
            border: 3px solid #343a40; /* Admin-specific border */
        }
        
        .profile-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        /* Form styles */
        .info-field {
            margin-bottom: 15px;
        }
        
        .info-field label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
        }
        
        .info-field input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            background-color: #e8f4f4;
        }
        
        /* Button styles */
        .button-container {
            text-align: center;
            margin-top: 20px;
        }
        
        .edit-btn {
            background-color: #343a40; /* Different color for admin */
            color: white;
            border: none;
            padding: 10px 30px;
            font-size: 16px;
            border-radius: 4px;
            cursor: pointer;
        }
        
        /* Upload container */
        .upload-container {
            text-align: center;
            margin-top: 10px;
            display: none;
        }
        
        /* Messages */
        .success-message, .error-message {
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 4px;
        }
        
        .success-message {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .error-message {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
    <script>
        function toggleEditMode() {
            var inputs = document.querySelectorAll('.info-field input:not([type="password"])');
            var editButton = document.getElementById('editButton');
            var saveButton = document.getElementById('saveButton');
            var cancelButton = document.getElementById('cancelButton');
            var imageUpload = document.getElementById('imageUploadContainer');
            
            // Toggle disabled state for input fields
            for (var i = 0; i < inputs.length; i++) {
                inputs[i].disabled = !inputs[i].disabled;
            }
            
            // Toggle buttons visibility
            if (editButton.style.display === 'none') {
                editButton.style.display = 'inline-block';
                saveButton.style.display = 'none';
                cancelButton.style.display = 'none';
                imageUpload.style.display = 'none';
            } else {
                editButton.style.display = 'none';
                saveButton.style.display = 'inline-block';
                cancelButton.style.display = 'inline-block';
                imageUpload.style.display = 'block';
            }
        }
        
        function cancelEdit() {
            // Reload the page to discard changes
            window.location.reload();
        }
    </script>
</head>
<body>
    <div class="banner-image">
        <img src="${pageContext.request.contextPath}/resources/images/system/headBackground.jpg" alt="Admin Dashboard" />
    </div>

    <nav class="navbar">
        <div class="nav-links">
            <a href="adminDashboard">Dashboard</a>
            <a href="adminProfile" class="active">Admin Profile</a>
            <a href="userList">User List</a>
        </div>
    </nav>

    <div class="profile-container">
        <!-- Display success or error messages if any -->
        <% if (request.getAttribute("success") != null) { %>
            <div class="success-message">${success}</div>
        <% } %>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">${error}</div>
        <% } %>

        <div class="admin-badge-container">
            <div class="admin-badge">Administrator</div>
        </div>

        <form method="POST" action="adminProfile" enctype="multipart/form-data">
            <div class="profile-image">
                <img src="${pageContext.request.contextPath}/${imagePath}" alt="Admin Profile Image" />
            </div>
            
            <div id="imageUploadContainer" class="upload-container">
                <input type="file" name="profileImage" id="profileImage" accept="image/*">
                <div style="font-size: 12px; color: #666; margin-top: 5px;">Upload a new profile picture</div>
            </div>
            
            <div class="info-field">
                <label for="firstName">First Name</label>
                <input type="text" id="firstName" name="firstName" value="${user.firstName}" disabled />
            </div>

            <div class="info-field">
                <label for="lastName">Last Name</label>
                <input type="text" id="lastName" name="lastName" value="${user.lastName}" disabled />
            </div>

            <div class="info-field">
                <label for="gender">Gender</label>
                <input type="text" id="gender" name="gender" value="${user.gender}" disabled />
            </div>

            <div class="info-field">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" value="${user.email}" disabled />
            </div>

            <div class="info-field">
                <label for="dob">DOB</label>
                <input type="date" id="dob" name="dob" value="${user.dob}" disabled />
            </div>

            <div class="info-field">
                <label for="phoneNumber">Phone Number</label>
                <input type="text" id="phoneNumber" name="phoneNumber" value="${user.number}" disabled />
            </div>

            <div class="info-field">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" value="${user.userName}" disabled />
            </div>

            <div class="info-field">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" value="********" disabled />
            </div>

            <div class="button-container">
                <button type="button" id="editButton" class="edit-btn" onclick="toggleEditMode()">Edit Profile</button>
                <button type="submit" id="saveButton" class="edit-btn" style="display: none;">Save Changes</button>
                <button type="button" id="cancelButton" class="edit-btn" style="display: none; background-color: #dc3545; margin-left: 10px;" onclick="cancelEdit()">Cancel</button>
            </div>
            
            <input type="hidden" name="userId" value="${user.id}">
        </form>
    </div>
</body>
</html>