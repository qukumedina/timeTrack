package at.fhtw.timetracker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * The UserTotalHoursCalculator class is responsible for calculating the total hours
 *  * spent by a user on tasks asynchronously using a thread pool.
 */
public class UserTotalHoursCalculator {
    // You can adjust the number of threads in the pool based on your system's capabilities
    private static final int THREAD_POOL_SIZE = 4;

    private ExecutorService executorService;

    /**
     * Constructs a UserTotalHoursCalculator with a fixed-size thread pool.
     */
    public UserTotalHoursCalculator() {
        // Create a fixed-size thread pool
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    /**
     * Calculates the total hours spent by a user on tasks asynchronously.
     *
     * @param user  the user for whom to calculate total hours
     * @param tasks the list of tasks to consider for the calculation
     * @return a Future representing the result of the calculation
     */
    public Future<Integer> calculateTotalHoursAsync(User user, List<Task> tasks) {
        // Submit the calculation task to the thread pool
        return executorService.submit(() -> calculateTotalHours(user, tasks));
    }

    /**
     * Calculates the total hours spent by a user on tasks.
     *
     * @param user  the user for whom to calculate total hours
     * @param tasks the list of tasks to consider for the calculation
     * @return the total hours spent by the user on tasks
     */
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

    /**
     * Shuts down the thread pool when it's no longer needed.
     */
    public void shutdown() {
        // Shutdown the thread pool when it's no longer needed
        executorService.shutdown();
    }
}

