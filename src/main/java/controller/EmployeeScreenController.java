// LoginController.java
package controller;

import com.example.time.CsvUtil;
import com.example.time.Role;
import com.example.time.TCPClient;
import com.example.time.User;
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
            TCPClient.getInstance().loadTaskTrackingScreen(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the controller with the logged-in user.
     * @param loggedInUser The logged-in user.
     */
    public void initData(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
