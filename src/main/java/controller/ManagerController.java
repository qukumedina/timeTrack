package controller;

import at.fhtw.timetracker.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller class for managing tasks, holiday requests, and user registrations by a manager.
 */
public class ManagerController {

    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private ListView<HolidayRequest> holidayRequestListView;

    @FXML
    private ListView<User> pendingRegistrationsListView;
    @FXML
    private ComboBox<User> employeeComboBox;
    @FXML
    private TextField taskDescriptionField;
    @FXML
    private TextField taskTimeField;

    @FXML
    private TableColumn<Task, Integer> taskIdColumn; // Assuming Task has an Integer ID
    @FXML
    private TableColumn<Task, String> taskDescriptionColumn;
    // Add other columns as necessary
    @FXML
    private TableColumn<Task, Integer> timeSpentColumn; // Added column for time spent

    @FXML
    private Button editTaskButton; // Button for editing a selected task
    @FXML
    private Label holidaysLabel;

    /**
     * Initializes the manager controller.
     * Sets up task table columns, loads tasks into the table, loads pending registrations,
     * and initializes the employee combo box.
     */
    @FXML
    private void initialize() {
        setupTaskTableColumns();
        loadTasksIntoTable();
        loadPendingRegistrations();
        holidaysLabel.setText("Fixed Holidays: \n24th December\n1st January");
        List<User> employees = CsvUtil.readCsv("users.csv", User.class)
                .stream()
                .filter(user -> user.getRole() == Role.EMPLOYEE)
                .collect(Collectors.toList());
        employeeComboBox.setItems(FXCollections.observableArrayList(employees));

    }

    /**
     * Handles the action when the manager assigns a task.
     * Opens a dialog for the manager to enter task details and assigns the task.
     */
    @FXML
    private void handleAssignTask() {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Assign Task");
        dialog.setHeaderText("Enter task details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");
        TextField timeField = new TextField();
        timeField.setPromptText("Time");
        // Add a ChoiceBox to select the role in the UI
        ChoiceBox<Role> roleChoiceBox = new ChoiceBox<>();
        roleChoiceBox.getItems().addAll(Role.EMPLOYEE, Role.MANAGER);
        roleChoiceBox.setValue(Role.EMPLOYEE); // Set the default role to EMPLOYEE
        // Add the ChoiceBox to the grid
        grid.add(new Label("Role:"), 0, 2);
        grid.add(roleChoiceBox, 1, 2);

        grid.add(new Label("Description:"), 0, 0);
        grid.add(descriptionField, 1, 0);
        grid.add(new Label("Time:"), 0, 1);
        grid.add(timeField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(descriptionField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Pair<>(descriptionField.getText(), timeField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(taskDetails -> {
            String description = taskDetails.getKey(); // Get the description entered by the user
            String timeStr = taskDetails.getValue();

            if (description.isEmpty() || timeStr.isEmpty()) {
                showAlert("Error", "Both description and time are required.");
                return;
            }

            // Ensure that timeStr is a valid integer
            if (!isInteger(timeStr)) {
                showAlert("Error", "Time must be a valid number.");
                return;
            }
            int timeInt;

            try {
                timeInt = Integer.parseInt(timeStr); // Convert the time string to an integer
            } catch (NumberFormatException e) {
                showAlert("Error", "Time must be a valid number.");
                return;
            }
            Role role = roleChoiceBox.getValue(); // Get the selected role from the ChoiceBox
            String response = sendTaskToServer(description, timeInt, role); // Use the user-entered description

            System.out.println("Server Response: " + response);

            if ("Task Assigned Successfully".equals(response)) {
                showAlert("Success", "Task assigned successfully.");
                loadTasksIntoTable();
            } else {
                showAlert("Error", "Failed to assign task: " + response);
            }
        });
    }

    /**
     * Checks if a given string is a valid integer.
     * @param s The string to check.
     * @return True if the string is a valid integer, false otherwise.
     */
    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * Shows an alert with the given title and content.
     * @param title The title of the alert.
     * @param content The content of the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Sends a task assignment request to the server.
     * @param description The description of the task.
     * @param time The time required for the task.
     * @param role The role to which the task is assigned (Employee/Manager).
     * @return The server's response to the task assignment request.
     */
    // Update the sendTaskToServer method to include the selected role
    private String sendTaskToServer(String description, int time, Role role) {
        String serverResponse = "";
        try (Socket clientSocket = new Socket("localhost", 6789)) {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String taskDetails =  CsvUtil.generateTaskId() + "," + description + "," + time + "," + role.name();
            System.out.println("Sending to server: " + taskDetails);
            outToServer.writeBytes(taskDetails + '\n');
            serverResponse = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponse;
    }


 /* @FXML
  private void handleAssignTask() {
      Dialog<Pair<String, String>> dialog = new Dialog<>();
      dialog.setTitle("Assign Task");
      dialog.setHeaderText("Enter task details");

      ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

      GridPane grid = new GridPane();
      grid.setHgap(10);
      grid.setVgap(10);
      grid.setPadding(new Insets(20, 150, 10, 10));

      TextField descriptionField = new TextField();
      descriptionField.setPromptText("Description");
      TextField timeField = new TextField();
      timeField.setPromptText("Time");

      // Add the ComboBox for selecting the user
      grid.add(new Label("User:"), 0, 3);
      grid.add(employeeComboBox, 1, 3);

      grid.add(new Label("Description:"), 0, 0);
      grid.add(descriptionField, 1, 0);
      grid.add(new Label("Time:"), 0, 1);
      grid.add(timeField, 1, 1);

      dialog.getDialogPane().setContent(grid);
      Platform.runLater(descriptionField::requestFocus);

      dialog.setResultConverter(dialogButton -> {
          if (dialogButton == saveButtonType) {
              return new Pair<>(descriptionField.getText(), timeField.getText());
          }
          return null;
      });

      Optional<Pair<String, String>> result = dialog.showAndWait();

      result.ifPresent(taskDetails -> {
          String description = taskDetails.getKey();
          String timeStr = taskDetails.getValue();

          if (description.isEmpty() || timeStr.isEmpty()) {
              showAlert("Error", "Both description and time are required.");
              return;
          }

          if (!isInteger(timeStr)) {
              showAlert("Error", "Time must be a valid number.");
              return;
          }
          int timeInt = Integer.parseInt(timeStr);

          User selectedUser = employeeComboBox.getValue();
          if (selectedUser == null) {
              showAlert("Error", "Please select a user.");
              return;
          }

          String response = sendTaskToServer(description, timeInt, selectedUser.getId());
          System.out.println("Server Response: " + response);

          if ("Task Assigned Successfully".equals(response)) {
              showAlert("Success", "Task assigned successfully.");
              loadTasksIntoTable();
          } else {
              showAlert("Error", "Failed to assign task: " + response);
          }
      });
  }*/

   /* private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }*/
/*

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
*/



// Other methods and logic...


    /**
     * Generates a unique task ID.
     * @return The generated task ID.
     */
    private int generateTaskId() {
        List<Task> tasks = CsvUtil.readCsv("tasks.csv", Task.class);
        int maxId = tasks.stream()
                .mapToInt(Task::getTaskId) // Assuming Task has a getTaskId method
                .max()
                .orElse(0); // Start from 0 if there are no tasks

        return maxId + 1; // Return the next available ID
    }

    /**
     * Sets up the columns for the task table.
     */
    private void setupTaskTableColumns() {
        // Assuming the Task class has 'id' and 'description' properties
        taskIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        taskDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        timeSpentColumn.setCellValueFactory(new PropertyValueFactory<>("timeSpent")); // Setup for the new column

        // Initialize other columns similarly
    }

    /**
     * Loads tasks from CSV and populates the task table.
     */
    private void loadTasksIntoTable() {
        List<Task> tasks = CsvUtil.readTaskCsv("tasks.csv", Task.class);
        ObservableList<Task> taskData = FXCollections.observableArrayList(tasks);
        taskTableView.setItems(taskData);
    }


    /**
     * Handles the action when the manager clicks the "Edit Task" button.
     * Opens a dialog to edit the selected task's details.
     */
    @FXML
    private void handleEditTask() {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            openEditTaskDialog(selectedTask);
        }
    }

    /**
     * Opens a dialog to edit the details of a task.
     * @param task The task to edit.
     */
    private void openEditTaskDialog(Task task) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL); // Block interaction with other windows

        VBox dialogVBox = new VBox(10);
        TextField descriptionField = new TextField(task.getDescription());
        TextField timeSpentField = new TextField(String.valueOf(task.getTimeSpent()));
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(e -> {
            // Update task details
            task.setDescription(descriptionField.getText());
            task.setTimeSpent(Integer.parseInt(timeSpentField.getText()));
            updateTaskInCsv(task);
            dialogStage.close();
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        dialogVBox.getChildren().addAll(descriptionField, timeSpentField, saveButton, cancelButton);
        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }
    /**
     * Updates a task in the CSV file.
     * @param task The task to update.
     */
    private void updateTaskInCsv(Task task) {
        List<Task> tasks = CsvUtil.readTaskCsv("tasks.csv", Task.class);
        tasks.removeIf(t -> t.getTaskId() == task.getTaskId());
        tasks.add(task);
        CsvUtil.writeTaskCsv(tasks, "tasks.csv");
        loadTasksIntoTable(); // Refresh the table view
    }
    @FXML
    private void handleApproveRegistrations() {
        try {
            List<User> users = CsvUtil.readCsv("users.csv", User.class);
            if (users == null || users.isEmpty()) {
                System.out.println("No users found or error reading the CSV file.");
                return;
            }

            List<User> pendingUsers = users.stream()
                    .filter(user -> user.getStatus() == Status.PENDING)
                    .collect(Collectors.toList());

            if (pendingUsers.isEmpty()) {
                System.out.println("No pending registrations found.");
                return;
            }

            pendingRegistrationsListView.setItems(FXCollections.observableArrayList(pendingUsers));
            pendingRegistrationsListView.refresh(); // Refresh the ListView
            System.out.println("Pending registrations loaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

/*    @FXML
    private void approveSelectedRegistration() {
        User selectedUser = pendingRegistrationsListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            selectedUser.setStatus(Status.APPROVED);
            updateUsersInCsv();
        }
    }*/
    /**
     * Handles the action when the manager approves a selected registration.
     * Sends an approval request to the server and updates the status locally if successful.
     */
@FXML
private void approveSelectedRegistration() {
    User selectedUser = pendingRegistrationsListView.getSelectionModel().getSelectedItem();
    if (selectedUser != null) {
        String response = sendApprovalToServer(selectedUser.getUsername(), true);
        System.out.println("Server Response: " + response);

        if (response.equals("Registration Approved Successfully")) {
            // Update status in local list
            selectedUser.setStatus(Status.APPROVED);

            // Refresh the pending registrations list by re-reading the list and filtering for PENDING status
            List<User> updatedUsers = CsvUtil.readCsv("users.csv", User.class)
                    .stream()
                    .filter(user -> user.getStatus() == Status.PENDING)
                    .collect(Collectors.toList());

            // Update the ListView with the new list
            pendingRegistrationsListView.setItems(FXCollections.observableArrayList(updatedUsers));

            // Refresh the ListView to show the changes
            pendingRegistrationsListView.refresh();
        } else {
            // Handle error or unsuccessful approval
            System.out.println("Failed to approve registration: " + response);
        }
    }
}



    /**
     * Sends an approval request to the server for a registration.
     * @param username The username of the user to approve.
     * @param isApproval True if the request is an approval, false if rejection.
     * @return The server's response to the approval request.
     */
    private String sendApprovalToServer(String username, boolean isApproval) {
        String serverResponse = "";
        try (Socket clientSocket = new Socket("localhost", 6789)) {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String approvalStatus = isApproval ? "Approve" : "Reject";
            String requestMessage = "ApproveRegistration:" + username;
            outToServer.writeBytes(requestMessage + '\n');

            serverResponse = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponse;
    }





    @FXML
    private void rejectSelectedRegistration() {
        User selectedUser = pendingRegistrationsListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            selectedUser.setStatus(Status.REJECTED);

            List<User> users = CsvUtil.readCsv("users.csv", User.class);
            users = users.stream()
                    .map(user -> user.getUsername().equals(selectedUser.getUsername()) ? selectedUser : user)
                    .collect(Collectors.toList());

            CsvUtil.writeCsv(users, "users.csv");
            loadPendingRegistrations();
        }
    }

    private void loadPendingRegistrations() {
        List<User> users = CsvUtil.readCsv("users.csv", User.class);
        List<User> pendingUsers = users.stream()
                .filter(user -> user.getStatus() == Status.PENDING)
                .collect(Collectors.toList());
        pendingRegistrationsListView.setItems(FXCollections.observableArrayList(pendingUsers));
    }
    private void updateUsersInCsv() {
        List<User> users = CsvUtil.readCsv("users.csv", User.class);
        CsvUtil.writeCsv(users, "users.csv");
        handleApproveRegistrations(); // Refresh the list
    }
    /**
     * Loads holiday requests from the server and populates the list view.
     */
    @FXML
    private void loadHolidayRequests() {
        // Send a request to the server to get the holiday requests
        String response = sendRequestToServer("GetHolidayRequests");
        System.out.println("Server Response for Holiday Requests: " + response);

        // Assuming the server sends a CSV string as a response, parse it into HolidayRequest objects
        List<HolidayRequest> requests = CsvUtil.readCsv(response, HolidayRequest.class);
        holidayRequestListView.setItems(FXCollections.observableArrayList(requests));
    }
    /**
     * Approves the selected holiday request.
     */
    @FXML
    private void approveSelectedRequest() {
        HolidayRequest selectedRequest = holidayRequestListView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            // Convert the request ID to String
            String requestIdString = String.valueOf(selectedRequest.getRequestId());
            String response = sendApprovalToServer(requestIdString, true); // true for approval
            System.out.println("Server Response for Approval: " + response);

            selectedRequest.setStatus(Status.APPROVED);
            updateHolidayRequestInCsv(selectedRequest);
        }
    }


    private String sendRequestToServer(String requestType) {
        String serverResponse = "";
        try (Socket clientSocket = new Socket("localhost", 6789)) {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String requestMessage = requestType; // No additional details for loading holiday requests
            System.out.println("Sending request to server: " + requestMessage);
            outToServer.writeBytes(requestMessage + '\n');

            serverResponse = inFromServer.readLine();
            System.out.println("Received from server: " + serverResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponse;
    }



    private void updateHolidayRequestInCsv(HolidayRequest updatedRequest) {
        List<HolidayRequest> requests = CsvUtil.readCsv("holidayRequests.csv", HolidayRequest.class);
        requests.removeIf(request -> request.getRequestId() == updatedRequest.getRequestId());
        requests.add(updatedRequest);
        CsvUtil.writeCsv(requests, "holidayRequests.csv");
        loadHolidayRequests(); // Refresh the list view
    }

    /**
     * Sends a request to the server to proceed to the task tracking screen.
     */
    @FXML
    private void goToTaskTracking() {
        String response = sendRequestTTasktrackingoServer("LoadTaskTrackingScreen");
        System.out.println("Server Response for Task Tracking Screen: " + response);

        // Assuming the server's response determines whether or not to proceed
        if (response.equals("Proceed to Task Tracking")) {
            try {
                TCPClient.getInstance().loadTaskTrackingScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Unable to load Task Tracking Screen");
            // Handle accordingly
        }
    }

    private String sendRequestTTasktrackingoServer(String requestType) {
        String serverResponse = "";
        try (Socket clientSocket = new Socket("localhost", 6789)) {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            outToServer.writeBytes(requestType + '\n');
            serverResponse = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    /**
     * Handles the action when the manager approves holiday requests.
     * This method is not yet implemented.
     */
    @FXML
    private void handleApproveHolidayRequests() {
        // TODO
        System.out.println("Approving Holiday Requests");
    }
}
