package at.fhtw.timetracker;

/**
 * The User class represents a user in the system with information such as username, password, role, and working hours.
 */
public class User {

        private int id;
        private String username;
        private String password;
        private Role role;
        private int maxWorkingHours; // New field
        private Status status; // New field
    private static int lastAssignedUserId = 0; // Class variable for tracking last ID

    private int workedMonths;
    private int availableHolidays;

    /**
     * Constructs a new User with the specified parameters.
     *
     * @param id             the user ID
     * @param username       the username
     * @param password       the password
     * @param role           the role of the user (EMPLOYEE or MANAGER)
     * @param maxWorkingHours the maximum working hours per week
     */
        public User(int id, String username, String password, Role role, int maxWorkingHours) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.role = role;
            this.maxWorkingHours = maxWorkingHours;
            this.status = Status.PENDING; // Default status
        }
    /**
     * Constructs a new User with auto-assigned ID and the specified parameters.
     *
     * @param username the username
     * @param password the password
     * @param role     the role of the user (EMPLOYEE or MANAGER)
     */
        public User(String username, String password, Role role) {
        this.id = ++lastAssignedUserId; // Auto-increment ID
        this.username = username;
        this.password = password;
        this.role = role;
        this.maxWorkingHours = 0; // default value
        this.status = Status.PENDING;
    }


        // Getter and setter methods
    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
        public int getId() {
            return id;
        }

    /**
     * Sets the user ID.
     *
     * @param id the user ID
     */
        public void setId(int id) {
            this.id = id;
        }

    /**
     * Gets the username.
     *
     * @return the username
     */
        public String getUsername() {
            return username;
        }

    /**
     * Sets the username.
     *
     * @param username the username
     */
        public void setUsername(String username) {
            this.username = username;
        }

    /**
     * Gets the password.
     *
     * @return the password
     */
        public String getPassword() {
            return password;
        }

    /**
     * Sets the password.
     *
     * @param password the password
     */
        public void setPassword(String password) {
            this.password = password;
        }

    /**
     * Gets the role of the user.
     *
     * @return the role of the user
     */
        public Role getRole() {
            return role;
        }

    /**
     * Sets the role of the user.
     *
     * @param role the role of the user
     */
        public void setRole(Role role) {
            this.role = role;
        }
    /**
     * Gets the maximum working hours per week.
     *
     * @return the maximum working hours per week
     */
        public int getMaxWorkingHours() {
        return maxWorkingHours;
    }

    /**
     * Sets the maximum working hours per week.
     *
     * @param maxWorkingHours the maximum working hours per week
     */
    public void setMaxWorkingHours(int maxWorkingHours) {
        this.maxWorkingHours = maxWorkingHours;
    }

    /**
     * Gets the status of the user.
     *
     * @return the status of the user
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the user.
     *
     * @param status the status of the user
     */
    public void setStatus(Status status) {
        this.status = status;
    }
        @Override
        public String toString() {
            return username + "," + password + "," + role;
        }
    // Method to calculate available holidays
    public void calculateAvailableHolidays() {
        this.availableHolidays = Math.min(workedMonths * 2, 25); // Max 25 days per year
    }

    // Getters and setters for new fields
    public int getWorkedMonths() {
        return workedMonths;
    }

    public void setWorkedMonths(int workedMonths) {
        this.workedMonths = workedMonths;
        calculateAvailableHolidays(); // Update available holidays whenever worked months change
    }

    public int getAvailableHolidays() {
        return availableHolidays;
    }
    }



