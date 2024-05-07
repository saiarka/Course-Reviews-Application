package edu.virginia.sde.reviews;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class SceneCreator {

    public SceneCreator() {
        // Empty constructor on purpose.
    }

    public Scene createScene(String fxmlFilename) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFilename));
        return new Scene(fxmlLoader.load());
    }
}
