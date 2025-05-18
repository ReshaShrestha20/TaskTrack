package com.tasktrack.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig {
    private static final String DB_NAME = "tasktrack";
    private static final String URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    public static Connection getDbConnection() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully");
            
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connection established successfully");
            
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found: " + e.getMessage());
            throw e;
        } 
    }
}