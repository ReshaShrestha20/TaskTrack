package com.tasktrack.service;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import com.tasktrack.config.DbConfig;
import com.tasktrack.model.TaskModel;

public class TaskService {
    
    public boolean testConnection() {
        try (Connection conn = DbConfig.getDbConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
   
    public Boolean addTask(TaskModel taskModel) {
        System.out.println("Starting addTask with model: " + taskModel.getTaskTitle());
        String sql = "INSERT INTO task (Task_title, Status, Assigned_date, Due_date, " +
                     "Progress, Assigned_by, Assigned_to, Difficulty_rank, Priority_rank) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConfig.getDbConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, taskModel.getTaskTitle());
                pstmt.setString(2, taskModel.getStatus());
                if (taskModel.getAssignedDate() != null) {
                    pstmt.setDate(3, Date.valueOf(taskModel.getAssignedDate()));
                } else {
                    pstmt.setDate(3, Date.valueOf(LocalDate.now()));
                }
                
                if (taskModel.getDueDate() != null) {
                    pstmt.setDate(4, Date.valueOf(taskModel.getDueDate()));
                } else {
                    pstmt.setDate(4, Date.valueOf(LocalDate.now()));
                }
                pstmt.setString(5, taskModel.getProgress() != null ? taskModel.getProgress() : "0");
                pstmt.setString(6, taskModel.getAssignedBy() != null ? taskModel.getAssignedBy() : "");
                pstmt.setString(7, taskModel.getAssignedTo() != null ? taskModel.getAssignedTo() : "");
                pstmt.setInt(8, taskModel.getDifficultyRank());
                pstmt.setInt(9, taskModel.getPriorityRank());
                int affected = pstmt.executeUpdate();
                return affected > 0;
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("General Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public Boolean updateTask(TaskModel taskModel) {
        String sql = "UPDATE task SET Task_title = ?, Status = ?, Assigned_date = ?, Due_date = ?, " +
                     "Progress = ?, Assigned_by = ?, Assigned_to = ?, Difficulty_rank = ?, Priority_rank = ? " +
                     "WHERE Task_id = ?";
        
        try (Connection conn = DbConfig.getDbConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, taskModel.getTaskTitle());
                pstmt.setString(2, taskModel.getStatus());
                if (taskModel.getAssignedDate() != null) {
                    pstmt.setDate(3, Date.valueOf(taskModel.getAssignedDate()));
                } else {
                    pstmt.setDate(3, Date.valueOf(LocalDate.now()));
                }
                
                if (taskModel.getDueDate() != null) {
                    pstmt.setDate(4, Date.valueOf(taskModel.getDueDate()));
                } else {
                    pstmt.setDate(4, Date.valueOf(LocalDate.now()));
                }
                pstmt.setString(5, taskModel.getProgress() != null ? taskModel.getProgress() : "0");
                pstmt.setString(6, taskModel.getAssignedBy() != null ? taskModel.getAssignedBy() : "");
                pstmt.setString(7, taskModel.getAssignedTo() != null ? taskModel.getAssignedTo() : "");
                pstmt.setInt(8, taskModel.getDifficultyRank());
                pstmt.setInt(9, taskModel.getPriorityRank());
                pstmt.setInt(10, taskModel.getTaskId());
                int affected = pstmt.executeUpdate();
                return affected > 0;
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception during update: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("General Exception during update: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public Boolean deleteTask(int taskId) {
        String sql = "DELETE FROM task WHERE Task_id = ?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("SQL Exception during delete: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("General Exception during delete: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}