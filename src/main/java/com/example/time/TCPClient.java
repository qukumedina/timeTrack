package com.example.time;

import controller.LoginController;
import controller.TaskTrackingController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * The TCPClient class represents a JavaFX application that acts as a TCP client.
 */
public class TCPClient extends Application {
    private static TCPClient instance;

    private Stage primaryStage;

    /**
     * Gets the singleton instance of TCPClient.
     *
     * @return The TCPClient instance
     */
    public static TCPClient getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        instance = this;
        primaryStage.setTitle("Time Tracker");

        // Load the Login scene initially
        loadLoginScreen();

        primaryStage.show();
    }

    // Method to load the Login screen
    /**
     * Method to load the Login screen.
     *
     * @throws IOException If an I/O error occurs
     */
    public void loadLoginScreen() throws IOException {
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/login.fxml"));
        primaryStage.setScene(new Scene(loginRoot));
    }

    /**
     * Method to load the Employee screen.
     *
     * @param loggedInUser The logged-in user
     * @throws IOException If an I/O error occurs
     */
    public void loadEmployeeScreen(User loggedInUser) throws IOException {

        Parent employeeScreen = FXMLLoader.load(getClass().getResource("/EmployeeScreen.fxml"));
        primaryStage.setScene(new Scene(employeeScreen));
    }

    // Method to load the Task Tracking screen
    /**
     * Method to load the Task Tracking screen.
     *
     * @param loggedInUser The logged-in user
     * @throws IOException If an I/O error occurs
     */
    public void loadTaskTrackingScreen(User loggedInUser) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/TaskTracking.fxml"
                )
        );

        Parent taskTrackingRoot = loader.load(getClass().getResource("/TaskTracking.fxml"));
        TaskTrackingController controller = loader.getController();

        if (controller != null)
            controller.initData(loggedInUser);

        primaryStage.setScene(new Scene(taskTrackingRoot));
    }


    /**
     * Method to load the Working Hours screen.
     *
     * @param loggedInUser The logged-in user
     * @throws IOException If an I/O error occurs
     */
    public void loadWorkingHoursScreen(User loggedInUser) throws IOException {
        Parent workingHrs = FXMLLoader.load(getClass().getResource("/WorkingHours.fxml"));
        primaryStage.setScene(new Scene(workingHrs));
    }


    // Method to load the Registration screen
    /**
     * Method to load the Registration screen.
     *
     * @throws IOException If an I/O error occurs
     */
    public void loadRegistrationScreen() throws IOException {
        Parent registrationRoot = FXMLLoader.load(getClass().getResource("/registration.fxml"));
        primaryStage.setScene(new Scene(registrationRoot));
    }


    /**
     * Method to load the Manager screen.
     *
     * @throws IOException If an I/O error occurs
     */
    public void loadManagerScreen() throws IOException {
        Parent managerRoot = FXMLLoader.load(getClass().getResource("/ManagerScreen.fxml"));
        primaryStage.setScene(new Scene(managerRoot));
    }

    /**
     * Main method to launch the JavaFX application and include the TCP client logic.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        launch(args);

        // Here you can include the TCP client logic after the JavaFX application is launched
        String serverName = "localhost"; // Server name or IP
        int port = 6789; // Port number

        try (Socket clientSocket = new Socket(serverName, port)) {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            // Ask for username and password
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Sending login details to the server
            outToServer.writeBytes("Login:" + username + "," + password + '\n');

            // Receiving response from the server
            String response = inFromServer.readLine();
            System.out.println("FROM SERVER: " + response);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
