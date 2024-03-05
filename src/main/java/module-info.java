module com.example.time {
    requires javafx.controls;
    requires javafx.fxml;

    opens controller to javafx.fxml; // Add this line

    exports com.example.time;
}
