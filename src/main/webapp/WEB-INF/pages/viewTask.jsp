<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tasktrack.model.TaskModel" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task List - Connect.Track.Conquer</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/viewTask.css" />
    <style>
        .clickable-row {
            cursor: pointer;
        }
        .clickable-row:hover {
            background-color: #f5f5f5;
        }
        
        .search-container {
            display: flex;
            justify-content: center;
            margin-bottom: 20px;
        }
        
        .search-container form {
            display: flex;
            width: 80%;
            max-width: 600px;
        }
        
        .search-container input[type="text"] {
            flex-grow: 1;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px 0 0 4px;
            font-size: 16px;
        }
        
        .search-button {
            padding: 10px 15px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 0 4px 4px 0;
            cursor: pointer;
            font-size: 16px;
        }
        
        .search-button:hover {
            background-color: #45a049;
        }
        
        .no-tasks-message {
            text-align: center;
            padding: 20px;
            font-size: 18px;
            color: #666;
        }
        
        .search-results-info {
            text-align: center;
            margin-bottom: 15px;
            font-size: 14px;
            color: #666;
        }
        
        .search-results-info a {
            color: #2196F3;
            text-decoration: none;
            margin-left: 10px;
        }
        
        .search-results-info a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="banner-image">
        <img src="${pageContext.request.contextPath}/resources/images/system/headBackground.jpg" alt="Desk Items" />
    </div>

    <nav class="navbar">
        <div class="nav-links">
            <a href="home">Home</a>
            <a href="userProfile">Profile</a>
            <a href="viewTask" class="active">Tasks</a>
            <a href="aboutUs">About Us</a>
            <a href="contactUs">Contact Us</a>
        </div>
    </nav>

    <main class="task-list-container">
        <div class="task-list-header">
            <h1>Task List</h1>
        </div>
        
        <div class="search-container">
            <form action="viewTask" method="post">
                <input type="text" placeholder="Search tasks by title..." id="searchInput" name="searchInput" 
                       value="<%= request.getAttribute("searchQuery") != null ? request.getAttribute("searchQuery") : "" %>">
                <button type="submit" class="search-button">Search</button>
            </form>
        </div>
        
        <!-- Search results info (only show when search was performed) -->
        <% if (request.getAttribute("searchQuery") != null) { %>
            <div class="search-results-info">
                <p>
                    Showing results for: "<%= request.getAttribute("searchQuery") %>" 
                    <a href="viewTask">View all tasks</a>
                </p>
            </div>
        <% } %>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <div class="task-table-container">
            <table class="task-table">
                <thead>
                    <tr>
                        <th>Task ID</th>
                        <th>Task Title</th>
                        <th>Assigned Date</th>
                        <th>Due Date</th>
                        <th>Assigned By</th>
                        <th>Assigned To</th>
                        <th>Progress</th>
                        <th>Status</th>
                        <th>Difficulty</th>
                        <th>Priority</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        List<TaskModel> tasks = (List<TaskModel>) request.getAttribute("tasks");
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        
                        if (tasks == null || tasks.isEmpty()) {
                    %>
                        <tr>
                            <td colspan="10" class="no-tasks-message">
                                <% if (request.getAttribute("searchQuery") != null) { %>
                                    No tasks found matching "<%= request.getAttribute("searchQuery") %>". 
                                    <a href="viewTask">View all tasks</a>
                                <% } else { %>
                                    No tasks found. 
                                    <a href="taskDataForm">Create a new task</a>
                                <% } %>
                            </td>
                        </tr>
                    <% 
                        } else {
                            for (TaskModel task : tasks) {
                                String standardizedStatus = task.getStandardizedStatus();
                    %>
                        <tr class="clickable-row" onclick="window.location.href='taskDataForm?id=<%= task.getTaskId() %>';">
                            <td><%= task.getTaskId() %></td>
                            <td><%= task.getTaskTitle() %></td>
                            <td><%= task.getAssignedDate() != null ? task.getAssignedDate().format(dateFormatter) : "" %></td>
                            <td><%= task.getDueDate() != null ? task.getDueDate().format(dateFormatter) : "" %></td>
                            <td><%= task.getAssignedBy() %></td>
                            <td><%= task.getAssignedTo() %></td>
                            <td>
                                <div class="progress-bar">
                                    <div class="progress-fill" style="width: <%= task.getProgress() %>%;"></div>
                                    <span><%= task.getProgress() %></span>
                                </div>
                            </td>
                            <td><span class="status <%= task.getStatusClass() %>"><%= standardizedStatus %></span></td>
                            <td><span class="difficulty <%= task.getDifficultyText().toLowerCase() %>"><%= task.getDifficultyText() %></span></td>
                            <td><span class="priority <%= task.getPriorityText().toLowerCase() %>"><%= task.getPriorityText() %></span></td>
                        </tr>
                    <% 
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>

        <div class="action-buttons">
            <a href="taskDataForm" class="add-task-btn">Add New Task</a>
        </div>
    </main>

    <footer class="footer">
        <p>&copy; 2025 Task Track. All rights reserved.</p>
    </footer>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            console.log('Task list page loaded');
            
            const searchInput = document.getElementById('searchInput');
            if (searchInput && searchInput.value === '') {
                setTimeout(function() {
                    if (document.activeElement === document.body) {
                        searchInput.focus();
                    }
                }, 500);
            }
        });
    </script>
</body>
</html>