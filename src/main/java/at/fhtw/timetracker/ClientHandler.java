package at.fhtw.timetracker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The ClientHandler class handles client requests in a multithreaded server application.
 * It implements the Runnable interface to run as a separate thread for each client connection.
 */
public class ClientHandler implements Runnable{

    private final Socket connectionSocket;
    private final Queue<User> userList;
    private final Queue<Task> taskList;
    private final Queue<WorkingHours> workingHours;
    private final Queue<HolidayRequest> holidayRequests;

    private AtomicBoolean isShutdown;

    /**
     *
     * Constructs a ClientHandler object with the given parameters.
     *
     * @param connectionSocket The client's socket connection
     * @param userList          The queue of users
     * @param taskList          The queue of tasks
     * @param workingHours      The queue of working hours
     * @param holidayRequests   The queue of holiday requests
     * @param isShutdown        AtomicBoolean flag to indicate server shutdown
     */
    public ClientHandler(Socket connectionSocket, Queue<User> userList, Queue<Task> taskList, Queue<WorkingHours> workingHours, Queue<HolidayRequest> holidayRequests, AtomicBoolean isShutdown) {
        this.connectionSocket = connectionSocket;
        this.userList = userList;
        this.taskList = taskList;
        this.holidayRequests = holidayRequests;
        this.workingHours = workingHours;
        this.isShutdown = isShutdown;
    }

    /**
     * Executes the client request handling logic.
     * Reads client requests, processes them, and sends back responses.
     */
    @Override
    public void run() {
        BufferedReader inFromClient = null;
        try {
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

        String request = inFromClient.readLine();
        System.out.println("Received from client: " + request);

        if (request.startsWith("Login:")) {
            String response = processLoginRequest(request);
            outToClient.writeBytes(response + "\n");
        } else if (request.startsWith("Register:")) {
            String response = processRegisterRequest(request);
            outToClient.writeBytes(response + "\n");
        }else if (request.startsWith("AssignTask:")) {
            String response = processAssignTaskRequest(request);
            outToClient.writeBytes(response + "\n");
        }else if (request.startsWith("Track Time:")) {
            String response = processTrackTimeRequest(request);
            outToClient.writeBytes(response + "\n");
        }else  if (request.equals("LoadTaskTrackingScreen")) {
            String response = processLoadTaskTrackingScreen();
            outToClient.writeBytes(response + "\n");
        }else  if (request.equals("SHUTDOWN")) {
            isShutdown.set(true);
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Processes a registration request from the client.
     *
     * @param request   The registartion request from the client
     * @return          A response indicating the result of the registration attempt
     */
    private String processRegisterRequest(String request) {
        try {
            String[] details = request.substring(9).split(",");
            String username = details[0];
            String password = details[1];
            String roleStr = details[2];

            // Check if username already exists
            boolean userExists = userList.stream().anyMatch(user -> user.getUsername().equals(username));
            if (userExists) {
                return "Registration Failed: Username already exists";
            }

            // Determine the role based on the roleStr parameter
            Role role = Role.EMPLOYEE; // Default role
            if ("MANAGER".equalsIgnoreCase(roleStr)) {
                role = Role.MANAGER;
            }

            // Add logic to register the user with the specified role
            User newUser = new User(CsvUtil.generateUserId(), username, password, role, 40); // Set the role
            userList.add(newUser);
            //CsvUtil.writeCsv(Arrays.asList(userList.toArray()), "users.csv");
            System.out.println("User registered successfully: " + username + ", Role: " + role);
            return "Registration Successful";
        } catch (Exception e) {
            e.printStackTrace();
            return "Registration Failed: " + e.getMessage();
        }
    }

    /**
     * Processes a login request from the client.
     *
     * @param request   The login request from the client
     * @return          A response indicating the result of the login attempt
     */
    private String processLoginRequest(String request) {
        String[] details = request.substring(6).split(",");
        String username = details[0];
        String password = details[1];
        boolean isValid = validateLogin(username, password);

        if (isValid) {
            User user = getUserByUsername(username);
            // Set the logged-in user here
            //  UserSession.setLoggedInUser(user);
            return "Login Successful:" + (user.getRole() == Role.MANAGER ? "Manager" : "Employee");
        } else {
            return "Login Failed";
        }
    }

    /**
     * Processes a track time request from the client.
     *
     * @param request   The track time request from the client
     * @return          A response indicating the result of the track time operation
     */
    private String processTrackTimeRequest(String request) {
        String[] details = request.substring(11).split(",");
        String date = details[0];
        String startTime = details[1];
        String endTime = details[2];
        String hoursDone = details[3];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        WorkingHours newTrackTime = new WorkingHours(LocalDate.parse(date, formatter), startTime, endTime, hoursDone);

        //List<Task> tasks = CsvUtil.readTaskCsv(TASKS_CSV_FILE, Task.class);
        workingHours.add(newTrackTime);
        //CsvUtil.writeTaskCsv(tasks, TASKS_CSV_FILE);

        System.out.println("Time Tracked Successfully");
        return "ime Tracked Successfully";
    }

    /**
     * Validates a user's login credentials.
     *
     * @param username  The username to validate
     * @param password  The password to validate
     * @return          true, if the credentials are valid, false otherwise
     */
    private boolean validateLogin(String username, String password) {
        return userList.stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
    }

    /**
     * Retrieves a user object by username
     *
     * @param username  The username of the user to retrieve
     * @return          The User object corresponding to the username, or null if not found
     */
    private User getUserByUsername(String username) {
        return userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }


    private Role getRoleFromId(int roleId) {
        switch (roleId) {
            case 1: return Role.EMPLOYEE;
            case 2: return Role.MANAGER;
            // add other cases as needed
            default: throw new IllegalArgumentException("Invalid role ID");
        }
    }


    /**
     * Processes an assigned task request from the client
     *
     * @param request   The assigned task request from the client
     * @return          A response indicating the result of the assigned task operation
     */
    private String processAssignTaskRequest(String request) {
        try {
            System.out.println("Received AssignTask request: " + request);

            String[] details = request.substring(11).split(",");
            if (details.length != 3) { // Check for three details: description, timeSpent, and taskId
                return "Invalid request format: Incorrect number of fields";
            }

            int taskId = Integer.parseInt(details[0].trim());
            String description = details[1].trim();
            int timeSpent = Integer.parseInt(details[2].trim());

            // Check if the logged-in user is a manager
            //  User loggedInUser = UserSession.getLoggedInUser();
/*
            if (loggedInUser != null && loggedInUser.getRole() == Role.MANAGER) {
*/
            // If the user is a manager, they can assign the task

            Task newTask = new Task(taskId, description, timeSpent, Role.EMPLOYEE);

            //List<Task> tasks = CsvUtil.readTaskCsv(TASKS_CSV_FILE, Task.class);
            taskList.add(newTask);
            //CsvUtil.writeTaskCsv(tasks, TASKS_CSV_FILE);

            System.out.println("Task Assigned Successfully");
            return "Task Assigned Successfully";
          /*  } else {
                // If the user is not a manager, they cannot assign the task
                return "Task Assignment Failed: Only managers can assign tasks.";
            }*/
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "Task Assignment Failed: Invalid number format"+e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Task Assignment Failed " + e.getMessage();
        }
    }

    /**
     * Determines if the task tracking screen can be loaded.
     *
     * @return  A message indicating whether the screen can be loaded or access is denied
     */
    private static String processLoadTaskTrackingScreen() {
        // Add your logic here to determine if the task tracking screen can be loaded
        // For example, check user roles, permissions, etc.
        // For simplicity, this example assumes all users can access the task tracking screen

        // Example logic: Check if the user has the necessary role (this is a placeholder)
        boolean userHasAccess = true; // Placeholder for actual access check

        if (userHasAccess) {
            return "Proceed to Task Tracking";
        } else {
            return "Access Denied";
        }
    }
}
