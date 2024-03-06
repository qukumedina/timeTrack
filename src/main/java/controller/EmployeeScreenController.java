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

public class EmployeeScreenController {

    @FXML
    private Button trackTaskButton;

    @FXML
    private Button trackTimeButton;
    private User loggedInUser;


    @FXML
    private void goTimeTrack() {
        try {
            TCPClient.getInstance().loadWorkingHoursScreen(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goTaskTrack() {
        try {
            TCPClient.getInstance().loadTaskTrackingScreen(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initData(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
