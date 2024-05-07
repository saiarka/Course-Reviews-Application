package edu.virginia.sde.reviews;

import javafx.fxml.FXML;

import javafx.scene.control.Label;

public class CoursesItemController{
    @FXML
    private Label courseMnemonic;
    @FXML
    private Label courseName;
    @FXML
    private Label courseNumber;
    @FXML
    private Label courseAvgRating;

    public void setCourseItemData(String courseMnemonic, String courseName, int courseNumber, double courseAvgRating){
        this.courseMnemonic.setText(courseMnemonic);
        this.courseName.setText(courseName);

        String courseNumberString = Integer.toString(courseNumber);
        String courseAvgRatingString = Double.toString(courseAvgRating);

        this.courseNumber.setText(courseNumberString);
        this.courseAvgRating.setText(courseAvgRatingString);
    }

}
