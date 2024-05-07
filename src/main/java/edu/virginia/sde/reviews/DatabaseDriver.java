package edu.virginia.sde.reviews;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        Statement statement = connection.createStatement();
        // Create AccountInfo table.
        String accountInfoSql = "CREATE TABLE IF NOT EXISTS AccountInfo (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Username TEXT NOT NULL UNIQUE, " +
                "Password TEXT NOT NULL)";
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

    public boolean isExistingUsername(String username) throws SQLException {
        String usernameSql = "SELECT COUNT(*) FROM AccountInfo WHERE Username = ?";
        try (PreparedStatement statement = connection.prepareStatement(usernameSql)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getInt(1) != 0;
        }
    }

    public String getPasswordFromUsername(String username) throws SQLException {
        String usernameSql = "SELECT Password FROM AccountInfo WHERE Username = ?";
        try (PreparedStatement statement = connection.prepareStatement(usernameSql)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getString("Password");
        }
    }

    public void addAccount(String username, String password) throws SQLException {
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

    //Method for retrieving list of courses from database
    public List<Course> retrieveAllCoursesFromDatabase() throws SQLException {
        List<Course> courseList = new ArrayList<>();
        String retrieveSql = "SELECT * FROM Courses ORDER BY CourseMnemonic ASC";
        Statement retrievalStatement = connection.createStatement();
        ResultSet rs = retrievalStatement.executeQuery(retrieveSql);

        while(rs.next()) {
            //TODO: Implement this retrieval set so that it creates a list of courses that can be returned
        }

        return courseList;
    }

    public Course getCourseDetails(int courseId) throws SQLException {
        String retrieveSql = "SELECT * FROM Courses WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(retrieveSql)) {
            statement.setInt(1, courseId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int courseNumber = rs.getInt("CourseNumber");
                String courseName = rs.getString("CourseName");
                String courseMnemonic = rs.getString("CourseMnemonic");
                double avgRating = rs.getDouble("AverageCourseRating");

                // Assuming you have a method to retrieve ratings for this course ID
                List<Rating> ratingList = retrieveAllRatingsForCourse(courseId);

                return new Course(courseNumber, courseName, courseMnemonic, avgRating, ratingList);
            }
        }
        return null; // Course not found
    }


    public List<Rating> retrieveAllRatingsForCourse(int courseId) throws SQLException {
        List<Rating> ratingList = new ArrayList<>();
        String retrieveSql = "SELECT * FROM Reviews WHERE CourseID = ?";
        try (PreparedStatement statement = connection.prepareStatement(retrieveSql)) {
            statement.setInt(1, courseId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Rating rating = new Rating(rs.getString("Comment"), rs.getInt("Rating"));
                rating.setTimestamp(rs.getTimestamp("TimeStamp"));
                ratingList.add(rating);
            }
        }
        return ratingList;
    }

    public void addReview(int courseId, int userId, Rating rating) throws SQLException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String insertSql = "INSERT INTO Reviews " +
                "(CourseID, UserID, Rating, TimeStamp, Comment) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
            statement.setInt(1, courseId);
            statement.setInt(2, userId);
            statement.setInt(3, rating.getRatingNumber());
            statement.setTimestamp(4, timestamp);
            statement.setString(5, rating.getCommentText());
            statement.executeUpdate();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }


    public void deleteReview(int reviewId) throws SQLException {
        String deleteSql = "DELETE FROM Reviews WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteSql)) {
            statement.setInt(1, reviewId);
            statement.executeUpdate();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

    public void updateReview(int reviewId, Rating updatedRating) throws SQLException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String updateSql = "UPDATE Reviews SET Rating = ?, TimeStamp = ?, Comment = ? WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateSql)) {
            statement.setInt(1, updatedRating.getRatingNumber());
            statement.setTimestamp(2, timestamp);
            statement.setString(3, updatedRating.getCommentText());
            statement.setInt(4, reviewId);
            statement.executeUpdate();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

}
