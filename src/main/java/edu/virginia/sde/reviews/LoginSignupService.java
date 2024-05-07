package edu.virginia.sde.reviews;

import java.sql.SQLException;

public class LoginSignupService {

    public LoginSignupService() {
        // Empty constructor on purpose.
    }

    public boolean isExistingUsername(String username) {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            boolean isExistingUsername = databaseDriver.isExistingUsername(username);
            databaseDriver.disconnect();
            return isExistingUsername;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isCorrectPassword(String username, String password) {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            String realPassword = databaseDriver.getPasswordFromUsername(username);
            databaseDriver.disconnect();
            return realPassword.equals(password);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    public void addAccount(String username, String password) {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            databaseDriver.addAccount(username, password);
            databaseDriver.commit();
            databaseDriver.disconnect();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
