package com.tasktrack.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tasktrack.config.DbConfig;
import com.tasktrack.model.UserModel;

public class UserService {
    
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
    
    public List<UserModel> getAllUsersSafe() {
        try {
            return getAllUsers();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in UserService.getAllUsers: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<UserModel> searchUsersSafe(String searchQuery) {
        try {
            return searchUsers(searchQuery);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in UserService.searchUsers: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<UserModel> filterUsersByTypeSafe(String userType) {
        try {
            return filterUsersByType(userType);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in UserService.filterUsersByType: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public boolean deleteUser(int userId) {
        Connection conn = null;
        try {
            conn = DbConfig.getDbConnection();
            conn.setAutoCommit(false);
            
            String deleteTasksSQL = "DELETE FROM user_task WHERE User_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteTasksSQL)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }
            String deleteUserSQL = "DELETE FROM user WHERE User_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteUserSQL)) {
                stmt.setInt(1, userId);
                int affected = stmt.executeUpdate();
                conn.commit();
                return affected > 0;
            }
        } catch (Exception e) {
            System.err.println("Error in UserService.deleteUser: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
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
    
    public String getUserImagePath(int userId) {
        String sql = "SELECT Image_path FROM user WHERE User_id = ?";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String imagePath = rs.getString("Image_path");
                System.out.println("Database Image_path: " + imagePath);
                
                if (imagePath != null && !imagePath.isEmpty() && !imagePath.equals("default-avatar.png")) {
                    return "resources/images/user/" + imagePath;
                }
            }
            
            return "resources/images/system/loginPageSide.png";
        } catch (Exception e) {
            System.err.println("Error in UserService.getUserImagePath: " + e.getMessage());
            e.printStackTrace();
            return "resources/images/system/loginPageSide.png";
        }
    }

    public UserModel getUserByUsername(String username) {
        String sql = "SELECT User_id, First_name, Last_name, Username, DOB, Gender, Email, " + 
                     "Phone_number, User_type FROM user WHERE Username = ?";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
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
            System.err.println("Error in UserService.getUserByUsername: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean updateUser(UserModel user) {
        if (getUserById(user.getId()) == null) {
            return false;
        }
        String sql;
        boolean updatePassword = user.getPassword() != null && !user.getPassword().isEmpty() && !user.getPassword().equals("********");
        boolean updateImage = user.getImageUrl() != null && !user.getImageUrl().isEmpty();
        if (updatePassword && updateImage) {
            sql = "UPDATE user SET First_name = ?, Last_name = ?, Username = ?, " +
                  "DOB = ?, Gender = ?, Email = ?, Phone_number = ?, " +
                  "Password = ?, User_type = ?, Image_path = ? WHERE User_id = ?";
        } else if (updatePassword) {
            sql = "UPDATE user SET First_name = ?, Last_name = ?, Username = ?, " +
                  "DOB = ?, Gender = ?, Email = ?, Phone_number = ?, " +
                  "Password = ?, User_type = ? WHERE User_id = ?";
        } else if (updateImage) {
            sql = "UPDATE user SET First_name = ?, Last_name = ?, Username = ?, " +
                  "DOB = ?, Gender = ?, Email = ?, Phone_number = ?, " +
                  "User_type = ?, Image_path = ? WHERE User_id = ?";
        } else {
            sql = "UPDATE user SET First_name = ?, Last_name = ?, Username = ?, " +
                  "DOB = ?, Gender = ?, Email = ?, Phone_number = ?, " +
                  "User_type = ? WHERE User_id = ?";
        }
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            stmt.setString(paramIndex++, user.getFirstName());
            stmt.setString(paramIndex++, user.getLastName());
            stmt.setString(paramIndex++, user.getUserName());
            if (user.getDob() != null) {
                stmt.setDate(paramIndex++, java.sql.Date.valueOf(user.getDob()));
            } else {
                stmt.setNull(paramIndex++, java.sql.Types.DATE);
            }
            stmt.setString(paramIndex++, user.getGender());
            stmt.setString(paramIndex++, user.getEmail());
            stmt.setString(paramIndex++, user.getNumber());
            if (updatePassword) {
                stmt.setString(paramIndex++, user.getPassword());
            }
            stmt.setString(paramIndex++, user.getUserType());
            if (updateImage) {
                stmt.setString(paramIndex++, user.getImageUrl());
            }
            stmt.setInt(paramIndex, user.getId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            return false;
        }
    }
}