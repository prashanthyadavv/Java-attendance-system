package attendance.services;

import attendance.database.DataStore;
import attendance.models.User;

/**
 * Authentication service for login and password management
 */
public class AuthService {
    private static AuthService instance;
    private DataStore dataStore;

    private AuthService() {
        dataStore = DataStore.getInstance();
    }

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    /**
     * Authenticate user with username and password
     */
    public User login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }

        User user = dataStore.getUserByUsername(username);

        if (user != null && user.getPassword().equals(password) && user.isActive()) {
            dataStore.setCurrentUser(user);
            return user;
        }

        return null;
    }

    /**
     * Logout current user
     */
    public void logout() {
        dataStore.logout();
    }

    /**
     * Get currently logged in user
     */
    public User getCurrentUser() {
        return dataStore.getCurrentUser();
    }

    /**
     * Reset password for a user (admin only)
     */
    public boolean resetPassword(int userId, String newPassword) {
        User user = dataStore.getUserById(userId);
        if (user != null && newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(newPassword);
            dataStore.updateUser(user);
            return true;
        }
        return false;
    }

    /**
     * Change password for current user
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        User currentUser = getCurrentUser();
        if (currentUser != null && currentUser.getPassword().equals(oldPassword)) {
            currentUser.setPassword(newPassword);
            dataStore.updateUser(currentUser);
            return true;
        }
        return false;
    }
}
