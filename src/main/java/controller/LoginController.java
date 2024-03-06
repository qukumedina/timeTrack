package controller;

import com.example.time.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Controller class for handling user login functionality.
 *  * Allows users to log in, check user roles, and navigate to different screens based on roles.
 */
public class LoginController {

    private User loggedInUser;
    private Queue<User> userList = new ConcurrentLinkedQueue<>();

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private static final String USERS_CSV_FILE = "users.csv";
    @FXML
    private Button managerScreenButton;
    private User currentUser; // Assume this is set to the currently logged-in user

    @FXML
    /**
     * Handles the action when the user clicks the login button.
     * Sends login request to the server, processes the response, and loads the appropriate screen.
     */
    private void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Debug: Print username and password for verification
        System.out.println("Attempting to log in with Username: " + username + ", Password: " + password);

        String response = sendLoginRequestToServer(username, password);
        System.out.println("Response from server: " + response); // Debug

        if (response != null && !response.isEmpty()) {
            String[] responseParts = response.split(":");

            if ("Login Successful".equals(responseParts[0])) {
                Role userRole = getUserRoleFromResponse(responseParts[1]);
                System.out.println("Login successful, user role: " + userRole); // Debug

                for (User user : userList) {
                    if (user.getUsername().equals(username)){
                        loggedInUser = user;
                        break;
                    }
                }

                if (userRole == Role.MANAGER) {
                    loadManagerScreen();
                } else {
                    loadEmployeeScreen();
                }
            } else {
                System.out.println("Invalid login credentials");
            }
        } else {
            System.out.println("No response received from server or response is empty.");
        }
    }

    /**
     * Converts the user role string from the server response to a Role enum.
     * @param roleStr The user role string from the server response.
     * @return The corresponding Role enum.
     */
    private Role getUserRoleFromResponse(String roleStr) {
        return "Manager".equalsIgnoreCase(roleStr) ? Role.MANAGER : Role.EMPLOYEE;
    }

    /**
     * Sends a login request to the server with the provided username and password.
     * Receives and returns the server's response.
     * @param username The username for login.
     * @param password The password for login.
     * @return The server's response to the login request.
     */
    private String sendLoginRequestToServer(String username, String password) {
        String serverResponse = "";
        try (Socket clientSocket = new Socket("localhost", 6789)) {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String request = "Login:" + username + "," + password;
            System.out.println("Sending request to server: " + request); // Debug

            outToServer.writeBytes(request + '\n');

            // Receiving response from the server
            serverResponse = inFromServer.readLine();
            System.out.println("FROM SERVER: " + serverResponse); // Debug
        } catch (Exception e) {
            System.out.println("Error during server communication: " + e.getMessage()); // Debug
            e.printStackTrace();
        }
        return serverResponse;
    }



    /**
     * Loads the employee screen after successful login.
     */
    private void loadEmployeeScreen() {
        try {
            TCPClient.getInstance().loadEmployeeScreen(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the manager screen after successful login.
     */
    private void loadManagerScreen() {
        try {
            TCPClient.getInstance().loadManagerScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadTaskTrackingScreen() {
        try {
            TCPClient.getInstance().loadTaskTrackingScreen(loggedInUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Call this method after the user logs in
    /**
     * Checks the user role and displays the manager screen button if the user is a manager.
     */
    public void checkUserRole() {
        if (currentUser != null && currentUser.getRole() == Role.MANAGER) {
            managerScreenButton.setVisible(true);
        }
    }

    /**
     * Checks if the provided login credentials are valid.
     * @param username The username for login.
     * @param password The password for login.
     * @return True if the credentials are valid, false otherwise.
     */
    private boolean isValidLogin(String username, String password) {
        List<User> userList = CsvUtil.readCsv(USERS_CSV_FILE, User.class);
        return userList.stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
    }

    /**
     * Retrieves the user object based on the provided username.
     * @param username The username of the user to retrieve.
     * @return The User object corresponding to the username.
     */
    private User getUserByUsername(String username) {
        List<User> userList = CsvUtil.readCsv(USERS_CSV_FILE, User.class);
        return userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Handles the action when the user clicks the button to navigate to the registration screen.
     * Loads the registration screen.
     */
    @FXML
    private void goToRegister() {
        try {
            TCPClient.getInstance().loadRegistrationScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action when the user clicks the button to navigate to the manager screen.
     * Loads the manager screen.
     */
    @FXML
    private void goToManagerScreen() {
        try {
            TCPClient.getInstance().loadManagerScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the data for the controller.
     * Sets the list of users.
     * @param users The list of users.
     */
    public void initData(Queue<User> users) {
        this.userList = users;
    }

}
