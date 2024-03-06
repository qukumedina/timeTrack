// CsvUtil.java
package at.fhtw.timetracker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The CSVUtil class provides utility methods for reading from and writing to CSV files.
 */
public class CsvUtil {
    private static final String USERS_CSV_FILE = "users.csv";
    private static final String Tasks_CSV_FILE = "tasks.csv";

    private static final String TrackTime_CSV_FILE = "workinghours.csv";

    private static final String COMMA_DELIMITER = ",";
    private static int lastAssignedUserId = 0;

    private static final String NEW_LINE_SEPARATOR = "\n";

    /**
     * Writes a list of objects to the users CSV file.
     *
     * @param dataList  The list of objects to write to the CSV file
     * @param USERS_CSV_FILE    The name of the CSV file to write to
     * @param <T>   The type of objects in the list
     */
    // Write data to CSV file
    public static <T> void writeCsv(List<T> dataList, String USERS_CSV_FILE) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_CSV_FILE))) {
            for (T data : dataList) {
                writer.write(data.toString());
                writer.write(NEW_LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *Writes a list of objects to the tasks CSV file.
     *
     * @param dataList  The list of objects to write to the CSV file
     * @param tasks_CSV_FILE    The name of the CSV file to write to
     * @param <T>   The type of objects in the list
     */
    public static <T> void writeTaskCsv(List<T> dataList, String tasks_CSV_FILE) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tasks_CSV_FILE))) {
            for (T data : dataList) {
                writer.write(data.toString());
                writer.write(NEW_LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *Writes a list of objects to the working hours CSV file.
     *
     * @param dataList  The list of objects to write to the CSV file
     * @param  TrackTime_CSV_FILE  The name of the CSV file to write to
     * @param <T>   The type of objects in the list
     */
    public static <T> void writeTrackTimeCsv(List<T> dataList, String TrackTime_CSV_FILE) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TrackTime_CSV_FILE))) {
            for (T data : dataList) {
                writer.write(data.toString());
                writer.write(NEW_LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads data from the users CSV file and returns a list of objects.
     *
     * @param fileName  The name of the CSV file to read from
     * @param dataClass The class of the objects to create from the CSV data
     * @param <T>   The type of objects to return
     * @return  A list of objects read from the CSV file
     */
    // Read data from CSV file
    public static <T> List<T> readCsv(String fileName, Class<T> dataClass) {
        List<T> dataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader
                (new InputStreamReader(
                        new FileInputStream(fileName),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // The ID will be generated within the createDataInstance method.
                T data = createDataInstance(line, dataClass);
                if (data != null) {
                    dataList.add(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Reads data from the task CSV file and returns a list of objects.
     *
     * @param fileName  The name of the CSV file to read from
     * @param dataClass The class of the objects to create from the CSV data
     * @param <T>   The type of objects to return
     * @return  A list of objects read from the CSV file
     */
    public static <T> List<T> readTaskCsv(String fileName, Class<T> dataClass) {
        List<T> dataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader
                (new InputStreamReader(
                        new FileInputStream(fileName),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // The ID will be generated within the createDataInstance method.
                T data = createTaskInstance(line, dataClass);
                if (data != null) {
                    dataList.add(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Reads data from the working hours CSV file and returns a list of objects.
     *
     * @param fileName  The name of the CSV file to read from
     * @param dataClass The class of the objects to create from the CSV data
     * @param <T>   The type of objects to return
     * @return  A list of objects read from the CSV file
     */
    public static <T> List<T> readTrackTimeCsv(String fileName, Class<T> dataClass) {
        List<T> dataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader
                (new InputStreamReader(
                        new FileInputStream(fileName),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // The ID will be generated within the createDataInstance method.
                T data = createTrackInstance(line, dataClass);
                if (data != null) {
                    dataList.add(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }


    // Convert a list of objects to a CSV-formatted string

    /**
     * Converts a list of objects to a CSV-formatted string.
     *
     * @param dataList  The List of objects to convert
     * @return      A CSV-formatted string representing the list of objects
     * @param <T>   The type of objects in the list
     */
    public static <T> String convertListToCsvString(List<T> dataList) {
        StringBuilder sb = new StringBuilder();
        for (T data : dataList) {
            sb.append(data.toString());
            sb.append(NEW_LINE_SEPARATOR);
        }
        return sb.toString();
    }


    /**
     * Creates an instance of the working hours class based on the CSV-formatted line
     *
     * @param line  The CSV-formatted line
     * @param dataClass The class of the object to create
     * @param <T>   The type of objects to create
     * @return  An instance of the Track class created from the CSV line
     */
    public static <T> T createTrackInstance(String line, Class<T> dataClass) {
        String[] fields = line.split(COMMA_DELIMITER);
        try {
            if (fields.length==4 ) {
                // Adjusted to the new format: username, password, role
                String date = fields[0].trim();
                String startTime = fields[1].trim();
                String endTime = fields[2].trim();
                String hoursDone = fields[3].trim();

                // Create a new User instance with auto-generated ID

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                return dataClass.getDeclaredConstructor(LocalDate.class, String.class,String.class, String.class)
                        .newInstance(LocalDate.parse(date, formatter), startTime,endTime, hoursDone);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Create an instance of the data class based on the CSV line
    // Assumes lastAssignedUserId is a static member of CsvUtil and managed there.

    // Adjusted method to create a User instance without needing an ID from CSV
    /**
     * Creates an instance of the User class based on the CSV-formatted line
     *
     * @param line  The CSV-formatted line
     * @param dataClass The class of the object to create
     * @param <T>   The type of objects to create
     * @return  An instance of the Track class created from the CSV line
     */
    public static <T> T createDataInstance(String line, Class<T> dataClass) {
        String[] fields = line.split(COMMA_DELIMITER);
        try {
            if (fields.length > 1) {
                // Adjusted to the new format: username, password, role
                String username = fields[0].trim();
                String password = fields[1].trim();
                Role role = Role.valueOf(fields[2].trim()); // Assuming role is the third field

                // Create a new User instance with auto-generated ID
                T data = dataClass.getDeclaredConstructor(String.class, String.class, Role.class)
                        .newInstance(username, password, role);

                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Creates an instance of the Task class based on the CSV-formatted line
     *
     * @param line  The CSV-formatted line
     * @param dataClass The class of the object to create
     * @param <T>   The type of objects to create
     * @return  An instance of the Track class created from the CSV line
     */
    public static <T> T createTaskInstance(String line, Class<T> dataClass) {
        String[] fields = line.split(COMMA_DELIMITER);
        try {
            if (fields.length==4 ) {
                // Adjusted to the new format: username, password, role
                int taskId = Integer.parseInt(fields[0].trim());
                String description = fields[1].trim();
                int timeSpent = Integer.parseInt(fields[2].trim());
                Role assignedRole = Role.valueOf(fields[3].trim());

                // Create a new User instance with auto-generated ID
                T data = dataClass.getDeclaredConstructor(int.class, String.class,int.class, Role.class)
                        .newInstance(taskId, description,timeSpent, assignedRole);

                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Generates a new task ID.
     *
     * @return  The generated task ID.
     */
    // Add the following method for generating a unique task ID
    public static int generateTaskId() {
        List<Task> tasks = readCsv("tasks.csv", Task.class);
        int maxId = tasks.stream()
                .mapToInt(Task::getTaskId)
                .max()
                .orElse(0);

        return maxId + 1;
    }

    /**
     * Generates a new user ID.
     *
     * @return  The generated used ID.
     */
    // Increment the last assigned user ID and return the new value
    public static int generateUserId() {
        return ++lastAssignedUserId;
    }
}
