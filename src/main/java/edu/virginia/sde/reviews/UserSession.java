package edu.virginia.sde.reviews;

public class UserSession {
    private static UserSession instance;
    private int userId;
    private String username;

    private UserSession() {
        // Private constructor to prevent instantiation
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void clearSession() {
        userId = 0;
        username = null;
        // Other session data to clear if needed
    }
}
