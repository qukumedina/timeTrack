// LoginController.java
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


/*    private void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        String response = sendLoginRequestToServer(username, password);
        String[] responseParts = response.split(":");

        if (responseParts[0].equals("Login Successful")) {
            if (responseParts[1].equals("Manager")) {
                loadManagerScreen();
            } else {
                loadTaskTrackingScreen();
            }
        } else {
            System.out.println("Invalid login credentials");
        }
    }*/
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

    private Role getUserRoleFromResponse(String roleStr) {
        return "Manager".equalsIgnoreCase(roleStr) ? Role.MANAGER : Role.EMPLOYEE;
    }

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



    private void loadEmployeeScreen() {
        try {
            TCPClient.getInstance().loadEmployeeScreen(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    public void checkUserRole() {
        if (currentUser != null && currentUser.getRole() == Role.MANAGER) {
            managerScreenButton.setVisible(true);
        }
    }


    private boolean isValidLogin(String username, String password) {
        List<User> userList = CsvUtil.readCsv(USERS_CSV_FILE, User.class);
        return userList.stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
    }

    private User getUserByUsername(String username) {
        List<User> userList = CsvUtil.readCsv(USERS_CSV_FILE, User.class);
        return userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @FXML
    private void goToRegister() {
        try {
            TCPClient.getInstance().loadRegistrationScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToManagerScreen() {
        try {
            TCPClient.getInstance().loadManagerScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initData(Queue<User> users) {
        this.userList = users;
    }

}
