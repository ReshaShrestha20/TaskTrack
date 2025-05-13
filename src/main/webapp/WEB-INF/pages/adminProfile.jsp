<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile - Connect.Track.Conquer</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/userProfile.css" />
</head>
<body>
    <div class="banner-image">
        <img src="${pageContext.request.contextPath}/resources/images/system/headBackground.jpg" alt="Desk Items" />
    </div>
    
    <nav class="navbar">
        <div class="nav-links">
            <a href="adminDashboard">Admin Dashboard</a>
            <a href="adminProfile" class="active">Admin Profile</a>
            <a href="userList">User List</a>
        </div>
    </nav>

    <main class="container">
        <div class="profile-container">
            <div class="profile-image">
                <!-- Profile image placeholder -->
            </div>
            
            <div class="profile-info">
                <div class="info-row">
                    <div class="info-field">
                        <label>First Name</label>
                        <input type="text" disabled />
                    </div>
                    
                    <div class="info-field">
                        <label>DOB</label>
                        <input type="text" disabled />
                    </div>
                </div>
                
                <div class="info-row">
                    <div class="info-field">
                        <label>Last Name</label>
                        <input type="text" disabled />
                    </div>
                    
                    <div class="info-field">
                        <label>Phone Number</label>
                        <input type="text" disabled />
                    </div>
                </div>
                
                <div class="info-row">
                    <div class="info-field">
                        <label>Gender</label>
                        <input type="text" disabled />
                    </div>
                    
                    <div class="info-field">
                        <label>Username</label>
                        <input type="text" disabled />
                    </div>
                </div>
                
                <div class="info-row">
                    <div class="info-field">
                        <label>Email</label>
                        <input type="email" disabled />
                    </div>
                    
                    <div class="info-field">
                        <label>Password</label>
                        <input type="password" disabled />
                    </div>
                </div>
                
                <div class="button-container">
                    <button class="edit-btn">Edit</button>
                </div>
            </div>
        </div>
    </main>
</body>
</html>