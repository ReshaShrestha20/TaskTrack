<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tasktrack.model.UserModel" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>TaskTrack - User List</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/userList.css" />
    <style>
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f7f0e0;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }
        
        .message {
            padding: 12px;
            margin-bottom: 15px;
            border-radius: 5px;
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
        
        .search-filter {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
        }
        
        .search-form {
            display: flex;
            flex-grow: 1;
            margin-right: 10px;
        }
        
        .search-form input {
            flex-grow: 1;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px 0 0 4px;
        }
        
        .search-form button {
            padding: 10px 15px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 0 4px 4px 0;
            cursor: pointer;
        }
        
        .filter-form {
            display: flex;
        }
        
        .filter-form select {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px 0 0 4px;
        }
        
        .filter-form button {
            padding: 10px 15px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 0 4px 4px 0;
            cursor: pointer;
        }
        
        .user-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        
        .user-table th {
            background-color: #4CAF50;
            color: white;
            padding: 12px;
            text-align: left;
        }
        
        .user-table td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }
        
        .user-table tr:hover {
            background-color: #f5f5f5;
        }
        
        .actions {
            display: flex;
            gap: 5px;
        }
        
        .edit-btn, .delete-btn {
            padding: 6px 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            color: white;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }
        
        .edit-btn {
            background-color: #2196F3;
        }
        
        .delete-btn {
            background-color: #f44336;
        }
        
        .center-button {
            text-align: center;
            margin-top: 20px;
        }
        
        .go-back-btn {
            padding: 10px 20px;
            background-color: #607d8b;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            text-decoration: none;
            display: inline-block;
        }
        
        .go-back-btn:hover {
            background-color: #455a64;
        }
        
        .no-data {
            text-align: center;
            padding: 20px;
            color: #666;
            font-style: italic;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>User Management</h1>
        
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
        
        <% 
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            session.removeAttribute("successMessage");
        %>
            <div class="message success">
                <%= successMessage %>
            </div>
        <% } %>
        
       <div class="search-filter">
		    <form class="search-form" action="${pageContext.request.contextPath}/userList" method="post">
		        <input type="hidden" name="action" value="search">
		        <input type="text" id="searchInput" name="searchInput" placeholder="Search users..." 
		               value="<%= request.getAttribute("searchQuery") != null ? request.getAttribute("searchQuery") : "" %>">
		        <button type="submit" class="search-button">Search</button>
		    </form>
    
		    <form class="filter-form" action="${pageContext.request.contextPath}/userList" method="post">
		        <input type="hidden" name="action" value="filter">
		        <select id="filterType" name="filterType">
		            <option value="all" <%= request.getAttribute("filterType") == null || "all".equals(request.getAttribute("filterType")) ? "selected" : "" %>>All Users</option>
		            <option value="Admin" <%= "Admin".equals(request.getAttribute("filterType")) ? "selected" : "" %>>Admins</option>
		            <option value="User" <%= "User".equals(request.getAttribute("filterType")) ? "selected" : "" %>>Regular Users</option>
		        </select>
		        <button type="submit" class="filter-button">Filter</button>
		    </form>
		</div>
        
 
        <% if (request.getAttribute("searchQuery") != null) { %>
            <div class="search-results-info">
                <p>
                    Showing results for: "<%= request.getAttribute("searchQuery") %>" 
                    <a href="userList">(Clear search)</a>
                </p>
            </div>
        <% } else if (request.getAttribute("filterType") != null && !"all".equals(request.getAttribute("filterType"))) { %>
            <div class="search-results-info">
                <p>
                    Filtered by user type: <%= request.getAttribute("filterType") %> 
                    <a href="userList">(Show all)</a>
                </p>
            </div>
        <% } %>
        
        <div class="table-container">
            <table class="user-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>User Type</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    List<UserModel> userList = (List<UserModel>) request.getAttribute("userList");
                    if (userList != null && !userList.isEmpty()) {
                        for (UserModel user : userList) {
                    %>
                        <tr>
                            <td><%= user.getId() %></td>
                            <td><%= user.getFirstName() %> <%= user.getLastName() %></td>
                            <td><%= user.getUserName() != null ? user.getUserName() : "" %></td>
                            <td><%= user.getEmail() %></td>
                            <td><%= user.getNumber() %></td>
                            <td><%= user.getUserType() %></td>
                            <td class="actions">
    						<a href="${pageContext.request.contextPath}/editUser?id=<%= user.getId() %>" class="edit-btn">Edit</a>
    						<form style="display:inline;" action="userList" method="post" onsubmit="return confirm('Are you sure you want to delete this user?');">
       					   	 <input type="hidden" name="action" value="delete">
        					 <input type="hidden" name="userId" value="<%= user.getId() %>">
  							 <button type="submit" class="delete-btn">Delete</button>
    </form>
</td>
                        </tr>
                    <% 
                        }
                    } else {
                    %>
                        <tr>
                            <td colspan="7" class="no-data">
                                <% if (request.getAttribute("searchQuery") != null) { %>
                                    No users found matching "<%= request.getAttribute("searchQuery") %>". 
                                    <a href="userList">View all users</a>
                                <% } else if (request.getAttribute("filterType") != null && !"all".equals(request.getAttribute("filterType"))) { %>
                                    No users found with type "<%= request.getAttribute("filterType") %>". 
                                    <a href="userList">View all users</a>
                                <% } else { %>
                                    No users found in the system.
                                <% } %>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        
        <div class="center-button">
            <a href="adminDashboard" class="go-back-btn">Go Back to Dashboard</a>
        </div>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const searchInput = document.getElementById('searchInput');
            if (searchInput) {
                searchInput.addEventListener('keyup', function(event) {
                    if (event.key === 'Enter') {
                        this.closest('form').submit();
                    }
                });
            }
            
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