<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tasktrack.model.TaskModel" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
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
<%
    TaskModel task = (TaskModel) request.getAttribute("task");
    boolean isEditMode = (task != null);
    String pageTitle = isEditMode ? "Edit Task" : "Add New Task";
%>

<h1 class="form-title"><%= pageTitle %></h1>

<!-- Success and Error messages -->
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

<form class="task-form" method="post" action="${pageContext.request.contextPath}/taskDataForm">
<!-- Hidden field for task ID when in edit mode -->
<% if (isEditMode) { %>
<input type="hidden" id="taskId" name="taskId" value="<%= task.getTaskId() %>">
<% } %>

<div class="form-row">
<div class="form-field">
<label for="taskTitle">Task Title</label>
<input type="text" id="taskTitle" name="taskTitle" required 
       value="<%= isEditMode ? task.getTaskTitle() : "" %>">
</div>

<div class="form-field">
<label for="status">Status</label>
<select id="status" name="status">
    <option value="Complete" <%= isEditMode && "Complete".equals(task.getStatus()) ? "selected" : "" %>>Complete</option>
    <option value="Missed" <%= isEditMode && "Missed".equals(task.getStatus()) ? "selected" : "" %>>Missed</option>
    <option value="In-progress" <%= !isEditMode || (isEditMode && "In-progress".equals(task.getStatus())) ? "selected" : "" %>>In-progress</option>
</select>
</div>
</div>

<div class="form-row">
<div class="form-field">
<label for="assignedDate">Assigned Date</label>
<input type="date" id="assignedDate" name="assignedDate"
       value="<%= isEditMode && task.getAssignedDate() != null ? task.getAssignedDate() : "" %>">
</div>

<div class="form-field">
<label for="dueDate">Due Date</label>
<input type="date" id="dueDate" name="dueDate"
       value="<%= isEditMode && task.getDueDate() != null ? task.getDueDate() : "" %>">
</div>

<div class="form-field">
<label for="progress">Progress</label>
<input type="text" id="progress" name="progress"
       value="<%= isEditMode ? task.getProgress() : "" %>">
</div>
</div>

<div class="form-row">
<div class="form-field wide-field">
<label for="assignedBy">Assigned By</label>
<input type="text" id="assignedBy" name="assignedBy"
       value="<%= isEditMode && task.getAssignedBy() != null ? task.getAssignedBy() : "" %>">
</div>

<div class="form-field wide-field">
<label for="assignedTo">Assigned To</label>
<input type="text" id="assignedTo" name="assignedTo"
       value="<%= isEditMode && task.getAssignedTo() != null ? task.getAssignedTo() : "" %>">
</div>
</div>

<div class="form-row">
<div class="form-field wide-field">
<label for="difficultyRank">Difficulty Rank</label>
<select id="difficultyRank" name="difficultyRank">
    <% 
    List<Integer> difficultyRanks = (List<Integer>) request.getAttribute("difficultyRanks");
    if (difficultyRanks != null && !difficultyRanks.isEmpty()) {
        for (Integer rank : difficultyRanks) {
            boolean isSelected = isEditMode && rank == task.getDifficultyRank();
    %>
        <option value="<%= rank %>" <%= isSelected ? "selected" : "" %>><%= rank %></option>
    <% 
        }
    } else {
    %>
        <option value="1" <%= isEditMode && task.getDifficultyRank() == 1 ? "selected" : "" %>>1</option>
        <option value="2" <%= isEditMode && task.getDifficultyRank() == 2 ? "selected" : "" %>>2</option>
        <option value="3" <%= isEditMode && task.getDifficultyRank() == 3 ? "selected" : "" %>>3</option>
        <option value="4" <%= isEditMode && task.getDifficultyRank() == 4 ? "selected" : "" %>>4</option>
        <option value="5" <%= isEditMode && task.getDifficultyRank() == 5 ? "selected" : "" %>>5</option>
    <% } %>
</select>
</div>

<div class="form-field wide-field">
<label for="priorityRank">Priority Rank</label>
<select id="priorityRank" name="priorityRank">
    <% 
    List<Integer> priorityRanks = (List<Integer>) request.getAttribute("priorityRanks");
    if (priorityRanks != null && !priorityRanks.isEmpty()) {
        for (Integer rank : priorityRanks) {
            boolean isSelected = isEditMode && rank == task.getPriorityRank();
    %>
        <option value="<%= rank %>" <%= isSelected ? "selected" : "" %>><%= rank %></option>
    <% 
        }
    } else {
    %>
        <option value="1" <%= isEditMode && task.getPriorityRank() == 1 ? "selected" : "" %>>1</option>
        <option value="2" <%= isEditMode && task.getPriorityRank() == 2 ? "selected" : "" %>>2</option>
        <option value="3" <%= isEditMode && task.getPriorityRank() == 3 ? "selected" : "" %>>3</option>
        <option value="4" <%= isEditMode && task.getPriorityRank() == 4 ? "selected" : "" %>>4</option>
        <option value="5" <%= isEditMode && task.getPriorityRank() == 5 ? "selected" : "" %>>5</option>
    <% } %>
</select>
</div>
</div>

<div class="button-row">
<% if (isEditMode) { %>
    <button type="submit" class="update-btn" name="action" value="update">Update</button>
    <button type="submit" class="add-btn" name="action" value="add">Add as New</button>
    <button type="submit" class="delete-btn" name="action" value="delete" onclick="return confirm('Are you sure you want to delete this task?');">Delete</button>
<% } else { %>
    <button type="submit" class="add-btn" name="action" value="add">Add</button>
<% } %>
</div>

<div class="button-row view-row">
<a href="viewTask" class="view-btn">Go Back</a>
</div>
</form>
</main>

<script>
document.addEventListener('DOMContentLoaded', function() {
    console.log('Form loaded');
    
    document.querySelector('.task-form').addEventListener('submit', function(event) {
        console.log('Form submission triggered');
        
        var formData = {
            taskId: document.getElementById('taskId') ? document.getElementById('taskId').value : 'new',
            taskTitle: document.getElementById('taskTitle').value,
            status: document.getElementById('status').value,
            assignedDate: document.getElementById('assignedDate').value,
            dueDate: document.getElementById('dueDate').value,
            progress: document.getElementById('progress').value,
            assignedBy: document.getElementById('assignedBy').value,
            assignedTo: document.getElementById('assignedTo').value,
            difficultyRank: document.getElementById('difficultyRank').value,
            priorityRank: document.getElementById('priorityRank').value,
            action: event.submitter.value
        };
        
        console.log('Form data:', formData);
    });
});
</script>

</body>
</html>