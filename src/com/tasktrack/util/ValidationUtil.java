/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tasktrack.util;

import java.util.regex.Pattern;

/**
 *
 * @author SUPRIYA
 */
public class ValidationUtil {

    // Regular expression patterns
    private static final Pattern STRING_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");
    private static final Pattern TASKID_PATTERN = Pattern.compile("^\\d{5}$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final Pattern CONTACT_PATTERN = Pattern.compile("^98\\d{8}$");
    private static final Pattern PROGRESS_PATTERN = Pattern.compile("^\\d{2}%$");

    /**
     * Validates if a string is null or empty.
     *
     * 
     * @param value the string to validate
     * @return true if the string is null or empty, otherwise false
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validates if the name contains only alphabets and spaces.
     *
     * @param name the name to validate
     * @return true if valid, otherwise false
     */
    public static boolean isValidString(String name) {
        return !isNullOrEmpty(name) && STRING_PATTERN.matcher(name).matches();
    }
    
    public static boolean isValidProgress(String progress) {
        return !isNullOrEmpty(progress) && PROGRESS_PATTERN.matcher(progress).matches();
    }
    
     public static boolean isValidDate(String date) {
        return !isNullOrEmpty(date) && DATE_PATTERN.matcher(date).matches();
    }

    /**
     * Validates if the LMU ID is exactly 7 digits.
     *
     * @param lmuId the LMU ID to validate
     * @return true if valid, otherwise false
     */
    public static boolean isValidTaskId(String lmuId) {
        return !isNullOrEmpty(lmuId) && TASKID_PATTERN.matcher(lmuId).matches();
    }

   

    /**
     * Validates if the contact number starts with 98 and has 10 digits in total.
     *
     * @param contact the contact number to validate
     * @return true if valid, otherwise false
     */
    public static boolean isValidContact(String contact) {
        return !isNullOrEmpty(contact) && CONTACT_PATTERN.matcher(contact.toLowerCase()).matches();
    }

    /**
     * Validates if the age is between 18 and 70 (inclusive).
     *
     * @param age the age to validate
     * @return true if valid, otherwise false
     */
    public static boolean isValidAge(short age) {
        return age >= 18 && age <= 70;
    }

    /**
     * Generic field validation utility that checks for non-empty and specific criteria.
     *
     * @param value      the field value to validate
     * @param isCriteria the specific criteria to validate against
     * @return true if both non-empty and criteria validation pass, otherwise false
     */
    public static boolean validateField(String value, boolean isCriteria) {
        return !isNullOrEmpty(value) && isCriteria;
    }

    /**
     * Utility to parse and validate an age value.
     *
     * @param ageText the text representing age
     * @return true if valid, otherwise false
     */
    public static boolean validateAgeInput(String ageText) {
        try {
            short age = Short.parseShort(ageText.trim());
            return isValidAge(age);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
