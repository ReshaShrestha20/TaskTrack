<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.List, com.tasktrack.model.UserModel"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Task Track</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/adminDashboard.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="banner-image">
        <img src="${pageContext.request.contextPath}/resources/images/system/headBackground.jpg" alt="Desk Items" />
    </div>

    <nav class="navbar">
        <div class="nav-links">
            <a href="adminDashboard" class="active">Admin Dashboard</a>
            <a href="adminProfile">Admin Profile</a>
            <a href="userList">User List</a>
            <a href="modifyUser">Modify User</a>
        </div>
    </nav>

    <main class="dashboard-container">
        <!-- Header/Navigation -->
        <header class="dashboard-header">
            <div class="dashboard-title">
                <i class="fas fa-th-large"></i> Dashboard
            </div>
        </header>

        <!-- Main Content -->
        <div class="dashboard-content">
            <!-- Display error message if there is one -->
            <% 
            String dbError = (String)request.getAttribute("dbError");
            if (dbError != null && !dbError.isEmpty()) { 
            %>
                <div class="error-message">
                    <p><i class="fas fa-exclamation-triangle"></i> <%= dbError %></p>
                </div>
            <% } %>
            
            <!-- Stats Overview Section -->
            <section class="stats-section">
                <div class="stat-card users">
                    <div class="stat-icon">
                        <i class="fas fa-user-circle"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Users</h3>
                        <p class="stat-count"><%= request.getAttribute("userCount") != null ? request.getAttribute("userCount") : 0 %></p>
                    </div>
                </div>

                <div class="stat-card admins">
                    <div class="stat-icon">
                        <i class="fas fa-user-shield"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Admins</h3>
                        <p class="stat-count"><%= request.getAttribute("adminCount") != null ? request.getAttribute("adminCount") : 0 %></p>
                    </div>
                </div>

                <div class="stat-card projects">
                    <div class="stat-icon">
                        <i class="fas fa-cogs"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Projects</h3>
                        <p class="stat-count"><%= request.getAttribute("projectCount") != null ? request.getAttribute("projectCount") : 0 %></p>
                    </div>
                </div>

                <div class="stat-card tasks">
                    <div class="stat-icon">
                        <i class="fas fa-tasks"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Tasks</h3>
                        <p class="stat-count"><%= request.getAttribute("taskCount") != null ? request.getAttribute("taskCount") : 0 %></p>
                    </div>
                </div>
            </section>

            <!-- Charts Section -->
            <section class="charts-section">
                <div class="chart-card">
                    <h3>User number overview</h3>
                    <div class="chart user-chart">
                        <!-- User chart will be rendered here -->
                        <img src="${pageContext.request.contextPath}/resources/images/system/userNumberChart.png" alt="User number chart">
                    </div>
                </div>

                <div class="chart-card">
                    <h3>Task success insight</h3>
                    <div class="chart task-chart">
                        <!-- Task chart will be rendered here -->
                        <img src="${pageContext.request.contextPath}/resources/images/system/taskSuccess.png" alt="Task success chart">
                    </div>
                </div>
            </section>

            <!-- Recently Added Users Table -->
            <section class="table-section">
                <h3>Recently Added Users</h3>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Phone</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% 
                    List<UserModel> recentUsers = (List<UserModel>)request.getAttribute("recentUsers");
                    if (recentUsers != null && !recentUsers.isEmpty()) {
                        for (UserModel user : recentUsers) {
                    %>
                        <tr>
                            <td><%= user.getId() %></td>
                            <td><%= user.getFirstName() + " " + user.getLastName() %></td>
                            <td><%= user.getUserName() %></td>
                            <td><%= user.getEmail() %></td>
                            <td><%= user.getNumber() %></td>
                        </tr>
                    <%
                      }
                    } 
                    else {
                    %>
                        <!-- Display default data if no users are found -->
                        <tr>
                            <td>001</td>
                            <td>John Doe</td>
                            <td>john.doe</td>
                            <td>john.doe@example.com</td>
                            <td>555-1234</td>
                        </tr>
                        <tr>
                            <td>002</td>
                            <td>Jane Smith</td>
                            <td>jane.smith</td>
                            <td>jane.smith@example.com</td>
                            <td>555-5678</td>
                        </tr>
                        <tr>
                            <td>003</td>
                            <td>Alex Johnson</td>
                            <td>alex.j</td>
                            <td>alex.j@example.com</td>
                            <td>555-9012</td>
                        </tr>
                        <tr>
                            <td>004</td>
                            <td>Sara Williams</td>
                            <td>sara.w</td>
                            <td>sara.w@example.com</td>
                            <td>555-3456</td>
                        </tr>
                    <%
                    }
                    %>
                    </tbody>
                </table>
            </section>
        </div>
    </main>
</body>
</html>