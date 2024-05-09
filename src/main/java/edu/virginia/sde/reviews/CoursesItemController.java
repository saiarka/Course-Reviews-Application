package edu.virginia.sde.reviews;

import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;


import java.sql.SQLException;

public class CoursesItemController{
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

    public void setCourseItemData(String courseMnemonic, String courseName, int courseNumber, double courseAvgRating){
        this.courseMnemonic = courseMnemonic;
        this.courseName = courseName;
        String courseNumberString = Integer.toString(courseNumber);
        String courseAvgRatingString = Double.toString(courseAvgRating);

        courseButton.setText(courseMnemonic + " " + courseNumberString + ": " + courseName + "\n" + "Rating: " + courseAvgRatingString);

        this.courseNumber = courseNumber;
        this.courseAvgRating = courseAvgRating;
    }


public int getCourseID() throws SQLException {
        int coursenum=courseNumber;
        driver.connect();
        int output= driver.getCourseID(courseMnemonic,courseName, coursenum);
        driver.disconnect();
        return output;
}
    public void handleCourseButtonClick() throws SQLException {
        SceneCreator sceneCreator = new SceneCreator();
        //TODO: Change this code so that each course click will lead to that course's reviews
        ReviewSceneController.setcourseID(getCourseID());
        Scene scene = sceneCreator.createScene("course-review-scene.fxml");
        SceneSwitcher.setScene(scene);
    }


}
