package controller;

import com.example.time.TCPClient;
import com.example.time.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Controller class for tracking working hours.
 *  * Allows users to track their working hours for a specific date
 */
public class WorkingHoursController {

    @FXML
    private DatePicker dateTrackField;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;

    /**
     * Handles the action when the user clicks the "Track Time" button.
     * Calculates the hours worked based on the start and end times, then sends the data to the server.
     */
    @FXML
    private void trackTime() {
        LocalDate date = dateTrackField.getValue();
        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText();
        String hoursDone = calculateHours (startTime, endTime);

        if (date == null || startTime.isEmpty() || endTime.isEmpty()) {
            // Handle validation or display an error message
            System.out.println("Please fill in all fields");
        } else {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formattedString = date.format(formatter);

            String response = sendWorkingHoursToServer(formattedString, startTime, endTime, hoursDone);
        }
    }

    /**
     * Calculates the difference between two time strings in hours and minutes format.
     *
     * @param startTime The start time in "HH:mm" format.
     * @param endTime   The end time in "HH:mm" format.
     * @return The calculated hours and minutes as a formatted string "HH:mm".
     */
    public String calculateHours (String startTime, String endTime){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date start = null;
        Date end = null;
        try {
            start = sdf.parse(startTime);
            end = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long differenceInMillis = end.getTime() - start.getTime();

        // Calculate hours and minutes
        long hours = differenceInMillis / (60 * 60 * 1000);
        long minutes = (differenceInMillis / (60 * 1000)) % 60;

        String hoursCalc = String.format("%02d:%02d", hours, minutes);
        System.out.println("Hours Done: " + hoursCalc);
        return hoursCalc;
    }

    /**
     * Sends the working hours data to the server.
     *
     * @param date      The date in "dd.MM.yyyy" format.
     * @param startTime The start time in "HH:mm" format.
     * @param endTime   The end time in "HH:mm" format.
     * @param hoursDone The calculated hours and minutes worked as a formatted string "HH:mm".
     * @return The server response to the working hours tracking request.
     */
    private String sendWorkingHoursToServer(String date, String startTime, String endTime, String hoursDone){
        String serverResponseTrack = "";
        try (Socket clientSocket = new Socket("localhost", 6789)) {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String request = "Track Time:" + date + "," + startTime + "," + endTime + "," + hoursDone + '\n';
            System.out.println("Sending to server: " + request);

            outToServer.writeBytes(request);
            serverResponseTrack = inFromServer.readLine();

            // Print server response for debugging
            System.out.println("FROM SERVER Track Time: " + serverResponseTrack);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error Track: " + e.getMessage());
        }
        return serverResponseTrack;
    }


    /**
     * Initializes data for the controller.
     * Currently not used, but can be implemented to pass the logged-in user's information.
     *
     * @param loggedInUser The logged-in user object.
     */
    public void initData(User loggedInUser) {
        //this.loggedInUser = loggedInUser;
        //title.setText("Task Tracking for user: " + loggedInUser.getUsername());
    }
}
