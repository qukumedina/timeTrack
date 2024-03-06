package com.example.time;


/**
 * Exception thrown when a holiday request is invalid.
 */
public class InvalidHolidayRequestException extends Exception {
    /**
     * Constructs a new InvalidHolidayRequestException with the specified detail message.
     *
     * @param message The detail message
     */
    public InvalidHolidayRequestException(String message) {
        super(message);
    }
}