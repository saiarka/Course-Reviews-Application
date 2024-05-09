package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class MyReviewSceneController {

    @FXML
    private Button backButton;
    @FXML
    private VBox reviewContainer;

    DatabaseDriver databaseDriver =  new DatabaseDriver();

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("course-search-scene.fxml"));
            Scene nextScene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(nextScene);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }












    @FXML
    private void addReview(String courseKey, String reviewText, int reviewVal) {

        String[] keyParts = courseKey.split(" ");
        String mnemonic = keyParts[0];
        String courseId = keyParts[1];
        String title = keyParts[2];


        Label courseLabel = new Label(mnemonic + " " + courseId + " " + title);
        Label ratingLabel = new Label("Rating: " + reviewVal);
        Label summaryLabel = new Label("Summary: " + reviewText);


        //makes it easier to read and separate many reviews hopefully
        Separator separator = new Separator();


        reviewContainer.getChildren().addAll(courseLabel, ratingLabel, summaryLabel, separator);


        courseLabel.setStyle("-fx-font-weight: bold;");
    }

    @FXML
    public void initialize() {

        UserSession userSession = UserSession.getInstance();
        String username = userSession.getUsername();

        try {
databaseDriver.connect();
            Map<String, Rating> userReviewsMap = databaseDriver.getUserReviewsByCourse(username);
            databaseDriver.disconnect();

            for (Map.Entry<String, Rating> entry : userReviewsMap.entrySet()) {
                String courseKey = entry.getKey();
                String reviewText = entry.getValue().getCommentText();
                int reviewval=entry.getValue().getRatingNumber();
                addReview(courseKey, reviewText, reviewval);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

}