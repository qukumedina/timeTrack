/*
package com.example.time;

import com.example.time.User;

public class UserSession {
    private static final ThreadLocal<User> loggedInUser = new ThreadLocal<>();

    public static void setLoggedInUser(User user) {
        loggedInUser.set(user);
        System.out.println("UserSession: Logged in user set to " + user.getUsername());
    }

    public static User getLoggedInUser() {
        User user = loggedInUser.get();
        if (user == null) {
            System.out.println("UserSession: No user is currently logged in.");
        } else {
            System.out.println("UserSession: Current logged-in user is " + user.getUsername());
        }
        return user;
    }
}
*/
