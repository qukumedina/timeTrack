package com.example.time;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientHandler implements Runnable{

    private final Socket connectionSocket;
    private final Queue<User> userList;
    private final Queue<Task> taskList;
    private final Queue<HolidayRequest> holidayRequests;

    public ClientHandler(Socket connectionSocket, Queue<User> userList, Queue<Task> taskList, Queue<HolidayRequest> holidayRequests) {
        this.connectionSocket = connectionSocket;
        this.userList = userList;
        this.taskList = taskList;
        this.holidayRequests = holidayRequests;
    }

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
        }/*
        else if (request.startsWith("ApproveRegistration:")) {
            String response = processApproveRegistrationRequest(request);
            outToClient.writeBytes(response + "\n");
        }else if (request.equals("GetHolidayRequests")) {
            String response = processGetHolidayRequests();
            outToClient.writeBytes(response + "\n");
        } else if (request.startsWith("ApproveHolidayRequest:")) {
            int requestId = Integer.parseInt(request.substring(22));
            String response = processApproveHolidayRequest(requestId);
            outToClient.writeBytes(response + "\n");
        } */else  if (request.equals("LoadTaskTrackingScreen")) {
            String response = processLoadTaskTrackingScreen();
            outToClient.writeBytes(response + "\n");
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

            System.out.println("User registered successfully: " + username + ", Role: " + role);
            return "Registration Successful";
        } catch (Exception e) {
            e.printStackTrace();
            return "Registration Failed: " + e.getMessage();
        }
    }



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

    private boolean validateLogin(String username, String password) {
        return userList.stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
    }

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


    private String processAssignTaskRequest(String request) {
        try {
            System.out.println("Received AssignTask request: " + request);

            String[] details = request.substring(11).split(",");
            if (details.length != 4) { // Check for three details: description, timeSpent, and taskId
                return "Invalid request format: Incorrect number of fields";
            }

            String description = details[0].trim();
            int timeSpent = Integer.parseInt(details[1].trim());

            // Check if the logged-in user is a manager
            //  User loggedInUser = UserSession.getLoggedInUser();
/*
            if (loggedInUser != null && loggedInUser.getRole() == Role.MANAGER) {
*/
            // If the user is a manager, they can assign the task
            Task newTask = new Task(CsvUtil.generateTaskId(), description, timeSpent, Role.MANAGER);

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


    /*
    private static String processApproveRegistrationRequest(String request) {
        try {
            // Extract the username from the request
            String username = request.substring(20); // Adjust substring index as needed

            List<User> users = CsvUtil.readCsv(USERS_CSV_FILE, User.class);
            boolean userFound = false;

            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    user.setStatus(Status.APPROVED);
                    userFound = true;
                    break;
                }
            }

            if (userFound) {
                CsvUtil.writeCsv(users, USERS_CSV_FILE);
                return "Registration Approved Successfully";
            } else {
                return "User not found for Username: " + username;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Registration Approval Failed: " + e.getMessage();
        }
    }


    private static String processGetHolidayRequests() {
        try {
            // Read holiday requests from CSV and convert to a string format
            List<HolidayRequest> requests = CsvUtil.readCsv(HOLIDAY_REQUESTS_CSV_FILE, HolidayRequest.class);
            return CsvUtil.convertListToCsvString(requests);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private static String processApproveHolidayRequest(int requestId) {
        try {
            List<HolidayRequest> requests = CsvUtil.readCsv(HOLIDAY_REQUESTS_CSV_FILE, HolidayRequest.class);
            for (HolidayRequest request : requests) {
                if (request.getRequestId() == requestId) {
                    request.setStatus(Status.APPROVED);
                    break;
                }
            }
            CsvUtil.writeCsv(requests, HOLIDAY_REQUESTS_CSV_FILE);
            return "Holiday Request Approved Successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Holiday Request Approval Failed: " + e.getMessage();
        }
    }

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
