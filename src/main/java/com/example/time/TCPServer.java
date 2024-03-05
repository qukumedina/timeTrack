package com.example.time;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TCPServer implements Runnable{
    private static int port = 6789;
    private static final String USERS_CSV_FILE = "users.csv"; // Path to your users CSV file
    private static final String TASKS_CSV_FILE = "tasks.csv"; // Path to your users CSV file
    private static final String HOLIDAY_REQUESTS_CSV_FILE = "holidayRequests.csv"; // Path to your holiday requests CSV file

    private final Queue<User> userList = new ConcurrentLinkedQueue<>();
    private final Queue<Task> taskList = new ConcurrentLinkedQueue<>();
    private final Queue<HolidayRequest> holidayRequests = new ConcurrentLinkedQueue<>();



    public static void main(String[] args) {
        new TCPServer().run();
    }



    @Override
    public void run() {
        List<User>  users = CsvUtil.readCsv(USERS_CSV_FILE, User.class);
        List<Task> tasks = CsvUtil.readTaskCsv(TASKS_CSV_FILE, Task.class);
        taskList.addAll(tasks);
        userList.addAll(users);
        ServerSocket server = null;

        try {
            System.out.println("Server is listening on port " + port);
            server = new ServerSocket(port);

            while (true) {
                Socket connectionSocket = server.accept();
                //clienthadler
                new ClientHandler(connectionSocket, userList, taskList, holidayRequests).run();

                // Handle other types of requests here
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Write the updated list to the CSV file

            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
