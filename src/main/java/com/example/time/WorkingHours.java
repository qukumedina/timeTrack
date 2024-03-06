package com.example.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

// User.java
public class WorkingHours {

    private LocalDate date;
    private String startTime;
    private String endTime;

    private String hoursDone;


    public WorkingHours(LocalDate date, String startTime, String endTime, String hoursDone) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hoursDone = hoursDone;
    }

    // Getter and setter methods


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getHoursDone() {
        return hoursDone;
    }

    public void setHoursDone(String hoursDone) {
        this.hoursDone = hoursDone;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedString = date.format(formatter);
        return formattedString + "," + startTime + "," + endTime + "," + hoursDone;
    }
    // Method to calculate available holidays*/

}


