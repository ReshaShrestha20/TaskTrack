<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>About Us - Connect.Track.Conquer</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/aboutUs.css" />
</head>
<body>
<div class="banner-image">
<img src="${pageContext.request.contextPath}/resources/images/system/headBackground.jpg" alt="Desk Items" />
</div>

<nav class="navbar">
<div class="nav-links">
			<a href="home">Home</a>
        	<a href="userProfile">Profile</a>
        	<a href="viewTask">Tasks</a>
        	<a href="aboutUs" class="active">About Us</a>
        	<a href="contactUs">Contact Us</a>
</div>
</nav>

<main class="about-container">
<div class="about-left">
<h1 class="about-heading">About Us</h1>
<div class="logo-container">
<img src="${pageContext.request.contextPath}/resources/images/system/logo.png" alt="Task Track Logo" class="task-track-logo">
</div>
</div>

<div class="about-right">
<h2 class="welcome-heading">Welcome to Task Track</h2>
<p class="tagline">the ultimate solution for efficient task management and seamless project tracking</p>

<p class="mission-statement">
At Task Track we believe that every successful project begins with effective planning, clear goals and streamlined communication.
</p>

<p class="cta-text">Empower you and your team with us.</p>

<h3 class="team-heading">Meet the Task Track Leaders</h3>

<div class="team-members">
<div class="team-member">
<img src="${pageContext.request.contextPath}/resources/images/system/founder.png" alt="Sam Wilson" class="member-photo">
<h4 class="member-name">Sam Wilson</h4>
<p class="member-title">Founder</p>
</div>

<div class="team-member">
<img src="${pageContext.request.contextPath}/resources/images/system/co-founder.png" alt="Mark Evans" class="member-photo">
<h4 class="member-name">Mark Evans</h4>
<p class="member-title">Co-founder</p>
</div>

<div class="team-member">
<img src="${pageContext.request.contextPath}/resources/images/system/CEO.png" alt="Rayne Holland" class="member-photo">
<h4 class="member-name">Rayne Holland</h4>
<p class="member-title">CEO</p>
</div>
</div>
</div>
</main>
</body>
</html>