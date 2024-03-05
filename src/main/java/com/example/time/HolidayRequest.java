package com.example.time;

import java.time.LocalDate;
import java.util.List;

// HolidayRequest.java
public class HolidayRequest {
    private int requestId;
    private int userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status status;

    // Constructors, getters, setters, etc.

    public HolidayRequest(int requestId, int userId, LocalDate startDate, LocalDate endDate, Status status) {
        this.requestId = requestId;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getter and setter methods
    public int  getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    private void saveHolidayRequest(HolidayRequest request) {
        List<HolidayRequest> requests = CsvUtil.readCsv("holidayRequests.csv", HolidayRequest.class);
        requests.add(request);
        CsvUtil.writeCsv(requests, "holidayRequests.csv");
    }

}