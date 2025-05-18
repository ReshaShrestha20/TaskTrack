package com.tasktrack.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("MM-dd-yyyy")
    };

    /**
     * Parses a date string using multiple formats and validates it.
     * @param dateString The date string to parse
     * @return The parsed LocalDate
     * @throws Exception If the date is in the future or invalid format
     */
    public static LocalDate parseDate(String dateString) throws Exception {
        if (dateString == null || dateString.trim().isEmpty()) {
            throw new Exception("Date cannot be empty");
        }

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(dateString, formatter);
                
                // Check if date is in the future
                if (date.isAfter(LocalDate.now())) {
                    throw new Exception("Date of birth cannot be in the future");
                }
                
                return date;
            } catch (DateTimeParseException e) {
                continue;
            }
        }
        
        throw new Exception("Invalid date format. Please use YYYY-MM-DD, MM/DD/YYYY, DD/MM/YYYY, or MM-DD-YYYY format");
    }

    /**
     * Formats a LocalDate to the default format (yyyy-MM-dd)
     * @param date The LocalDate to format
     * @return The formatted date string
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DATE_FORMATTERS[0]);
    }
}