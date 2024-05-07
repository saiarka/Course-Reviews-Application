package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class LoginSignupController {

    // Service attribute.
    LoginSignupService service = new LoginSignupService();
    // FXML elements.
    @FXML
    private TextField loginUsernameField;
    @FXML
    private TextField loginPasswordField;
    @FXML
    private Label loginErrorLabel;
    @FXML
    private TextField signupUsernameField;
    @FXML
    private TextField signupPasswordField;
    @FXML
    private Label signupErrorLabel;

    public void handleLoginButton() {
        String username = loginUsernameField.getText();
        if (!service.isExistingUsername(username)) {
            loginErrorLabel.setText("Username does not exist.");
        }
        else {
            String password = loginPasswordField.getText();
            if (service.isCorrectPassword(username, password)) {
                SceneCreator sceneCreator = new SceneCreator();
                Scene nextScene = sceneCreator.createScene("course-search-scene.fxml");
                SceneSwitcher.setScene(nextScene);
            }
            else {
                loginErrorLabel.setText("Incorrect password for username.");
            }
        }
    }

    public void handleSignupButton() {
        String username = signupUsernameField.getText();
        String password = signupPasswordField.getText();
        if (service.isExistingUsername(username)) {
            signupErrorLabel.setText("Username already taken.");
        }
        else if (!service.isValidPassword(password)) {
            signupErrorLabel.setText("Password must be at least 8 characters.");
        }
        else {
            service.addAccount(username, password);
            signupErrorLabel.setText("Account created. You can log in now.");
        }
    }
}
