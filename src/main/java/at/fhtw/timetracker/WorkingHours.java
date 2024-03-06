package at.fhtw.timetracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * The WorkingHours class represents the working hours of a user on a specific date.
 */
public class WorkingHours {

    private LocalDate date;
    private String startTime;
    private String endTime;

    private String hoursDone;

    /**
     * Constructs a WorkingHours object with the specified date, start time, end time, and hours worked.
     *
     * @param date      the date of the working hours
     * @param startTime the start time of the working hours
     * @param endTime   the end time of the working hours
     * @param hoursDone the hours worked during the working hours
     */
    public WorkingHours(LocalDate date, String startTime, String endTime, String hoursDone) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hoursDone = hoursDone;
    }

    // Getter and setter methods


    /**
     * Returns the date of the working hours.
     *
     * @return the date of the working hours
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the working hours.
     *
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the start time of the working hours.
     *
     * @return the start time of the working hours
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the working hours.
     *
     * @param startTime the start time to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns the end time of the working hours.
     *
     * @return the end time of the working hours
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the working hours.
     *
     * @param endTime the end time to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Returns the hours worked during the working hours.
     *
     * @return the hours worked during the working hours
     */
    public String getHoursDone() {
        return hoursDone;
    }

    /**
     * Sets the hours worked during the working hours.
     *
     * @param hoursDone the hours worked to set
     */
    public void setHoursDone(String hoursDone) {
        this.hoursDone = hoursDone;
    }

    /**
     * Returns a string representation of the WorkingHours object.
     *
     * @return a string in the format "date,startTime,endTime,hoursDone"
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedString = date.format(formatter);
        return formattedString + "," + startTime + "," + endTime + "," + hoursDone;
    }
    // Method to calculate available holidays*/

}


