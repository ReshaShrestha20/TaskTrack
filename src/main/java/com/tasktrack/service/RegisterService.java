package com.tasktrack.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.tasktrack.config.DbConfig;
import com.tasktrack.model.UserModel;

public class RegisterService {

    private Connection dbConn;

    public RegisterService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database connection error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Boolean addUser(UserModel userModel) {
        if (dbConn == null) {
            System.err.println("Database connection is not available.");
            return null;
        }

        String projectQuery = "SELECT Project_id FROM Project WHERE Project_name = ?";
        String insertQuery = "INSERT INTO User (First_name, Last_name, Username, DOB, Gender, Email, Phone_number, Password, User_type) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            dbConn.setAutoCommit(false);
            
            int projectId = 1;
            try (PreparedStatement projectStmt = dbConn.prepareStatement(projectQuery)) {
                projectStmt.setString(1, userModel.getProject().getName());
                System.out.println("Executing project query: " + projectQuery + " with value: " + userModel.getProject().getName());
                ResultSet result = projectStmt.executeQuery();
                if (result.next()) {
                    projectId = result.getInt("Project_id");
                    System.out.println("Found project ID: " + projectId);
                } else {
                    System.out.println("Project not found, using default ID: " + projectId);
                }
            }

            // Inserting the user
            try (PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, userModel.getFirstName());
                insertStmt.setString(2, userModel.getLastName());
                insertStmt.setString(3, userModel.getUserName());
                insertStmt.setDate(4, Date.valueOf(userModel.getDob()));
                insertStmt.setString(5, userModel.getGender());
                insertStmt.setString(6, userModel.getEmail());
                insertStmt.setString(7, userModel.getNumber());
                insertStmt.setString(8, userModel.getPassword());
                insertStmt.setString(9, "User");
                
                System.out.println("Executing insert query: " + insertQuery);
                int result = insertStmt.executeUpdate();
                System.out.println("Insert result: " + result);
                
                // Inserting into User_Project table
                if (result > 0) {
                    String userIdQuery = "SELECT User_id FROM User WHERE Username = ?";
                    try (PreparedStatement userIdStmt = dbConn.prepareStatement(userIdQuery)) {
                        userIdStmt.setString(1, userModel.getUserName());
                        ResultSet userIdResult = userIdStmt.executeQuery();
                        if (userIdResult.next()) {
                            int userId = userIdResult.getInt("User_id");
                            
                            String userProjectQuery = "INSERT INTO User_Project (User_id, Project_id) VALUES (?, ?)";
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
                dbConn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            return null;
        } finally {
            try {
                dbConn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
}