package com.tasktrack.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.tasktrack.config.DbConfig;
import com.tasktrack.model.UserModel;

public class RegisterService {

    public RegisterService() {
        try {
            Connection testConn = DbConfig.getDbConnection();
            if (testConn != null) {
                System.out.println("Database connection test successful");
                testConn.close();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database connection error during initialization: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    // Method to check if username already exists
    public boolean usernameExists(String username) {
        Connection dbConn = null;
        try {
            dbConn = DbConfig.getDbConnection();
            if (dbConn == null) {
                return false; // Cannot determine, assume it doesn't exist
            }
            
            String query = "SELECT COUNT(*) FROM user WHERE Username = ?";
            try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error checking username existence: " + e.getMessage());
        } finally {
            if (dbConn != null) {
                try {
                    dbConn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
        return false;
    }
    
    // Method to check if email already exists
    public boolean emailExists(String email) {
        Connection dbConn = null;
        try {
            dbConn = DbConfig.getDbConnection();
            if (dbConn == null) {
                return false; // Cannot determine, assume it doesn't exist
            }
            
            String query = "SELECT COUNT(*) FROM user WHERE Email = ?";
            try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        } finally {
            if (dbConn != null) {
                try {
                    dbConn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
        return false;
    }

    public Boolean addUser(UserModel userModel) {
        Connection dbConn = null;
        try {
            dbConn = DbConfig.getDbConnection();
            if (dbConn == null) {
                return null;
            }            
            dbConn.setAutoCommit(false);
            int taskId = 1;
            try (PreparedStatement taskStmt = dbConn.prepareStatement("SELECT Task_id FROM task LIMIT 1")) {
                ResultSet taskResult = taskStmt.executeQuery();
                if (taskResult.next()) {
                    taskId = taskResult.getInt("Task_id");
                } else {
                    System.out.println("No tasks found, using default ID: " + taskId);
                }
            } catch (SQLException e) {
                System.err.println("Error getting task ID: " + e.getMessage());
            }
            String insertQuery = "INSERT INTO user (First_name, Last_name, DOB, Gender, Email, Phone_number, User_type, Username, Password, Image_path) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, userModel.getFirstName());
                insertStmt.setString(2, userModel.getLastName());
                insertStmt.setDate(3, Date.valueOf(userModel.getDob()));
                insertStmt.setString(4, userModel.getGender());
                insertStmt.setString(5, userModel.getEmail());
                insertStmt.setString(6, userModel.getNumber());
                insertStmt.setString(7, userModel.getUserType() != null ? userModel.getUserType() : "User");
                insertStmt.setString(8, userModel.getUserName());
                insertStmt.setString(9, userModel.getPassword());
                insertStmt.setString(10, userModel.getImageUrl());
              
                int result = insertStmt.executeUpdate();
                if (result > 0) {
                    String userIdQuery = "SELECT User_id FROM user WHERE Username = ?";
                    try (PreparedStatement userIdStmt = dbConn.prepareStatement(userIdQuery)) {
                        userIdStmt.setString(1, userModel.getUserName());
                        ResultSet userIdResult = userIdStmt.executeQuery();
                        if (userIdResult.next()) {
                            int userId = userIdResult.getInt("User_id");
                            
                            String userTaskQuery = "INSERT INTO user_task (User_id, Task_id) VALUES (?, ?)";
                            try (PreparedStatement userTaskStmt = dbConn.prepareStatement(userTaskQuery)) {
                                userTaskStmt.setInt(1, userId);
                                userTaskStmt.setInt(2, taskId);
                                userTaskStmt.executeUpdate();
                            }
                        }
                    }
                }
                dbConn.commit();
                return result > 0;
            }
        } catch (SQLException e) {
            try {
                System.err.println("SQL Error during user registration: " + e.getMessage());
                e.printStackTrace();
                if (dbConn != null) {
                    dbConn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("Database driver not found: " + e.getMessage());
            return null;
        } finally {
            if (dbConn != null) {
                try {
                    dbConn.setAutoCommit(true);
                    dbConn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}