package com.example.time;

import java.time.LocalDate;
import java.util.List;

/**
 * The HolidayRequest class represents a holiday request made by a user.
 */
public class HolidayRequest {
    private int requestId;
    private int userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status status;

    // Constructors, getters, setters, etc.

    /**
     * Constructs a new HolidayRequest with the specified parameters
     *
     * @param requestId     The ID of the holiday request
     * @param userId        The ID of the user making the request
     * @param startDate     The start date of the holiday
     * @param endDate       The end date of the holiday
     * @param status        The status of the holiday request
     */
    public HolidayRequest(int requestId, int userId, LocalDate startDate, LocalDate endDate, Status status) {
        this.requestId = requestId;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getter and setter methods
    /**
     * Retrieves the ID of the holiday request.
     *
     * @return The ID of the holiday request
     */
    public int  getRequestId() {
        return requestId;
    }

    /**
     * Sets the ID of the holiday request.
     *
     * @param requestId The ID of the holiday request
     */
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    /**
     * Retrieves the ID of the user making the request.
     *
     * @return The ID of the user making the request
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user making the request.
     *
     * @param userId The ID of the user making the request
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Retrieves the start date of the holiday.
     *
     * @return The start date of the holiday
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the holiday.
     *
     * @param startDate The start date of the holiday
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Retrieves the end date of the holiday.
     *
     * @return The end date of the holiday
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the holiday.
     *
     * @param endDate The end date of the holiday
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Retrieves the status of the holiday request.
     *
     * @return The status of the holiday request
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the holiday request.
     *
     * @param status The status of the holiday request
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Saves the holiday request to a CSV file.
     *
     * @param request The holiday request to save
     */
    private void saveHolidayRequest(HolidayRequest request) {
        List<HolidayRequest> requests = CsvUtil.readCsv("holidayRequests.csv", HolidayRequest.class);
        requests.add(request);
        CsvUtil.writeCsv(requests, "holidayRequests.csv");
    }

}