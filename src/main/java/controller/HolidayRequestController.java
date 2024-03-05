package controller;


import com.example.time.CsvUtil;
import com.example.time.HolidayRequest;
import com.example.time.Status;
import com.example.time.User;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class HolidayRequestController {

    @FXML private Label availableHolidaysLabel;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    private User currentUser; // This should be set to the logged-in user

    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateAvailableHolidaysDisplay();
    }

    private void updateAvailableHolidaysDisplay() {
        currentUser.calculateAvailableHolidays();
        availableHolidaysLabel.setText("Available Holidays: " + currentUser.getAvailableHolidays());
    }


    private void saveHolidayRequest(HolidayRequest request) {
        List<HolidayRequest> requests = CsvUtil.readCsv("holidayRequests.csv", HolidayRequest.class);
        requests.add(request);
        CsvUtil.writeCsv(requests, "holidayRequests.csv");
    }
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
                // ... rest of your logic ...
            } else {
                // Handle case where requested days exceed available holidays
            }
        }
    }
    private int generateHolidayRequestId() {
        List<HolidayRequest> requests = CsvUtil.readCsv("holidayRequests.csv", HolidayRequest.class);
        return requests.stream()
                .mapToInt(HolidayRequest::getRequestId)
                .max()
                .orElse(0) + 1; // Generate a new unique ID
    }

}
