package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MyReviewSceneController {

    @FXML
    private Button backButton;
    @FXML
    private VBox reviewContainer;


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
    private void addReview(String reviewText) {
        Label reviewLabel = new Label(reviewText);
        reviewContainer.getChildren().add(reviewLabel);
    }


    @FXML
    public void initialize() {
        //fake place holder
        addReview("This is review 1");
        addReview("This is review 2");
        addReview("This is review 3");
    }
}