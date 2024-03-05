package com.example.time;

// User.java
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

        public User(int id, String username, String password, Role role, int maxWorkingHours) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.role = role;
            this.maxWorkingHours = maxWorkingHours;
            this.status = Status.PENDING; // Default status
        }
    public User(String username, String password, Role role) {
        this.id = ++lastAssignedUserId; // Auto-increment ID
        this.username = username;
        this.password = password;
        this.role = role;
        this.maxWorkingHours = 0; // You might want to set a default value or handle it differently
        this.status = Status.PENDING;
    }


        // Getter and setter methods
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }
    public int getMaxWorkingHours() {
        return maxWorkingHours;
    }

    public void setMaxWorkingHours(int maxWorkingHours) {
        this.maxWorkingHours = maxWorkingHours;
    }

    public Status getStatus() {
        return status;
    }

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



