package edu.virginia.sde.reviews;

import javafx.scene.Scene;
import javafx.stage.Stage;

// Static scene switcher to switch with the same stage.
public class SceneSwitcher {

    private static Stage stage;

    // Used just once to set the stage.
    public static void setStageInitially(Stage staticStage) {
        stage = staticStage;
    }

    public static void setScene(Scene scene) {
        stage.setScene(scene);
        stage.show();
    }
}
