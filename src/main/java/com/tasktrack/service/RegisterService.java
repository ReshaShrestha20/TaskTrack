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

    public Boolean addUser(UserModel userModel) {
        Connection dbConn = null;
        
        try {
            dbConn = DbConfig.getDbConnection();
            if (dbConn == null) {
                System.err.println("Database connection is not available.");
                return null;
            }
            
            dbConn.setAutoCommit(false);
            
            String projectQuery = "SELECT Project_id FROM project WHERE Project_name = ?";
            int projectId = 1; 
            
            try (PreparedStatement projectStmt = dbConn.prepareStatement(projectQuery)) {
                projectStmt.setString(1, userModel.getProject().getName());
                System.out.println("Executing project query with value: " + userModel.getProject().getName());
                ResultSet result = projectStmt.executeQuery();
                if (result.next()) {
                    projectId = result.getInt("Project_id");
                    System.out.println("Found project ID: " + projectId);
                } else {
                    System.out.println("Project not found, using default ID: " + projectId);
                }
            }

            String insertQuery = "INSERT INTO user (First_name, Last_name, DOB, Gender, Email, Phone_number, User_type, Username, Password) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                
                System.out.println("Executing insert query with values:");
                System.out.println("First name: " + userModel.getFirstName());
                System.out.println("Last name: " + userModel.getLastName());
                System.out.println("DOB: " + userModel.getDob());
                System.out.println("Gender: " + userModel.getGender());
                System.out.println("Email: " + userModel.getEmail());
                System.out.println("Phone: " + userModel.getNumber());
                System.out.println("User type: " + (userModel.getUserType() != null ? userModel.getUserType() : "User"));
                System.out.println("Username: " + userModel.getUserName());
                
                int result = insertStmt.executeUpdate();
                System.out.println("Insert result: " + result);
                
                if (result > 0) {
                    String userIdQuery = "SELECT User_id FROM user WHERE Username = ?";
                    try (PreparedStatement userIdStmt = dbConn.prepareStatement(userIdQuery)) {
                        userIdStmt.setString(1, userModel.getUserName());
                        ResultSet userIdResult = userIdStmt.executeQuery();
                        if (userIdResult.next()) {
                            int userId = userIdResult.getInt("User_id");
                            
                            String userProjectQuery = "INSERT INTO user_project_task (User_id, Project_id) VALUES (?, ?)";
                            try (PreparedStatement userProjectStmt = dbConn.prepareStatement(userProjectQuery)) {
                                userProjectStmt.setInt(1, userId);
                                userProjectStmt.setInt(2, projectId);
                                userProjectStmt.executeUpdate();
                                System.out.println("User-Project relation created");
                            }
                        }
                    }
                }
                
                dbConn.commit();
                return result > 0;
            }
        } catch (SQLException e) {
            try {
                System.err.println("Error during user registration: " + e.getMessage());
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