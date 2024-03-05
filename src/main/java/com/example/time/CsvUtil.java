// CsvUtil.java
package com.example.time;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvUtil {
    private static final String USERS_CSV_FILE = "users.csv";
    private static final String Tasks_CSV_FILE = "tasks.csv";

    private static final String COMMA_DELIMITER = ",";
    private static int lastAssignedUserId = 0;

    private static final String NEW_LINE_SEPARATOR = "\n";

    // Write data to CSV file
    public static <T> void writeCsv(List<T> dataList, String USERS_CSV_FILE) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_CSV_FILE))) {
            for (T data : dataList) {
                writer.write(data.toString());
                writer.write(NEW_LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception or throw a custom exception if needed
        }
    }
    public static <T> void writeTaskCsv(List<T> dataList, String tasks_CSV_FILE) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tasks_CSV_FILE))) {
            for (T data : dataList) {
                writer.write(data.toString());
                writer.write(NEW_LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception or throw a custom exception if needed
        }
    }

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


    // Convert a list of objects to a CSV-formatted string
    public static <T> String convertListToCsvString(List<T> dataList) {
        StringBuilder sb = new StringBuilder();
        for (T data : dataList) {
            sb.append(data.toString());
            sb.append(NEW_LINE_SEPARATOR);
        }
        return sb.toString();
    }



    // Create an instance of the data class based on the CSV line
    // Assumes lastAssignedUserId is a static member of CsvUtil and managed there.

    // Adjusted method to create a User instance without needing an ID from CSV
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


    // Add the following method for generating a unique task ID
    public static int generateTaskId() {
        List<Task> tasks = readCsv("tasks.csv", Task.class);
        int maxId = tasks.stream()
                .mapToInt(Task::getTaskId)
                .max()
                .orElse(0);

        return maxId + 1;
    }
    // Increment the last assigned user ID and return the new value
    public static int generateUserId() {
        return ++lastAssignedUserId;
    }
}
