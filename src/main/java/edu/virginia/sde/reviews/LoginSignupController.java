package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class LoginSignupController {

    @FXML
    private TextField loginUsernameField;
    @FXML
    private TextField loginPasswordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginErrorLabel;
    @FXML
    private TextField signupUsernameField;
    @FXML
    private TextField signupPasswordField;
    @FXML
    private Button signupButton;
    @FXML
    private Label signupErrorLabel;

    // TODO: implement.
    public void handleLoginButton() {
        loginErrorLabel.setText("Not yet implemented...");
    }

    // TODO: implement fully (not complete right now).
    public void handleSignupButton() {
        DatabaseDriver databaseDriver = DatabaseDriver.getInstance();
        try {
            databaseDriver.insertIntoAccountInfo(signupUsernameField.getText(), signupPasswordField.getText());
            databaseDriver.commit();
            signupErrorLabel.setText("Account created. Please log in now.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
