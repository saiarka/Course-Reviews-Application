package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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

    private Scene createScene(String fxmlFilename) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFilename));
        return new Scene(fxmlLoader.load());
    }

    private void cleanUpDatabase() {
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
        stage.show();
        // Login and sign up scene.
        Scene loginSignupScene = createScene("login-signup.fxml");
        stage.setScene(loginSignupScene);
        // TODO: other scenes.
    }

    @Override
    public void stop() throws Exception {
        cleanUpDatabase();
        // TODO: anything else?
    }
}
