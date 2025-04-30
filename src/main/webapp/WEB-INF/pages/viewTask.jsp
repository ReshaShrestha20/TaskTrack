<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task List - Connect.Track.Conquer</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/viewTask.css" />
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
            <div class="search-container">
                <input type="text" placeholder="Search tasks..." id="searchInput">
                <button class="search-button">Search by Task Title</button>
            </div>
        </div>

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
                    <tr>
                        <td>101</td>
                        <td>Database design for IMS</td>
                        <td>04/12/2024</td>
                        <td>23/12/2024</td>
                        <td>Sam Wilson</td>
                        <td>John Doe</td>
                        <td>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: 80%;"></div>
                                <span>80</span>
                            </div>
                        </td>
                        <td><span class="status missed">Missed</span></td>
                        <td><span class="difficulty high">High</span></td>
                        <td><span class="priority urgent">Urgent</span></td>
                    </tr>
                    <tr>
                        <td>102</td>
                        <td>Business Case for IMS</td>
                        <td>10/11/2024</td>
                        <td>17/11/2024</td>
                        <td>Mark Evans</td>
                        <td>Sarah Johnson</td>
                        <td>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: 100%;"></div>
                                <span>100</span>
                            </div>
                        </td>
                        <td><span class="status complete">Complete</span></td>
                        <td><span class="difficulty medium">Medium</span></td>
                        <td><span class="priority high">High</span></td>
                    </tr>
                    <tr>
                        <td>103</td>
                        <td>Wireframe designing</td>
                        <td>05/11/2024</td>
                        <td>15/12/2024</td>
                        <td>Rayne Holland</td>
                        <td>Mike Brown</td>
                        <td>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: 60%;"></div>
                                <span>60</span>
                            </div>
                        </td>
                        <td><span class="status in-progress">In progress</span></td>
                        <td><span class="difficulty low">Low</span></td>
                        <td><span class="priority normal">Normal</span></td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="action-buttons">
            <button class="add-task-btn">Open Task Data Form</button>
        </div>
    </main>

    <footer class="footer">
        <p>&copy; 2025 Task Track. All rights reserved.</p>
    </footer>
</body>
</html>