package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.util.List;

public class ReviewSceneController {


    @FXML
    private Label mnemonicLabel;
    @FXML
    private Label numberLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label averageRatingLabel;
    @FXML
    private ListView<Rating> reviewsListView;
    @FXML
    private ChoiceBox<Integer> ratingChoiceBox;
    @FXML
    private TextArea commentTextArea;

    private DatabaseDriver databaseDriver= new DatabaseDriver();

    private int courseId;

public void setcourseID(int ID){
    courseId=ID;

}

    private void loadCourseDetails() {
        try {
            Course course = databaseDriver.getCourseDetails(courseId);
            mnemonicLabel.setText(course.getCoursemnemonic());
            numberLabel.setText(String.valueOf(course.getCoursenumber()));
            titleLabel.setText(course.getCoursename());
            averageRatingLabel.setText(String.format("%.2f", course.getAvgRating()));
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }
    }

    private void loadReviews() {
        try {
            List<Rating> ratings = databaseDriver.retrieveAllRatingsForCourse(courseId);
            ObservableList<Rating> observableRatings = FXCollections.observableArrayList(ratings);
            reviewsListView.setItems(observableRatings);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }
    }
    @FXML
    public void initialize() {
        // Load course details and reviews when the scene is initialized
        loadCourseDetails();
        loadReviews();
        // Populate the choice box with rating options
        ObservableList<Integer> ratings = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        ratingChoiceBox.setItems(ratings);

        reviewsListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Rating rating, boolean empty) {
                super.updateItem(rating, empty);
                if (empty || rating == null) {
                    setText(null);
                } else {
                    setText(String.format("Rating: %d, Timestamp: %s, Comment: %s",
                            rating.getRatingNumber(), rating.getTimestamp(), rating.getCommentText()));
                }
            }
        });
    }


    public void editReview(ActionEvent actionEvent) {

    }

    public void deleteReview(ActionEvent actionEvent) {
    }

    public void submitReview(ActionEvent actionEvent) {
    }

    public void goBack(ActionEvent actionEvent) {
    }
}
