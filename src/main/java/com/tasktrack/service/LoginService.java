package com.tasktrack.service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.tasktrack.config.DbConfig;
import com.tasktrack.model.UserModel;
import com.tasktrack.util.PasswordUtil;
/**
 * Service class for handling login operations. Connects to the database,
 * verifies user credentials, and returns login status.
 */
public class LoginService {
	private Connection dbConn;
	private boolean isConnectionError = false;
	public LoginService() {
		try {
			dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			isConnectionError = true;
		}
	}
	
	/**
	 * Validates the user credentials against the database records.
	 *
	 * @param userModel the UserModel object containing user credentials
	 * @return true if the user credentials are valid, false otherwise; null if a
	 *         connection error occurs
	 */
	public Boolean loginUser(UserModel userModel) {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return null;
		}
		String query = "SELECT Username, Password FROM user WHERE Username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, userModel.getUserName());
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return validatePassword(result, userModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return false;
	}
	
	/**
	 * Retrieves the user's role from the database.
	 * 
	 * @param username The username to look up
	 * @return The user's role ("Admin" or "User"), or null if not found or error occurs
	 */
	public String getUserRole(String username) {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return null;
		}
		
		String query = "SELECT User_type FROM user WHERE Username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, username);
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				String userType = result.getString("User_type");
				
				return userType;
			} 
		} catch (SQLException e) {
			System.out.println("SQL error in getUserRole: " + e.getMessage()); 
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Validates the password retrieved from the database.
	 *
	 * @param result     the ResultSet containing the username and password from
	 *                   the database
	 * @param userModel the UserModel object containing user credentials
	 * @return true if the passwords match, false otherwise
	 * @throws SQLException if a database access error occurs
	 */
	private boolean validatePassword(ResultSet result, UserModel userModel) throws SQLException {
		String dbUsername = result.getString("username");
		String dbPassword = result.getString("password");
		return dbUsername.equals(userModel.getUserName())
				&& PasswordUtil.decrypt(dbPassword, dbUsername).equals(userModel.getPassword());
	}
}