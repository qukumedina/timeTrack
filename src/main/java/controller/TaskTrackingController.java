// TaskTrackingController.java
package controller;

import com.example.time.CsvUtil;
import com.example.time.Role;
import com.example.time.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;

public class TaskTrackingController {

    @FXML
    private TextField taskNameField;
    @FXML
    private TextField taskIDField;

    @FXML
    private TextField timeSpentField;


    @FXML
    private void trackTask() {
        // Add logic to handle task tracking
        //krejt palidhje s ka lidhje me server
        //me e make sence si te useri
        String taskName = taskNameField.getText();
        String timeSpentInput = timeSpentField.getText();
        int taskId = Integer.parseInt(taskIDField.getText());
        String description = taskName; // Use a default description or add a new UI field
        String assignedRoleInput = "EMPLOYEE"; // Initialize with a default role (e.g., "EMPLOYEE")

        // Extract the numeric part from the timeSpent input
        int timeSpent = extractNumericValue(timeSpentInput);

        // Perform task tracking logic here
        // For simplicity, let's just print the tracked task details
        System.out.println("Task Tracked - Task Name: " + taskName + ", Time Spent: " + timeSpent + " hours");

        // You can store the tracked task in a CSV file or perform other actions as needed
        saveTaskToCsv(taskId, description, timeSpent, assignedRoleInput);
    }

    private int extractNumericValue(String input) {
        // Extract numeric part from the input (e.g., "2 hours" -> 2)
        int numericValue = 0;
        try {
            String[] parts = input.split("\\s+");
            for (String part : parts) {
                if (part.matches("\\d+")) {
                    numericValue = Integer.parseInt(part);
                    break;
                }
            }
        } catch (NumberFormatException e) {
            // Handle invalid input gracefully (e.g., provide a default value)
        }
        return numericValue;
    }
    int assignedRole = 1; // Use 1 to represent EMPLOYEE


    private void saveTaskToCsv(int taskId, String description, int timeSpent, String assignedRole) {
        Role role;
        if ("EMPLOYEE".equals(assignedRole)) {
            role = Role.EMPLOYEE;
        } else if ("MANAGER".equals(assignedRole)) {
            role = Role.MANAGER;
        } else {
            // Handle the case where the role input is not recognized
            throw new IllegalArgumentException("Invalid role: " + assignedRole);
        }

        Task task = new Task(taskId, description, timeSpent, role);

        // Read existing tasks from CSV file
        List<Task> taskList = CsvUtil.readTaskCsv("tasks.csv", Task.class);

        // Add the new task to the list
        taskList.add(task);

        // Write the updated task list back to the CSV file
        CsvUtil.writeCsv(taskList, "tasks.csv");
    }

}
