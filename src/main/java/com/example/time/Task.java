package com.example.time;

// Task.java
public class Task {
    private int taskId;
    private String description;
    private int timeSpent;
    private int employeeId; // New field to store the ID of the assigned employee

    private String taskName;
    private Role assignedRole;

    public Task(int taskId, String description, int timeSpent, Role assignedRole) {
        this.taskId = taskId;
        this.description = description;
        this.timeSpent = timeSpent;
        this.assignedRole = assignedRole;
    }

    // Getter and setter methods
    public int getTaskId() {
        return taskId;
    }
    // Getter and setter for the new field, along with existing getters and setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }
    @Override
    public String toString() {
        return taskId + "," + description + "," + timeSpent+","+assignedRole; // No employeeId in the output
    }
}
