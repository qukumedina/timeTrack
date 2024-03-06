package com.example.time;


/**
 * Exception thrown when a task is not found.
 */
public class TaskNotFoundException extends Exception {
    /**
     * Constructs a new TaskNotFoundException with the specified detail message.
     *
     * @param message The detail message
     */
    public TaskNotFoundException(String message) {
        super(message);
    }
}