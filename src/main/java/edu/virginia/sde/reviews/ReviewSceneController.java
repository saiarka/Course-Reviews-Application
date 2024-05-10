package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.util.List;

public class ReviewSceneController {

    @FXML
    private Label userCommentLabel;
    @FXML
    private Label userRatingLabel;
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

    @FXML
    private TextField ratingTextField;

    @FXML
    private Label errorLabel;

    private DatabaseDriver databaseDriver= new DatabaseDriver();

    private static int courseId;

public static void setcourseID(int ID){
    courseId=ID;

}

    private void loadCourseDetails() {
        try {
            databaseDriver.connect();
            Course course = databaseDriver.getCourseDetails(courseId);

            databaseDriver.disconnect();
            if(course!=null) {
                mnemonicLabel.setText(course.getCoursemnemonic());
                numberLabel.setText(String.valueOf(course.getCoursenumber()));
                titleLabel.setText(course.getCoursename());
                if (course.getAvgRating() > 0.00) {
                    averageRatingLabel.setText(String.format("%.2f", course.getAvgRating()));
                } else {
                    averageRatingLabel.setText(""); // Leave it blank if no reviews available
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }
    }

    private void loadReviews() {
        try {
            databaseDriver.connect();
            List<Rating> ratings = databaseDriver.retrieveAllRatingsForCourse(courseId);
            databaseDriver.disconnect();
            ObservableList<Rating> observableRatings = FXCollections.observableArrayList(ratings);
            reviewsListView.setItems(observableRatings);
            loadUserReview();
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
      //  ObservableList<Integer> ratings = FXCollections.observableArrayList(1, 2, 3, 4, 5);

      //  ratingChoiceBox.setItems(ratings);

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

    private void loadUserReview() {
        try {
            UserSession userSession = UserSession.getInstance();
            String username=userSession.getUsername();
            databaseDriver.connect();
            Rating userReview = databaseDriver.getUserReview(courseId, username);
            databaseDriver.disconnect();
            if (userReview != null) {
                // User has already submitted a review
                userRatingLabel.setText(String.valueOf(userReview.getRatingNumber()));
                userCommentLabel.setText(userReview.getCommentText());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }
    }
    public void editReview(ActionEvent actionEvent) {
        try {
            UserSession userSession = UserSession.getInstance();
            String username = userSession.getUsername();
            String ratingText = ratingTextField.getText();

            int rating= Integer.parseInt(ratingText);
            Rating updatedrate= new Rating(commentTextArea.getText(),rating );
            databaseDriver.connect();

            int userreviewID= databaseDriver.getUserReviewID(courseId, username);
            if(userreviewID!=-1) {
                databaseDriver.updateReview(userreviewID,updatedrate );
                databaseDriver.commit();
            }else{
                showError("You have not left a review to edit!");
            }
            databaseDriver.disconnect();

            // Clear the user review labels or reset them to default values
            userRatingLabel.setText("");
            userCommentLabel.setText("");
            // Reload reviews after deletion
            loadReviews();

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }
    }

    public void deleteReview(ActionEvent actionEvent) {
        try {
            UserSession userSession = UserSession.getInstance();
            String username = userSession.getUsername();
            databaseDriver.connect();

            int userreviewID= databaseDriver.getUserReviewID(courseId, username);
            if(userreviewID!=-1) {
                databaseDriver.deleteReview(userreviewID);
                databaseDriver.commit();
            }else{
                showError("You have not left a review to delete!");
            }
            databaseDriver.disconnect();

                userRatingLabel.setText("");
                userCommentLabel.setText("");
                loadReviews();

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }
    }

    public void submitReview(ActionEvent actionEvent) {

        try {
            String comment = commentTextArea.getText();
            String ratingtext= ratingTextField.getText();
            if(ratingtext==""){
                showError("The review rating is empty");
               return;
            }
            try {
                int rating = Integer.parseInt(ratingtext);
                if (rating < 1 || rating > 5) {
                    showError("Rating must be an integer between 1 and 5");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Rating must be an integer between 1 and 5");
                return;
            }

            int rating = Integer.parseInt(ratingtext);
            try {

                if (rating < 1 || rating > 5) {
                    showError("Rating must be an integer between 1 and 5");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Rating must be an integer between 1 and 5");
                return;
            }

            String user=UserSession.getInstance().getUsername();
            Rating newRating = new Rating( comment, rating);
            databaseDriver.connect();
            int alreadyreviewed= databaseDriver.getUserReviewID(courseId, user);
            if(alreadyreviewed==-1) {
                databaseDriver.addReview(courseId, user, newRating);
                databaseDriver.commit();
                loadReviews();
            }else{
                showError("You have already submitted a review for this course!");
            }
            databaseDriver.disconnect();


            loadReviews();
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();

        }
    }
    public void goBack(ActionEvent actionEvent) {
        try {
            SceneCreator sceneCreator = new SceneCreator();


            Scene back = sceneCreator.createScene("course-search-scene.fxml");
            SceneSwitcher.setScene(back);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showError(String errorMessage) {
        errorLabel.setText(errorMessage);

    }


}
