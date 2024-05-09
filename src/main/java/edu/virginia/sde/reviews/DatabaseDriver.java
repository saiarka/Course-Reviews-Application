package edu.virginia.sde.reviews;

import org.hibernate.annotations.processing.SQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String ratingTableSql = "CREATE TABLE IF NOT EXISTS Reviews (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Rating INTEGER NOT NULL, " +
                "TimeStamp TIMESTAMP NOT NULL, " +
                "Comment TEXT, " +
                "UserName TEXT NOT NULL UNIQUE, " +
                "CourseID INTEGER NOT NULL, " + // Added CourseID column
                "FOREIGN KEY (UserName) REFERENCES AccountInfo(Username), " +
                "FOREIGN KEY (CourseID) REFERENCES Courses(ID))"; // Added foreign ke
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
            Course tempCourse = new Course(rs.getInt("CourseNumber"), rs.getString("CourseName"), rs.getString("CourseMnemonic"), rs.getDouble("AverageCourseRating"));
            courseList.add(tempCourse);
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

    public void addReview(int courseId, String userName, Rating rating) throws SQLException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String insertSql = "INSERT INTO Reviews " +
                "(CourseID, UserName, Rating, TimeStamp, Comment) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
            statement.setInt(1, courseId);
            statement.setString(2, userName);
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

    public Rating getUserReview(int courseId, String username) throws SQLException {
        List<Rating> reviewList = new ArrayList<>();
        String retrieveSql = "SELECT * FROM Reviews WHERE CourseID = ? AND UserName = ?";
        try (PreparedStatement statement = connection.prepareStatement(retrieveSql)) {
            statement.setInt(1, courseId);
            statement.setString(2, username);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Rating rating = new Rating(rs.getString("Comment"), rs.getInt("Rating"));
                rating.setTimestamp(rs.getTimestamp("TimeStamp"));
                return rating;
            }
        }
return null;
    }


    public int getUserReviewID(int courseId, String username) throws SQLException {
        String retrieveSql = "SELECT ID FROM Reviews WHERE CourseID = ? AND UserName = ?";
        try (PreparedStatement statement = connection.prepareStatement(retrieveSql)) {
            statement.setInt(1, courseId);
            statement.setString(2, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID");
            }
        }
        return -1; // Return -1 if no review ID found for the given parameters
    }

    public List<Course> getSearchedCourseList(String courseMnemonic, String courseTitle, String courseNumber) throws SQLException {
        List<Course> returnedCourseList = new ArrayList<>();
        List<String> addedConditions = new ArrayList<>();
        String initalQuery = "SELECT * FROM Courses WHERE ";

        if (!courseMnemonic.isEmpty()) {
            addedConditions.add("CourseMnemonic = ?");
        }

        if (!courseTitle.isEmpty()) {
            addedConditions.add("CourseName LIKE ?");
        }

        if (!courseNumber.isEmpty()) {
            addedConditions.add("CourseNumber = ?");
        }

        if (!addedConditions.isEmpty()) {
            initalQuery += String.join(" AND ", addedConditions);
        } else {
            initalQuery = "SELECT * FROM Courses";
        }

        try (PreparedStatement statement = connection.prepareStatement(initalQuery)) {

            int parameterIndex = 1;
            if (!courseMnemonic.isEmpty()) {
                //Not sure if uppercase is right here
                statement.setString(parameterIndex++, courseMnemonic.toUpperCase());
            }

            if (!courseNumber.isEmpty()) {
                statement.setString(parameterIndex++, courseNumber);
            }

            if (!courseTitle.isEmpty()) {
                statement.setString(parameterIndex, "%" + courseTitle + "%");
            }

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                returnedCourseList.add(new Course(rs.getInt("CourseNumber"), rs.getString("CourseName"), rs.getString("CourseMnemonic"), rs.getDouble("AverageCourseRating")));
            }
        }
        return returnedCourseList;
    }

    public List<Rating> getAllUserReviews( String username) throws SQLException {
        List<Rating> reviewList = new ArrayList<>();
        String retrieveSql = "SELECT * FROM Reviews WHERE UserName = ?";
        try (PreparedStatement statement = connection.prepareStatement(retrieveSql)) {

            statement.setString(2, username);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Rating rating = new Rating(rs.getString("Comment"), rs.getInt("Rating"));
                rating.setTimestamp(rs.getTimestamp("TimeStamp"));
                reviewList.add(rating);

            }
        }
        return reviewList;
    }

    public Map<String, Rating> getUserReviewsByCourse(String username) throws SQLException {
        Map<String, Rating> userReviewsMap = new HashMap<>();
        String retrieveSql = "SELECT r.Comment, r.Rating, c.CourseMnemonic, c.CourseNumber " +
                "FROM Reviews r " +
                "JOIN Courses c ON r.CourseID = c.ID " +
                "WHERE r.UserName = ?";
        try (PreparedStatement statement = connection.prepareStatement(retrieveSql)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String courseKey = rs.getString("CourseMnemonic") + " " + rs.getInt("CourseNumber");
                Rating rating = new Rating(rs.getString("Comment"), rs.getInt("Rating"));
                userReviewsMap.put(courseKey, rating);
            }
        }
        return userReviewsMap;
    }


    public void addCourse(String mnemonic, String title, String number)  throws SQLException{
        String insertSql = "INSERT INTO Courses " +
                "(mnemonic, title, number)" +
                "VALUES(?, ?, ?)";

        try(PreparedStatement statement = connection.prepareStatement(insertSql)) {
            statement.setString(1, mnemonic);
            statement.setString(2, title);
            statement.setString(3, number);
            statement.executeUpdate();
            commit();
        }catch(SQLException e) {
            rollback();
            throw e;
        }
    }

}
