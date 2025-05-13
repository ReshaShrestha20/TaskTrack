package com.tasktrack.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tasktrack.config.DbConfig;
import com.tasktrack.model.UserModel;

/**
 * Service class for retrieving and managing user data
 */
public class UserService {
    
    /**
     * Get all users from the database
     * 
     * @return List of users
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<UserModel> getAllUsers() throws SQLException, ClassNotFoundException {
        List<UserModel> users = new ArrayList<>();
        
        String sql = "SELECT User_id, First_name, Last_name, Username, DOB, Gender, Email, " + 
                     "Phone_number, User_type FROM user ORDER BY User_id DESC";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                UserModel user = new UserModel();
                user.setId(rs.getInt("User_id"));
                user.setFirstName(rs.getString("First_name"));
                user.setLastName(rs.getString("Last_name"));
                user.setUserName(rs.getString("Username"));
                
                java.sql.Date dobDate = rs.getDate("DOB");
                if (dobDate != null) {
                    user.setDob(dobDate.toLocalDate());
                }
                
                user.setGender(rs.getString("Gender"));
                user.setEmail(rs.getString("Email"));
                user.setNumber(rs.getString("Phone_number"));
                user.setUserType(rs.getString("User_type"));
                
                users.add(user);
            }
        }
        
        return users;
    }
    
    /**
     * Search users by name or username
     * 
     * @param searchQuery The search term
     * @return List of matching users
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<UserModel> searchUsers(String searchQuery) throws SQLException, ClassNotFoundException {
        List<UserModel> users = new ArrayList<>();
        
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return getAllUsers();
        }
        searchQuery = searchQuery.trim();
        
        String sql = "SELECT User_id, First_name, Last_name, Username, DOB, Gender, Email, " + 
                     "Phone_number, User_type FROM user " +
                     "WHERE First_name LIKE ? OR Last_name LIKE ? OR Username LIKE ? OR Email LIKE ? " +
                     "ORDER BY User_id DESC";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String likeParam = "%" + searchQuery + "%";
            stmt.setString(1, likeParam);
            stmt.setString(2, likeParam);
            stmt.setString(3, likeParam);
            stmt.setString(4, likeParam);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                UserModel user = new UserModel();
                user.setId(rs.getInt("User_id"));
                user.setFirstName(rs.getString("First_name"));
                user.setLastName(rs.getString("Last_name"));
                user.setUserName(rs.getString("Username"));
                
                java.sql.Date dobDate = rs.getDate("DOB");
                if (dobDate != null) {
                    user.setDob(dobDate.toLocalDate());
                }
                
                user.setGender(rs.getString("Gender"));
                user.setEmail(rs.getString("Email"));
                user.setNumber(rs.getString("Phone_number"));
                user.setUserType(rs.getString("User_type"));
                
                users.add(user);
            }
        }
        
        return users;
    }
    
    /**
     * Filter users by user type
     * 
     * @param userType The user type to filter by
     * @return List of users with the specified type
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<UserModel> filterUsersByType(String userType) throws SQLException, ClassNotFoundException {
        List<UserModel> users = new ArrayList<>();
        
        if (userType == null || userType.trim().isEmpty() || userType.equalsIgnoreCase("all")) {
            return getAllUsers();
        }
        
        String sql = "SELECT User_id, First_name, Last_name, Username, DOB, Gender, Email, " + 
                     "Phone_number, User_type FROM user " +
                     "WHERE User_type = ? ORDER BY User_id DESC";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userType);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                UserModel user = new UserModel();
                user.setId(rs.getInt("User_id"));
                user.setFirstName(rs.getString("First_name"));
                user.setLastName(rs.getString("Last_name"));
                user.setUserName(rs.getString("Username"));
                
                java.sql.Date dobDate = rs.getDate("DOB");
                if (dobDate != null) {
                    user.setDob(dobDate.toLocalDate());
                }
                
                user.setGender(rs.getString("Gender"));
                user.setEmail(rs.getString("Email"));
                user.setNumber(rs.getString("Phone_number"));
                user.setUserType(rs.getString("User_type"));
                
                users.add(user);
            }
        }
        
        return users;
    }
    
    /**
     * Get all users with error handling and default empty list
     * 
     * @return List of users or empty list if database error occurs
     */
    public List<UserModel> getAllUsersSafe() {
        try {
            return getAllUsers();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in UserService.getAllUsers: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Search users with error handling and default empty list
     * 
     * @param searchQuery The search term
     * @return List of matching users or empty list if database error occurs
     */
    public List<UserModel> searchUsersSafe(String searchQuery) {
        try {
            return searchUsers(searchQuery);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in UserService.searchUsers: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Filter users by type with error handling and default empty list
     * 
     * @param userType The user type to filter by
     * @return List of users with the specified type or empty list if database error occurs
     */
    public List<UserModel> filterUsersByTypeSafe(String userType) {
        try {
            return filterUsersByType(userType);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in UserService.filterUsersByType: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Delete a user by ID
     * 
     * @param userId The ID of the user to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM user WHERE User_id = ?";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            int affected = stmt.executeUpdate();
            
            return affected > 0;
        } catch (Exception e) {
            System.err.println("Error in UserService.deleteUser: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get a single user by ID
     * 
     * @param userId The ID of the user to retrieve
     * @return The user model or null if not found
     */
    public UserModel getUserById(int userId) {
        String sql = "SELECT User_id, First_name, Last_name, Username, DOB, Gender, Email, " + 
                     "Phone_number, User_type FROM user WHERE User_id = ?";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                UserModel user = new UserModel();
                user.setId(rs.getInt("User_id"));
                user.setFirstName(rs.getString("First_name"));
                user.setLastName(rs.getString("Last_name"));
                user.setUserName(rs.getString("Username"));
                
                java.sql.Date dobDate = rs.getDate("DOB");
                if (dobDate != null) {
                    user.setDob(dobDate.toLocalDate());
                }
                
                user.setGender(rs.getString("Gender"));
                user.setEmail(rs.getString("Email"));
                user.setNumber(rs.getString("Phone_number"));
                user.setUserType(rs.getString("User_type"));
                
                return user;
            }
            
            return null;
        } catch (Exception e) {
            System.err.println("Error in UserService.getUserById: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Update an existing user
     * 
     * @param user The updated user data
     * @return true if successful, false otherwise
     */
    public boolean updateUser(UserModel user) {
        if (getUserById(user.getId()) == null) {
            return false;
        }
        String sql;
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            sql = "UPDATE user SET First_name = ?, Last_name = ?, Username = ?, " +
                  "DOB = ?, Gender = ?, Email = ?, Phone_number = ?, " +
                  "Password = ?, User_type = ? WHERE User_id = ?";
        } else {
            sql = "UPDATE user SET First_name = ?, Last_name = ?, Username = ?, " +
                  "DOB = ?, Gender = ?, Email = ?, Phone_number = ?, " +
                  "User_type = ? WHERE User_id = ?";
        }
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getUserName());
            
            if (user.getDob() != null) {
                stmt.setDate(4, java.sql.Date.valueOf(user.getDob()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }
            
            stmt.setString(5, user.getGender());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.getNumber());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                stmt.setString(8, user.getPassword());
                stmt.setString(9, user.getUserType());
                stmt.setInt(10, user.getId());
            } else {
                stmt.setString(8, user.getUserType());
                stmt.setInt(9, user.getId());
            }
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}