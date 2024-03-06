package controller;
import at.fhtw.timetracker.TCPClient;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The TaskTrackingController class is responsible for handling task tracking functionality.
 *  * It allows users to input task details such as task name, ID, and time spent,
 *  * and sends this information to the server for task tracking.
 */
public class TaskTrackingController {

    @FXML
    private TextField taskNameField;
    @FXML
    private TextField taskIDField;
    @FXML
    private TextField timeSpentField;

    /**
     * Handles the action when the user tracks a task.
     * It retrieves the input from the text fields, validates the input,
     * and sends a task tracking request to the server.
     * If successful, it saves the task to a CSV file.
     */
    @FXML
    private void trackTask() {
        // Add logic to handle task tracking
        String description = taskNameField.getText();
        String timeSpentInput = timeSpentField.getText();
        int taskId;
        try {
            taskId = Integer.parseInt(taskIDField.getText());
        } catch (NumberFormatException ex) {
            System.out.println("Please fill in all fields");
            return;
        }

        if (description.isEmpty() || timeSpentInput.isEmpty()) {
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
            e.printStackTrace();
        }
        return numericValue;
    }

    /**
     * Saves the tracked task to a CSV file.
     * It converts the assigned role from a string to the Role enum,
     * creates a new Task object, reads existing tasks from the CSV file,
     * adds the new task, and writes the updated task list back to the CSV file.
     *
     * @param taskId       The ID of the task.
     * @param description  The description of the task.
     * @param timeSpent    The time spent on the task.
     */

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

    @FXML
    private void goBack() {
        try {
            TCPClient.getInstance().loadEmployeeScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
