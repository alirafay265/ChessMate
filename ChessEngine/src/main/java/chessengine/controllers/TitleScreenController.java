package chessengine.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class TitleScreenController extends SceneSwitcher{
    @FXML
    private Button loginButton, registerButton, playAsAGuestButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @FXML
    public void loginScreen() {
        insertPane("LoginScreen.fxml", baseAnchor, new LoginScreenController());
    }

    @FXML
    public void registerScreen() {
        insertPane("RegistrationScreen.fxml", baseAnchor, new RegistrationScreenController());
    }

    @FXML
    private void exit() {
        System.exit(0);
    }
}