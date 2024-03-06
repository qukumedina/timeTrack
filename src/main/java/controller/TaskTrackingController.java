package controller;

import com.example.time.CsvUtil;
import com.example.time.Role;
import com.example.time.Task;
import com.example.time.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

/**
 * The TaskTrackingController class is responsible for handling task tracking functionality.
 *  * It allows users to input task details such as task name, ID, and time spent,
 *  * and sends this information to the server for task tracking.
 */
public class TaskTrackingController {

    private User loggedInUser;

    @FXML
    private TextField taskNameField;
    @FXML
    private TextField taskIDField;
    @FXML
    private TextField timeSpentField;
    @FXML
    private Text title;

    /**
     * Handles the action when the user tracks a task.
     * It retrieves the input from the text fields, validates the input,
     * and sends a task tracking request to the server.
     * If successful, it saves the task to a CSV file.
     */
    @FXML
    private void trackTask() {
        // Add logic to handle task tracking
        String taskName = taskNameField.getText();
        String timeSpentInput = timeSpentField.getText();
        int taskId = Integer.parseInt(taskIDField.getText());
        String description = taskName; // Use a default description or add a new UI field
        if (!taskName.isEmpty() && !timeSpentInput.isEmpty() && taskId == 0) {
            // Handle validation or display an error message
            System.out.println("Please fill in all fields");
            return;
        }

        // Extract the numeric part from the timeSpent input
        int timeSpent = extractNumericValue(timeSpentInput);
        String response = sendRegistrationReqToServerTask(taskId, description, timeSpent);
        if (response != null && response.startsWith("Task Assigned Successfully")) {
            System.out.println("Task Tracked - Task name: " +description + ", Time Spent: " + timeSpentInput);
        } else {
            System.out.println("Time Tracked failed: " + response);
        }
    }

    /**
     * Utility method to extract the numeric value from a string.
     * Used to parse the time spent on the task.
     *
     * @param input The input string containing the numeric value.
     * @return The extracted numeric value as an integer.
     */
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

    /**
     * Saves the tracked task to a CSV file.
     * It converts the assigned role from a string to the Role enum,
     * creates a new Task object, reads existing tasks from the CSV file,
     * adds the new task, and writes the updated task list back to the CSV file.
     *
     * @param taskId       The ID of the task.
     * @param description  The description of the task.
     * @param timeSpent    The time spent on the task.
     * @param assignedRole The role assigned to the user for this task.
     */
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
        CsvUtil.writeTaskCsv(taskList, "tasks.csv");
    }
    private String sendRegistrationReqToServerTask(int taskId, String description, int timeSpent){
        String serverResponseTrack = "";
        try (Socket clientSocket = new Socket("localhost", 6789)) {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String request = "AssignTask:" + taskId + "," + description + "," + timeSpent + '\n';
            System.out.println("Sending to server: " + request);

            outToServer.writeBytes(request);
            serverResponseTrack = inFromServer.readLine();

            // Print server response for debugging
            System.out.println("FROM SERVER Track: " + serverResponseTrack);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error Track: " + e.getMessage());
        }
        return serverResponseTrack;
    }

    /**
     * Initializes the controller with the logged-in user's information.
     *
     * @param loggedInUser The logged-in user for whom the task tracking is being done.
     */
    public void initData(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        if (loggedInUser != null)
            title.setText("Task Tracking for user: " + loggedInUser.getUsername());
    }
}
