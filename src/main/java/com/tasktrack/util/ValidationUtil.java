package com.tasktrack.util;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;
import jakarta.servlet.http.Part;

public class ValidationUtil {
    // 1. Validate if a field is null or empty
    public boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    // 2. Validate if a string contains only letters (updated to allow spaces)
    public boolean isAlphabetic(String value) {
        return !isNullOrEmpty(value) && value.matches("^[a-zA-Z\\s]+$");
    }
    
    // 3. Validate if a string starts with a letter and is composed of letters and numbers
    public boolean isAlphanumericStartingWithLetter(String value) {
        return !isNullOrEmpty(value) && value.matches("^[a-zA-Z][a-zA-Z0-9]*$");
    }
    
    // 4. Validate if a string is "male" or "female" or "other" (case insensitive)
    public boolean isValidGender(String value) {
        return !isNullOrEmpty(value) && (value.equalsIgnoreCase("male") || 
               value.equalsIgnoreCase("female") || value.equalsIgnoreCase("other"));
    }
    
    // 5. Validate if a string is a valid email address
    public boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return !isNullOrEmpty(email) && Pattern.matches(emailRegex, email);
    }
    
    // 6. Validate if a number is of 10 digits
    public boolean isValidPhoneNumber(String number) {
        return !isNullOrEmpty(number) && number.matches("^\\d{10}$");
    }
    
    // 7. Validate if a password is composed of at least 1 capital letter, 1 number, and 1 symbol
    public boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return !isNullOrEmpty(password) && password.matches(passwordRegex);
    }
    
    // 8. Validate if a Part's file extension matches with image extensions (jpg, jpeg, png, gif)
    public boolean isValidImageExtension(Part imagePart) {
        if (imagePart == null || imagePart.getSubmittedFileName() == null || 
            imagePart.getSubmittedFileName().trim().isEmpty()) {
            return true; // Image is optional, so return true if not provided
        }
        String fileName = imagePart.getSubmittedFileName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")
                || fileName.endsWith(".gif");
    }
    
    // 9. Validate if password and retype password match
    public boolean doPasswordsMatch(String password, String retypePassword) {
        return !isNullOrEmpty(password) && !isNullOrEmpty(retypePassword) && password.equals(retypePassword);
    }
    
    // 10. Validate if the date of birth is at least 16 years before today
    public boolean isAgeAtLeast16(LocalDate dob) {
        if (dob == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return Period.between(dob, today).getYears() >= 16;
    }
    
    // 11. Check if date is in the future
    public boolean isFutureDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(LocalDate.now());
    }
    
    // 12. Check if name contains only letters and spaces (no special characters)
    public boolean hasSpecialCharacters(String value) {
        return !isNullOrEmpty(value) && !value.matches("^[a-zA-Z\\s]+$");
    }
}