<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Connect.Track.Conquer</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/home.css" />
<style>
.navbar {
    display: flex !important;
    justify-content: space-between !important;
    align-items: center !important;
    width: 100% !important;
}

.logout-btn {
    display: inline-block;
    background-color: #f44336;
    color: white;
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-weight: bold;
    text-decoration: none;
}

.logout-btn:hover {
    background-color: #d32f2f;
}
</style>
</head>
<body>
<div class="banner-image">
    <img src="${pageContext.request.contextPath}/resources/images/system/headBackground.jpg" alt="Desk Items" />
</div>

<nav class="navbar">
    <div class="nav-links">
        <a href="#" class="active">Home</a>
        <a href="userProfile">Profile</a>
        <a href="viewTask">Tasks</a>
        <a href="aboutUs">About Us</a>
        <a href="contactUs">Contact Us</a>
    </div>
    <form action="${pageContext.request.contextPath}/logout" method="post">
    	<input type="submit" class="logout-btn" value="Logout"/>
    </form>
    
</nav>

<main class="container">
    <section class="hero">
        <h1>Connect.Track.Conquer</h1>
        <p>Stay on top of your work with task track.</p>

        <div class="logo-container">
            <img src="${pageContext.request.contextPath}/resources/images/system/logo.png" alt="Task Track Logo" class="logo" />
        </div>

        <button class="get-started-btn">Get Started</button>
    </section>
</main>
</body>
</html>