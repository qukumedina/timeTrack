package controller;


import at.fhtw.timetracker.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;

/**
 * Controller class for user registration functionality.
 *  * Allows users to register with a username, password, and role.
 */
public class UserRegistrationController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private ChoiceBox<Role> roleChoiceBox;

    /**
     * Handles the action when the user clicks the "Register" button.
     * Validates the input fields and sends a registration request to the server.
     */
    @FXML
    private void registerUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Role role = roleChoiceBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            // Handle validation or display an error message
            System.out.println("Please fill in all fields");
            return;
        }

        String response = sendRegistrationRequestToServer(username, password, role);

        if (response != null && response.equals("Registration Successful")) {
            // Save the user to CSV file
            System.out.println("User Registered - Username: " + username + ", Password: " + password + ", Role: " + role);
            // Optionally, switch to the login screen or provide other feedback
        } else {
            System.out.println("Registration failed: " + response);
        }
    }

    /**
     * Retrieves the selected role from the ChoiceBox.
     *
     * @return The selected role.
     */
    private Role getSelectedRole() {
        return roleChoiceBox.getValue();
    }

    /**
     * Sends a registration request to the server with the provided user details.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @param role     The role of the new user.
     * @return The server response to the registration request.
     */
    private String sendRegistrationRequestToServer(String username, String password, Role role) {
        String serverResponse = "";
        try (Socket clientSocket = new Socket("localhost", 6789)) {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String request = "Register:" + username + "," + password + "," + role + '\n';
            System.out.println("Sending to server: " + request);

            outToServer.writeBytes(request);
            serverResponse = inFromServer.readLine();

            // Print server response for debugging
            System.out.println("FROM SERVER: " + serverResponse);


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        return serverResponse;
    }

    /**
     * Switches the scene to the login screen.
     *
     * @param event The ActionEvent triggered by clicking the "Switch to Login" button.
     */
    @FXML
    private void switchToLoginScreen(ActionEvent event) {
        try {
            // Load the login FXML file
            Parent loginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/login.fxml")));
            // Update the path to your FXML file
            Scene loginScene = new Scene(loginRoot);

            // Get the stage from the event source and set the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
