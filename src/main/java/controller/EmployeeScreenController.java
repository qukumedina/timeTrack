// LoginController.java
package controller;

import at.fhtw.timetracker.TCPClient;
import at.fhtw.timetracker.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * Controller class for the employee screen.
 *  * Handles actions related to task tracking and time tracking.
 */
public class EmployeeScreenController {

    @FXML
    private Button trackTaskButton;
    @FXML
    private Button trackTimeButton;
    private User loggedInUser;


    /**
     * Navigates to the time tracking screen.
     */
    @FXML
    private void goTimeTrack() {
        try {
            TCPClient.getInstance().loadWorkingHoursScreen(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Navigates to the task tracking screen.
     */
    @FXML
    private void goTaskTrack() {
        try {
            TCPClient.getInstance().loadTaskTrackingScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action when the user clicks the button to navigate to the registration screen.
     * Loads the registration screen.
     */
    @FXML
    private void goBack() {
        try {
            TCPClient.getInstance().loadLoginScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
