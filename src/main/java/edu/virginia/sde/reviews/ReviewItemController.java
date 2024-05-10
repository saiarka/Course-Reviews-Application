package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.List;

public class ReviewItemController {
    @FXML
    private Button reviewButton;

    private String courseKey;
    String courseMnemonic;
    String courseTitle;
    @FXML
    String courseName;
    @FXML
    private int courseNumber;
    @FXML
    private double courseAvgRating;
    @FXML
    private Button courseButton;

    @FXML
    private int rating;

    private DatabaseDriver driver= new DatabaseDriver();

    public void setReviewItemData(String courseMnemonic, String courseID, String Reviewtext, int rating, String courseTitle) {
            this.courseMnemonic=courseMnemonic;
            this.courseTitle=courseTitle;
            this.courseNumber= Integer.parseInt(courseID);
        //this.courseKey = courseKey;
        reviewButton.setText(courseMnemonic + " " + courseID +  " "+ courseTitle +" " + "Review: "+Reviewtext+ " Rating: " + rating);
    }
    public int getCourseID() throws SQLException {
        int coursenum=courseNumber;
        driver.connect();
        int output= driver.getCourseID(courseMnemonic,courseTitle, coursenum);
        driver.disconnect();
        return output;
    }
    @FXML
    private void handleReviewButtonClick() {
        try {

            int courseId = getCourseID();

            ReviewSceneController.setcourseID(courseId);

            SceneCreator sceneCreator = new SceneCreator();
            Scene scene = sceneCreator.createScene("course-review-scene.fxml");

            // Switch to the course review scene
            SceneSwitcher.setScene(scene);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String calculateAverageRating() throws SQLException {
        int courseidnum=getCourseID();
        driver.connect();
        List<Double> ratingList= driver.getAllNumberRatingsForCourse(courseidnum);

        driver.disconnect();
        if (ratingList == null || ratingList.isEmpty()) {
            return "";
        }

        double sum = 0.0;
        for (Double num : ratingList) {
            sum += num;
        }

        double average = sum / ratingList.size();
        return String.format("%.2f", average); // Format the average rating to two decimal places
    }


}
