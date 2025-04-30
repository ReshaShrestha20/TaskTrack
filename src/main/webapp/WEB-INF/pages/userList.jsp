<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>TaskTrack - User List</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/userList.css" />
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
        
        <div class="search-filter">
            <input type="text" id="searchInput" placeholder="Search users...">
            <select id="filterType">
                <option value="all">All Users</option>
                <option value="Admin">Admins</option>
                <option value="User">Regular Users</option>
            </select>
        </div>
        
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
                    if(request.getAttribute("userList") != null) {
                        java.util.List<?> userList = (java.util.List<?>)request.getAttribute("userList");
                        for(Object userObj : userList) {
                            java.lang.reflect.Method getId = userObj.getClass().getMethod("getId");
                            java.lang.reflect.Method getFirstName = userObj.getClass().getMethod("getFirstName");
                            java.lang.reflect.Method getLastName = userObj.getClass().getMethod("getLastName");
                            java.lang.reflect.Method getUsername = userObj.getClass().getMethod("getUsername");
                            java.lang.reflect.Method getEmail = userObj.getClass().getMethod("getEmail");
                            java.lang.reflect.Method getPhoneNumber = userObj.getClass().getMethod("getPhoneNumber");
                            java.lang.reflect.Method getUserType = userObj.getClass().getMethod("getUserType");
                            Object id = getId.invoke(userObj);
                            String firstName = (String)getFirstName.invoke(userObj);
                            String lastName = (String)getLastName.invoke(userObj);
                            String username = (String)getUsername.invoke(userObj);
                            String email = (String)getEmail.invoke(userObj);
                            String phoneNumber = (String)getPhoneNumber.invoke(userObj);
                            String userType = (String)getUserType.invoke(userObj);
                    %>
                        <tr>
                            <td><%= id %></td>
                            <td><%= firstName %> <%= lastName %></td>
                            <td><%= username %></td>
                            <td><%= email %></td>
                            <td><%= phoneNumber %></td>
                            <td><%= userType %></td>
                            <td class="actions">
                                <button class="edit-btn" onclick="editUser('<%= id %>')">Edit</button>
                                <button class="delete-btn" onclick="deleteUser('<%= id %>')">Delete</button>
                            </td>
                        </tr>
                    <%
                        }
                    }
                    %>
                    
                    <!-- Sample data for preview (remove in production) -->
                    <tr>
                        <td>114</td>
                        <td>Resha Shrestha</td>
                        <td>user8</td>
                        <td>reshast1@gmail.com</td>
                        <td>9874562407</td>
                        <td>User</td>
                        <td class="actions">
                            <button class="edit-btn">Edit</button>
                            <button class="delete-btn">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <td>112</td>
                        <td>Rabina Burlakoti</td>
                        <td>admin203</td>
                        <td>rabina780@gmail.com</td>
                        <td>7895632107</td>
                        <td>Admin</td>
                        <td class="actions">
                            <button class="edit-btn">Edit</button>
                            <button class="delete-btn">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <td>111</td>
                        <td>Dinushka Regmi</td>
                        <td>dregmi925</td>
                        <td>drengmi@gmail.com</td>
                        <td>1234567890</td>
                        <td>User</td>
                        <td class="actions">
                            <button class="edit-btn">Edit</button>
                            <button class="delete-btn">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <td>110</td>
                        <td>Prachi Sharma</td>
                        <td>psharma19</td>
                        <td>prachisharma@gmail.com</td>
                        <td>9876123450</td>
                        <td>User</td>
                        <td class="actions">
                            <button class="edit-btn">Edit</button>
                            <button class="delete-btn">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <td>109</td>
                        <td>Rosha Shrestha</td>
                        <td>admin9940</td>
                        <td>roshashrestha10@gmail.com</td>
                        <td>9876554217</td>
                        <td>Admin</td>
                        <td class="actions">
                            <button class="edit-btn">Edit</button>
                            <button class="delete-btn">Delete</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        
        <div class="pagination">
            <button class="page-btn">Previous</button>
            <span class="page-info">Page 1 of 3</span>
            <button class="page-btn">Next</button>
        </div>
        
        <div class="center-button">
            <button id="updateBtn" class="update-btn">Update</button>
        </div>
    </div>
    
    <script>
        // Basic search functionality
        document.getElementById('searchInput').addEventListener('keyup', function() {
            const filter = this.value.toLowerCase();
            const rows = document.querySelectorAll('.user-table tbody tr');
            
            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(filter) ? '' : 'none';
            });
        });
        
        // Filter by user type
        document.getElementById('filterType').addEventListener('change', function() {
            const filter = this.value;
            const rows = document.querySelectorAll('.user-table tbody tr');
            
            rows.forEach(row => {
                const userType = row.cells[5].textContent;
                if (filter === 'all' || userType === filter) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        });
        
        // Update button functionality
        document.getElementById('updateBtn').addEventListener('click', function() {
            // Add your update logic here
            alert('User list updated successfully!');
        });
        
        function editUser(id) {
            // Add your edit logic here
            console.log('Edit user ID:', id);
        }
        
        function deleteUser(id) {
            // Add your delete logic here
            if (confirm('Are you sure you want to delete this user?')) {
                console.log('Delete user ID:', id);
            }
        }
    </script>
</body>
</html>