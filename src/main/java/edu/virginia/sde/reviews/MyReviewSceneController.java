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
    private void addReview(String courseKey, String reviewText) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("review-item.fxml"));
            VBox reviewItem = loader.load();
            ReviewItemController controller = loader.getController();
            controller.setReviewItemData(courseKey, reviewText);
            reviewContainer.getChildren().add(reviewItem);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                addReview(courseKey, reviewText);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

}