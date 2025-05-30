package com.tasktrack.model;

import java.time.LocalDate;

/**
 * Model class for User data
 */
public class UserModel {
    private int id;
    private String firstName;
    private String lastName;
    private String userName;
    private LocalDate dob;
    private String gender;
    private String email;
    private String number; // Phone number
    private String password;
    private String userType;
    private ProjectModel project;
    private String imageUrl; // Field name matches the getter method used in service

    /**
     * Default constructor
     */
    public UserModel() {
    }

    /**
     * Full constructor with ID
     */
    public UserModel(int id, String firstName, String lastName, String userName, LocalDate dob, String gender,
            String email, String number, String password, String userType, ProjectModel project, String imageUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.number = number;
        this.password = password;
        this.userType = userType;
        this.project = project;
        this.imageUrl = imageUrl;
    }

    /**
     * Constructor without ID (for new users) with image
     */
    public UserModel(String firstName, String lastName, String userName, LocalDate dob, String gender, String email,
            String number, String password, String userType, ProjectModel project, String imageUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.number = number;
        this.password = password;
        this.userType = userType;
        this.project = project;
        this.imageUrl = imageUrl;
    }

    /**
     * Constructor without ID (for new users) without image
     */
    public UserModel(String firstName, String lastName, String userName, LocalDate dob, String gender, String email,
            String number, String password, String userType, ProjectModel project) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.number = number;
        this.password = password;
        this.userType = userType;
        this.project = project;
        this.imageUrl = "default-avatar.png"; // Default image
    }

    /**
     * Login credentials constructor
     */
    public UserModel(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * Simple constructor for basic user info
     */
    public UserModel(int id, String firstName, String lastName, String email, String number) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.number = number;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUsername() {
        return userName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhoneNumber() {
        return number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public ProjectModel getProject() {
        return project;
    }

    public void setProject(ProjectModel project) {
        this.project = project;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    
    public String getImage_path() {
        return imageUrl;
    }
}