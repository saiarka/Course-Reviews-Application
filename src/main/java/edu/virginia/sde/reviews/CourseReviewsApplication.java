package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CourseReviewsApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private void initializeDatabase() {
        DatabaseDriver databaseDriver = DatabaseDriver.getInstance();
        try {
            databaseDriver.connect();
            databaseDriver.createTablesIfNeeded();
            databaseDriver.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeDatabase() {
        DatabaseDriver databaseDriver = DatabaseDriver.getInstance();
        try {
            databaseDriver.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Initial settings.
        initializeDatabase();
        stage.setTitle("Course Reviews");
        SceneSwitcher.setStageInitially(stage);
        SceneCreator sceneCreator = new SceneCreator();
        Scene loginSignupScene = sceneCreator.createScene("login-signup.fxml");
        SceneSwitcher.setScene(loginSignupScene);
    }

    @Override
    public void stop() throws Exception {
        closeDatabase();
        // TODO: anything else?
    }
}
