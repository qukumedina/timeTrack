# Time Tracker

## Overview
This project aims to create an efficient platform for tracking work hours and logging tasks. It provides features for user registration, work hour recording, task tracking and access rights management.

### Developers
- Quku Jasmina, ic22b088
- Quku Medina, ic22b090
- Sala Klesti, ic23b047

## Setup Instructions
1. Clone the repository.
2. Open the project in your preferred Java IDE(please select maven and correto-17).
3. Run the Server application file (`TCPServer.java`) to start the connection.
4. Proceed by running the Client application file (`TCPClient.java`) to open the client view.
5. When you are done using the application, to make sure each new entry(from registration of a new user, task, or working hours) is saved run the shutdown server file (`ServerShutdowner.java`) and each .csv file should be updated successfully.
6. When running the `ServerShutdowner.java` you must type "shutdown" on the terminal.

## Key Classes and Functions

### `UserRegistrationController`
- `registerUser()`: Handles user registration process.
    - Parameters: None.
    - Returns: Void.
    - Functionality:
        - Collects username, password, and role information from the user interface.
        - Sends registration request to the server.
        - Displays success or failure message based on server response.

### `TaskTrackingController`
- `trackTask()`: Handles task tracking process.
    - Parameters: None.
    - Returns: Void.
    - Functionality:
        - Collects task name, task ID, time spent, and description from the user interface.
        - Validates input fields.
        - Sends task tracking request to the server.
        - Displays success or failure message based on server response.

### `WorkingHoursController`
- `trackTime()`: Handles work time tracking process.
    - Parameters: None.
    - Returns: Void.
    - Functionality:
        - Collects date, start time, and end time from the user interface.
        - Calculates total hours worked.
        - Sends work time tracking request to the server.
        - Displays success or failure message based on server response.

### `TCPClient`
- `sendRequestToServer()`: Sends a generic request to the server.
    - Parameters:
        - `request`: The request message to be sent.
    - Returns:
        - `serverResponse`: The response received from the server.
    - Functionality:
        - Establishes a TCP connection with the server.
        - Sends the request message to the server.
        - Receives and returns the server's response.

### `User`
- Represents a user in the system.
- Properties:
    - `username`: The username of the user.
    - `password`: The password of the user.
    - `role`: The role of the user (EMPLOYEE or MANAGER).

### `Task`
- Represents a task logged by a user.
- Properties:
    - `taskId`: The unique ID of the task.
    - `description`: The description of the task.
    - `timeSpent`: The time spent on the task.
    - `role`: The role associated with the task (EMPLOYEE or MANAGER).

### `Role`
- Enum representing user roles (EMPLOYEE, MANAGER).

## Project Structure
- `controller`: Contains JavaFX controllers for user interfaces.
- `com.example.time`: Contains utility classes for CSV handling and user/task classes.
- `TCPClient`: Handles client-server communication using TCP.


## Requirements
- Java JDK 11 or 17
- JavaFX library
- TCP/IP connectivity for client-server communication

## Usage
1. **User Registration**:
    - Users can register with a username, password, and role.
    - Users can successfully log in, if they are already registered.

2. **Task Tracking**:
    - Users can log tasks with descriptions and time spent.

3. **Work Time Tracking**:
    - Users can record work hours with start and end times.
    - Users can get the total amount of working hours per day based on it.

4. **Vacation Management**:
    - Users can request vacation days.(attempted,not done)

## Notes
- The project uses JavaFX for the graphical user interface.
- Communication between client and server is handled using TCP/IP sockets.
- File I/O is used for storing user data (CSV files).
- Multithreading is implemented for background tasks.
- Access rights and user roles are enforced for security.
- Exception handling is in place for error scenarios.
