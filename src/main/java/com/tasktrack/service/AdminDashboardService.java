package com.tasktrack.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tasktrack.config.DbConfig;
import com.tasktrack.model.ProjectModel;
import com.tasktrack.model.UserModel;

public class AdminDashboardService {
    
    /**
     * Get the total count of regular users from the database
     * 
     * @return The count of users
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int getUserCount() throws SQLException, ClassNotFoundException {
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM user WHERE User_type = 'User'")) {
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    /**
     * Get the total count of admins from the database
     * 
     * @return The count of admins
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int getAdminCount() throws SQLException, ClassNotFoundException {
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM user WHERE User_type = 'Admin'")) {
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    

    /**
     * Get the total count of tasks from the database
     * 
     * @return The count of tasks
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int getTaskCount() throws SQLException, ClassNotFoundException {
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM task")) {
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    /**
     * Get a list of recently added users from the database
     * 
     * @param limit The maximum number of users to retrieve
     * @return List of recently added users
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<UserModel> getRecentUsers(int limit) throws SQLException, ClassNotFoundException {
        List<UserModel> recentUsers = new ArrayList<>();
        
        String sql = "SELECT User_id, First_name, Last_name, Username, DOB, Gender, Email, Phone_number, User_type " +
                     "FROM user " +
                     "WHERE User_type = 'User' " +
                     "ORDER BY User_id DESC LIMIT ?";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                UserModel user = new UserModel();
                user.setId(rs.getInt("User_id"));
                user.setFirstName(rs.getString("First_name"));
                user.setLastName(rs.getString("Last_name"));
                user.setUserName(rs.getString("Username"));
                
                java.sql.Date sqlDate = rs.getDate("DOB");
                if (sqlDate != null) {
                    user.setDob(sqlDate.toLocalDate());
                }
                
                user.setGender(rs.getString("Gender"));
                user.setEmail(rs.getString("Email"));
                user.setNumber(rs.getString("Phone_number"));
                
                recentUsers.add(user);
            }
        }
        
        return recentUsers;
    }
    
    /**
     * Handle database errors gracefully by providing default values when needed
     * Used when a database operation fails but we still want to show the dashboard
     * 
     * @param e The exception that occurred
     * @param operation The name of the operation that failed
     * @param defaultValue The default value to return
     * @return The default value
     */
    private int handleDatabaseError(Exception e, String operation, int defaultValue) {
        System.err.println("Error in AdminDashboardService." + operation + ": " + e.getMessage());
        e.printStackTrace();
        return defaultValue;
    }
    
    /**
     * Get user count with error handling and default value
     * 
     * @return User count or default value if database error occurs
     */
    public int getUserCountSafe() {
        try {
            return getUserCount();
        } catch (SQLException | ClassNotFoundException e) {
            return handleDatabaseError(e, "getUserCount", 0);
        }
    }
    
    /**
     * Get admin count with error handling and default value
     * 
     * @return Admin count or default value if database error occurs
     */
    public int getAdminCountSafe() {
        try {
            return getAdminCount();
        } catch (SQLException | ClassNotFoundException e) {
            return handleDatabaseError(e, "getAdminCount", 0);
        }
    }
    
  
    
    /**
     * Get task count with error handling and default value
     * 
     * @return Task count or default value if database error occurs
     */
    public int getTaskCountSafe() {
        try {
            return getTaskCount();
        } catch (SQLException | ClassNotFoundException e) {
            return handleDatabaseError(e, "getTaskCount", 0);
        }
    }
    
    /**
     * Get recent users with error handling and default empty list
     * 
     * @param limit Maximum number of users to retrieve
     * @return List of users or empty list if database error occurs
     */
    public List<UserModel> getRecentUsersSafe(int limit) {
        try {
            return getRecentUsers(limit);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in AdminDashboardService.getRecentUsers: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}