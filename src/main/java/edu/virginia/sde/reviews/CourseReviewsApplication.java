package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class CourseReviewsApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private void setUpDatabase() {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            databaseDriver.createTablesIfNeeded();
            databaseDriver.commit();
            databaseDriver.disconnect();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) {
        // Initial settings.
        setUpDatabase();
        stage.setTitle("Course Reviews");
        SceneSwitcher.setStageInitially(stage);
        SceneCreator sceneCreator = new SceneCreator();
        Scene loginSignupScene = sceneCreator.createScene("course-review-scene.fxml");
        SceneSwitcher.setScene(loginSignupScene);
    }
}
