package com.example.time;

/**
 * The UnauthorizedAccessException class represents an exception thrown when a user
 *  * tries to access a resource without proper authorization.
 */
public class UnauthorizedAccessException extends Exception {
    /**
     * Constructs a new UnauthorizedAccessException with the specified detail message.
     *
     * @param message the detail message
     */
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
