package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MyReviewSceneController {

    @FXML
    private Button backButton; // Assuming your back button has an fx:id of "backButton"

    // Method to handle the "go back" action
    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("course-search-scene.fxml"));
            Scene nextScene = new Scene(loader.load());

            // Get the stage from any node in the scene (here, we use the back button)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(nextScene);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}