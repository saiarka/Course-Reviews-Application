package edu.virginia.sde.reviews;

import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class CoursesItemController{
    @FXML
    private Label courseMnemonic;
    @FXML
    private Label courseName;
    @FXML
    private Label courseNumber;
    @FXML
    private Label courseAvgRating;

    private DatabaseDriver driver= new DatabaseDriver();

    public void setCourseItemData(String courseMnemonic, String courseName, int courseNumber, double courseAvgRating){
        this.courseMnemonic.setText(courseMnemonic);
        this.courseName.setText(courseName);

        String courseNumberString = Integer.toString(courseNumber);
        String courseAvgRatingString = Double.toString(courseAvgRating);

        this.courseNumber.setText(courseNumberString);
        this.courseAvgRating.setText(courseAvgRatingString);
    }


public int getCourseID() throws SQLException {
        int coursenum=Integer.parseInt(courseNumber.getText());
        driver.connect();
        int output= driver.getCourseID(courseMnemonic.getText(),courseName.getText(), coursenum);
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
