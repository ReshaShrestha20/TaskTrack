<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task Data Form - Connect.Track.Conquer</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/taskDataForm.css" />
</head>
<body>
    <div class="banner-image">
         <img src="${pageContext.request.contextPath}/resources/images/system/headBackground.jpg" alt="Desk Items" />
    </div>

    <main class="container">
        <h1 class="form-title">Task Data Form</h1>
        
        <form class="task-form">
            <div class="form-row">
                <div class="form-field">
                    <label for="taskId">Task ID</label>
                    <input type="text" id="taskId" name="taskId">
                </div>
                
                <div class="form-field">
                    <label for="taskTitle">Task Title</label>
                    <input type="text" id="taskTitle" name="taskTitle">
                </div>
                
                <div class="form-field">
                    <label for="status">Status</label>
                    <select id="status" name="status">
                        <option value="completed">Completed</option>
                        <option value="created">Created</option>
                        <option value="in-progress">In-progress</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-field">
                    <label for="assignedDate">Assigned Date</label>
                    <input type="date" id="assignedDate" name="assignedDate">
                </div>
                
                <div class="form-field">
                    <label for="dueDate">Due Date</label>
                    <input type="date" id="dueDate" name="dueDate">
                </div>
                
                <div class="form-field">
                    <label for="progress">Progress</label>
                    <input type="text" id="progress" name="progress">
                </div>
            </div>

            <div class="form-row">
                <div class="form-field wide-field">
                    <label for="assignedBy">Assigned By</label>
                    <input type="text" id="assignedBy" name="assignedBy">
                </div>
                
                <div class="form-field wide-field">
                    <label for="assignedTo">Assigned To</label>
                    <input type="text" id="assignedTo" name="assignedTo">
                </div>
            </div>

            <div class="form-row">
                <div class="form-field wide-field">
                    <label for="difficultyRank">Difficulty Rank</label>
                    <input type="number" id="difficultyRank" name="difficultyRank" min="1" max="10">
                </div>
                
                <div class="form-field wide-field">
                    <label for="priorityRank">Priority Rank</label>
                    <input type="number" id="priorityRank" name="priorityRank" min="1" max="10">
                </div>
            </div>

            <div class="button-row">
                <button type="button" class="add-btn">Add</button>
                <button type="button" class="update-btn">Update</button>
                <button type="button" class="delete-btn">Delete</button>
            </div>
            
            <div class="button-row view-row">
                <button type="button" class="view-btn">Go Back</button>
            </div>
        </form>
    </main>
</body>
</html>