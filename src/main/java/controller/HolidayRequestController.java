package controller;


import at.fhtw.timetracker.CsvUtil;
import at.fhtw.timetracker.HolidayRequest;
import at.fhtw.timetracker.Status;
import at.fhtw.timetracker.User;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Controller class for handling holiday requests.
 *  * Allows users to request holidays, checks available holidays, and saves requests to a CSV file.
 */
public class HolidayRequestController {

    @FXML private Label availableHolidaysLabel;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    private User currentUser; // This should be set to the logged-in user

    /**
     * Sets the current logged-in user for this controller.
     * Updates the display of available holidays.
     * @param user The current logged-in user.
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateAvailableHolidaysDisplay();
    }

    /**
     * Updates the display of available holidays for the current user.
     */
    private void updateAvailableHolidaysDisplay() {
        currentUser.calculateAvailableHolidays();
        availableHolidaysLabel.setText("Available Holidays: " + currentUser.getAvailableHolidays());
    }

    /**
     * Saves the provided holiday request to the CSV file.
     * @param request The holiday request to be saved.
     */
    private void saveHolidayRequest(HolidayRequest request) {
        List<HolidayRequest> requests = CsvUtil.readCsv("holidayRequests.csv", HolidayRequest.class);
        requests.add(request);
        CsvUtil.writeCsv(requests, "holidayRequests.csv");
    }

    /**
     * Handles the action when the user requests a holiday.
     * Calculates the requested days, checks against available holidays, and saves the request.
     */
    @FXML
    private void handleHolidayRequest() {
        if (startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
            long requestedDays = ChronoUnit.DAYS.between(startDatePicker.getValue(), endDatePicker.getValue()) + 1;

            if (requestedDays <= currentUser.getAvailableHolidays()) {
                int requestId = generateHolidayRequestId();
                HolidayRequest newRequest = new HolidayRequest(
                        requestId,
                        currentUser.getId(),
                        startDatePicker.getValue(),
                        endDatePicker.getValue(),
                        Status.PENDING
                );
                saveHolidayRequest(newRequest);
            } else {
                //nothing happens
            }
        }
    }

    /**
     * Generates a new unique holiday request ID.
     * @return The new holiday request ID.
     */
    private int generateHolidayRequestId() {
        List<HolidayRequest> requests = CsvUtil.readCsv("holidayRequests.csv", HolidayRequest.class);
        return requests.stream()
                .mapToInt(HolidayRequest::getRequestId)
                .max()
                .orElse(0) + 1; // Generate a new unique ID
    }

}
