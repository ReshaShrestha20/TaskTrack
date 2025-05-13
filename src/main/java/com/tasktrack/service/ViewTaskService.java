package com.tasktrack.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tasktrack.config.DbConfig;
import com.tasktrack.model.TaskModel;

/**
 * Service class for retrieving and searching tasks
 */
public class ViewTaskService {
    
    /**
     * Get all tasks from the database
     * 
     * @return List of tasks
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<TaskModel> getAllTasks() throws SQLException, ClassNotFoundException {
        List<TaskModel> tasks = new ArrayList<>();
        
        String sql = "SELECT Task_id, Task_title, Assigned_date, Due_date, Assigned_by, Assigned_to, " +
                     "Progress, Status, Difficulty_rank, Priority_rank FROM task ORDER BY Task_id DESC";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                TaskModel task = createTaskFromResultSet(rs);
                tasks.add(task);
            }
        }
        
        return tasks;
    }
    
    /**
     * Search tasks by title using LIKE pattern matching
     * 
     * @param searchQuery The search term
     * @return List of matching tasks
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<TaskModel> searchTasksByTitle(String searchQuery) throws SQLException, ClassNotFoundException {
        List<TaskModel> tasks = new ArrayList<>();
        if (searchQuery == null) {
            return getAllTasks();
        }
        searchQuery = searchQuery.trim();
        if (searchQuery.isEmpty()) {
            return getAllTasks();
        }
        String sql = "SELECT Task_id, Task_title, Assigned_date, Due_date, Assigned_by, Assigned_to, " +
                     "Progress, Status, Difficulty_rank, Priority_rank FROM task " +
                     "WHERE Task_title LIKE ? ORDER BY Task_id DESC";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + searchQuery + "%");
            ResultSet rs = stmt.executeQuery(); 
            while (rs.next()) {
                TaskModel task = createTaskFromResultSet(rs);
                tasks.add(task);
            };
        }
        
        return tasks;
    }
    
    /**
     * Get all tasks with error handling and default empty list
     * 
     * @return List of tasks or empty list if database error occurs
     */
    public List<TaskModel> getAllTasksSafe() {
        try {
            return getAllTasks();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in ViewTaskService.getAllTasks: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Search tasks by title with error handling and default empty list
     * 
     * @param searchQuery The search term
     * @return List of matching tasks or empty list if database error occurs
     */
    public List<TaskModel> searchTasksByTitleSafe(String searchQuery) {
        try {
            return searchTasksByTitle(searchQuery);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in ViewTaskService.searchTasksByTitle: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Get a task by its ID
     * 
     * @param taskId The task ID to look up
     * @return The task model, or null if not found
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public TaskModel getTaskById(int taskId) throws SQLException, ClassNotFoundException {
        TaskModel task = null;
        
        String sql = "SELECT Task_id, Task_title, Assigned_date, Due_date, Assigned_by, Assigned_to, " +
                     "Progress, Status, Difficulty_rank, Priority_rank FROM task " +
                     "WHERE Task_id = ?";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                task = createTaskFromResultSet(rs);
            }
        }
        
        return task;
    }
    
    /**
     * Get a task by ID with error handling
     * 
     * @param taskId The task ID to look up
     * @return The task model, or null if not found or if an error occurs
     */
    public TaskModel getTaskByIdSafe(int taskId) {
        try {
            return getTaskById(taskId);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in ViewTaskService.getTaskById: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Helper method to create a TaskModel from a ResultSet row
     * Reduces code duplication in the service methods
     */
    private TaskModel createTaskFromResultSet(ResultSet rs) throws SQLException {
        TaskModel task = new TaskModel();
        
        task.setTaskId(rs.getInt("Task_id"));
        task.setTaskTitle(rs.getString("Task_title"));
        
        java.sql.Date assignedDate = rs.getDate("Assigned_date");
        if (assignedDate != null) {
            task.setAssignedDate(assignedDate.toLocalDate());
        }
        
        java.sql.Date dueDate = rs.getDate("Due_date");
        if (dueDate != null) {
            task.setDueDate(dueDate.toLocalDate());
        }
        
        task.setAssignedBy(rs.getString("Assigned_by"));
        task.setAssignedTo(rs.getString("Assigned_to"));
        task.setProgress(rs.getString("Progress"));
        task.setStatus(rs.getString("Status"));
        task.setDifficultyRank(rs.getInt("Difficulty_rank"));
        task.setPriorityRank(rs.getInt("Priority_rank"));
        
        return task;
    }
}