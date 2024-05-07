package edu.virginia.sde.reviews;

import java.sql.*;

public class DatabaseDriver {

    private Connection connection;

    public DatabaseDriver() {
        // Empty constructor on purpose.
    }

    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            throw new IllegalStateException("The connection is already opened");
        }
        connection = DriverManager.getConnection("jdbc:sqlite:course_reviews.sqlite");
        connection.createStatement().execute("PRAGMA foreign_keys = ON");
        connection.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    public void createTablesIfNeeded() throws SQLException {
        // Create AccountInfo table.
        String accountInfoSql = "CREATE TABLE IF NOT EXISTS AccountInfo (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Username TEXT NOT NULL UNIQUE, " +
                "Password TEXT NOT NULL)";
        Statement statement = connection.createStatement();
        statement.executeUpdate(accountInfoSql);
        // Create Course table
        String courseTableSql = "CREATE TABLE IF NOT EXISTS Courses (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CourseMnemonic TEXT NOT NULL, " +
                "CourseName TEXT NOT NULL UNIQUE, " +
                "CourseNumber INTEGER NOT NULL, " +
                "AverageCourseRating FLOAT NOT NULL)";
        statement.executeUpdate(courseTableSql);
        // Create Ratings table
        String ratingTableSql = "CREATE TABLE IF NOT EXISTS Ratings (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Rating INTEGER NOT NULL, " +
                "TimeStamp TIMESTAMP NOT NULL, " +
                "Comment TEXT, " +
                "UserID INTEGER NOT NULL UNIQUE, " +
                "FOREIGN KEY (UserID) REFERENCES AccountInfo(ID))";
        statement.executeUpdate(ratingTableSql);
        // TODO: other tables
    }

    public void insertIntoAccountInfo(String username, String password) throws SQLException{
        String insertSql = "INSERT INTO AccountInfo " +
                "(Username, Password) " +
                "VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            rollback();
            throw e;
        }
    }
}
