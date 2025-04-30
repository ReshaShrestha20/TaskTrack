<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tasktrack.util.CookiesUtil" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Task Track</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css" />
</head>
<body>
    <div class="left-section">
        <img src="${pageContext.request.contextPath}/resources/images/system/loginPageSideScreenIcon.png" />
    </div>
    <div class="right-section">
        <h1>Welcome to<br><strong>Task Track</strong></h1>

        <% if(request.getAttribute("error") != null) { %>
            <div class="error-message"><%= request.getAttribute("error") %></div>
        <% } %>

        <% if(request.getAttribute("success") != null) { %>
            <div class="success-message"><%= request.getAttribute("success") %></div>
        <% } %>

        <div class="login-buttons">
            <button class="facebook">Login with Facebook</button>
            <button class="google">Login with Google</button>
        </div>
        <div class="divider"><span>Or</span></div>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="input-field">
                <input type="text" name="username" placeholder="Enter your username">
            </div>
            <div class="input-field">
                <input type="password" name="password" placeholder="Enter your password">
            </div>
            <div class="forgot"><a href="#">Forgot Password?</a></div>
            <button type="submit" class="login-button">Login</button>
        </form>
        
        <div class="register-link">
            <p>Don't have an account? <a href="${pageContext.request.contextPath}/register">Register here</a></p>
        </div>
    </div>
</body>
</html>