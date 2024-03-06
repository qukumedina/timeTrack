/*
// UserRegistrationController.java
package controller;

import com.example.time.CsvUtil;
import com.example.time.Role;
import com.example.time.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

public class UserRegistrationController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

@FXML
private void registerUser() {
    String username = usernameField.getText();
    String password = passwordField.getText();

    String response = sendRegistrationRequestToServer(username, password);

    if (response.equals("Registration Successful")) {
        System.out.println("User Registered - Username: " + username + ", Password: " + password);
        // Optionally, switch to the login screen or provide other feedback
    } else {
        System.out.println("Registration failed: " + response);
    }
}

    private String sendRegistrationRequestToServer(String username, String password) {
        String serverResponse = "";
        try (Socket clientSocket = new Socket("localhost", 6789)) {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            outToServer.writeBytes("Register:" + username + "," + password + '\n');
            serverResponse = inFromServer.readLine();

            // Print server response for debugging
            System.out.println("FROM SERVER: " + serverResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    @FXML
    private void switchToLoginScreen(ActionEvent event) {
        try {
            // Load the login FXML file
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/login.fxml")); // Update the path to your FXML file
            Scene loginScene = new Scene(loginRoot);

            // Get the stage from the event source and set the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
        }
    }
}
*/

package controller;


import com.example.time.CsvUtil;
import com.example.time.Role;
import com.example.time.Task;
import com.example.time.User;
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
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class UserRegistrationController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private ChoiceBox<Role> roleChoiceBox;


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

    private Role getSelectedRole() {
        return roleChoiceBox.getValue();
    }

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
// UserRegistrationController.java
/*
package controller;

import com.example.time.CsvUtil;
import com.example.time.Role;
import com.example.time.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;

public class UserRegistrationController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private void registerUser() {
        // Add logic to handle user registration
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Perform registration logic here
        // For simplicity, let's create a new User object and store it in a CSV file
        User newUser = new User( username, password, Role.EMPLOYEE);

        // Read existing users from CSV file
        List<User> userList = CsvUtil.readCsv("users.csv", User.class);

        // Add the new user to the list
        userList.add(newUser);

        // Write the updated user list back to the CSV file
        CsvUtil.writeCsv(userList, "users.csv");

        System.out.println("User Registered - Username: " + username + ", Password: " + password);
    }
}*/
