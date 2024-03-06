// TaskTrackingController.java
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

    public void initData(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        if (loggedInUser != null)
            title.setText("Task Tracking for user: " + loggedInUser.getUsername());
    }
}
