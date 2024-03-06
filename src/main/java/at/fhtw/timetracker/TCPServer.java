package at.fhtw.timetracker;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The TCPServer class represents a TCP server that handles client requests.
 */
public class TCPServer implements Runnable{
    private static final String USERS_CSV_FILE = "users.csv"; // Path to your users CSV file
    private static final String TASKS_CSV_FILE = "tasks.csv"; // Path to your tasks CSV file
    private static final String TrackTime_CSV_FILE = "workinghrs.csv"; // Path to your working hours CSV file

    private static final String HOLIDAY_REQUESTS_CSV_FILE = "holidayRequests.csv"; // Path to your holiday requests CSV file

    private final Queue<User> userList = new ConcurrentLinkedQueue<>();
    private final Queue<Task> taskList = new ConcurrentLinkedQueue<>();
    private final Queue<WorkingHours> workingHoursList = new ConcurrentLinkedQueue<>();

    private final Queue<HolidayRequest> holidayRequests = new ConcurrentLinkedQueue<>();

    private AtomicBoolean isShutdown = new AtomicBoolean(false);

    /**
     * The main entry point for the TCP server.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        new TCPServer().run();
    }



    @Override
    public void run() {
        List<User>  users = CsvUtil.readCsv(USERS_CSV_FILE, User.class);
        List<Task> tasks = CsvUtil.readTaskCsv(TASKS_CSV_FILE, Task.class);
        List<WorkingHours> workingHours1 = CsvUtil.readTrackTimeCsv(TrackTime_CSV_FILE, WorkingHours.class);

        taskList.addAll(tasks);
        userList.addAll(users);
        workingHoursList.addAll(workingHours1);

        ServerSocket server = null;

        try {
            int port = 6789;
            System.out.println("Server is listening on port " + port);
            server = new ServerSocket(port);

            while (!isShutdown.get()) {
                Socket connectionSocket = server.accept();
                new ClientHandler(connectionSocket, userList, taskList, workingHoursList, holidayRequests, isShutdown).run();

                String command;
                /*BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                while ( (command = reader.readLine()) != null ) {
                    if ("shutdown".equals(command)) {
                        shutdown();
                    }
                }*/
            }
        } catch (SocketException e) {
            System.out.println("FINALLY not socketed");

            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("FINALLY not");

            e.printStackTrace();
        } finally {
            // Write the updated list to the CSV file
            System.out.println("FINALLY");
            List<User> users1 = new ArrayList<>(userList);
            CsvUtil.writeCsv(users1, USERS_CSV_FILE);

            List<Task> tasks1 = new ArrayList<>(taskList);
            CsvUtil.writeTaskCsv(tasks1, TASKS_CSV_FILE);

            List<WorkingHours> hours = new ArrayList<>(workingHoursList);
            CsvUtil.writeTrackTimeCsv(hours, TrackTime_CSV_FILE);

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
