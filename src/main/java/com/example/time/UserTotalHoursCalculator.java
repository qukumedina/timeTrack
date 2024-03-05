package com.example.time;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


// UserTotalHoursCalculator.java
public class UserTotalHoursCalculator {
    // You can adjust the number of threads in the pool based on your system's capabilities
    private static final int THREAD_POOL_SIZE = 4;

    private ExecutorService executorService;

    public UserTotalHoursCalculator() {
        // Create a fixed-size thread pool
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public Future<Integer> calculateTotalHoursAsync(User user, List<Task> tasks) {
        // Submit the calculation task to the thread pool
        return executorService.submit(() -> calculateTotalHours(user, tasks));
    }

    private int calculateTotalHours(User user, List<Task> tasks) {
        int totalHours = 0;
        // Calculate total hours for the user from the list of tasks
        for (Task task : tasks) {
            if (task.getTaskId() == user.getId()) {
                totalHours += task.getTimeSpent();
            }
        }
        return totalHours;
    }

    public void shutdown() {
        // Shutdown the thread pool when it's no longer needed
        executorService.shutdown();
    }
}

