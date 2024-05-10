package edu.virginia.sde.reviews;

import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;


import java.sql.SQLException;
import java.util.List;

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

    public void setCourseItemData(String courseMnemonic, String courseName, int courseNumber, double avgRating) throws SQLException {
        this.courseMnemonic = courseMnemonic;
        this.courseName = courseName;
        this.courseNumber = courseNumber;
        String courseNumberString = Integer.toString(courseNumber);
        String avgRatingString= calculateAverageRating();
        try {
            double avgRatingDouble = Double.parseDouble(avgRatingString);
            this.courseAvgRating = avgRatingDouble;
        } catch (NumberFormatException e) {
            // Handle parsing exception (e.g., set avgRatingDouble to a default value)
            avgRatingString = "";
        }




        courseButton.setText(courseMnemonic + " " + courseNumberString + ": " + courseName + "\n" + "Rating: " + avgRatingString);



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
