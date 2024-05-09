package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class ReviewItemController {
    @FXML
    private Button reviewButton;

    private String courseKey;
    String courseMnemonic;
    @FXML
    String courseName;
    @FXML
    private int courseNumber;
    @FXML
    private double courseAvgRating;
    @FXML
    private Button courseButton;

    private DatabaseDriver driver= new DatabaseDriver();

    public void setReviewItemData(String courseKey, String reviewText) {
        this.courseKey = courseKey;
        reviewButton.setText(courseKey + ": " + reviewText);
    }
    public int getCourseID() throws SQLException {
        int coursenum=courseNumber;
        driver.connect();
        int output= driver.getCourseID(courseMnemonic,courseName, coursenum);
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


}
