package at.fhtw.timetracker;

/**
 * The Task class represents a task assigned to an employee.
 */
public class Task {
    private int taskId;
    private String description;
    private int timeSpent;
    private int employeeId; // New field to store the ID of the assigned employee

    private String taskName;
    private Role assignedRole;

    /**
     * Constructs a new Task with the specified parameters.
     *
     * @param taskId       The ID of the task
     * @param description  The description of the task
     * @param timeSpent    The time spent on the task
     * @param assignedRole The role to which the task is assigned
     */

    public Task(int taskId, String description, int timeSpent, Role assignedRole) {
        this.taskId = taskId;
        this.description = description;
        this.timeSpent = timeSpent;
        this.assignedRole = assignedRole;
    }

    // Getter and setter methods
    /**
     * Retrieves the ID of the task.
     *
     * @return The ID of the task
     */
    public int getTaskId() {
        return taskId;
    }
    // Getter and setter for the new field, along with existing getters and setters
    /**
     * Retrieves the ID of the employee assigned to the task.
     *
     * @return The ID of the employee assigned to the task
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the ID of the employee assigned to the task.
     *
     * @param employeeId The ID of the employee assigned to the task
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Sets the ID of the task.
     *
     * @param taskId The ID of the task
     */
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    /**
     * Retrieves the description of the task.
     *
     * @return The description of the task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the task.
     *
     * @param description The description of the task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the time spent on the task.
     *
     * @return The time spent on the task
     */
    public int getTimeSpent() {
        return timeSpent;
    }

    /**
     * Sets the time spent on the task.
     *
     * @param timeSpent The time spent on the task
     */
    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }
    @Override
    public String toString() {
        return taskId + "," + description + "," + timeSpent+","+assignedRole; // No employeeId in the output
    }
}
