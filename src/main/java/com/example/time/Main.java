package com.example.time;// Main.java
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Main instance;

    private Stage primaryStage;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        instance = this;

        // Load the Login scene initially
        loadLoginScreen();

        primaryStage.setTitle("Time Tracker");
        primaryStage.show();
    }

    // Method to load the Login screen
    public void loadLoginScreen() throws IOException {
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/login.fxml"));
        primaryStage.setScene(new Scene(loginRoot));
    }

    // Method to load the Task Tracking screen
    public void loadTaskTrackingScreen() throws IOException {
        Parent taskTrackingRoot = FXMLLoader.load(getClass().getResource("/TaskTracking.fxml"));
        primaryStage.setScene(new Scene(taskTrackingRoot));
    }

    // Method to load the Registration screen
    public void loadRegistrationScreen() throws IOException {
        Parent registrationRoot = FXMLLoader.load(getClass().getResource("/registration.fxml"));
        primaryStage.setScene(new Scene(registrationRoot));
    }
    // Part of Main.java
    public void loadManagerScreen() throws IOException {
        Parent managerRoot = FXMLLoader.load(getClass().getResource("/ManagerScreen.fxml"));
        primaryStage.setScene(new Scene(managerRoot));
    }



    public static void main(String[] args) {
        launch(args);
    }
}
