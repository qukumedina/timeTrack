package com.example.time;

/**
 * Exception thrown when a user is invalid.
 */
public class InvalidUserException extends Exception {
    /**
     * Constructs a new InvalidUserException with the specified detail message.
     *
     * @param message The detail message
     */
    public InvalidUserException(String message) {
        super(message);
    }
}