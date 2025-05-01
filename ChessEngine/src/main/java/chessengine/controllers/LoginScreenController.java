package chessengine.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LoginScreenController extends SceneSwitcher{

    String username, password;

    @FXML
    private Label wrongPasswordText, emptyFieldsText;

    @FXML
    private TextField loginUsername, loginPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wrongPasswordText.setVisible(false);
        emptyFieldsText.setVisible(false);
    }

    @FXML
    private void login() throws IOException {
        wrongPasswordText.setVisible(false);
        emptyFieldsText.setVisible(false);
        username = loginUsername.getText();
        password = loginPassword.getText();
        if (username.isEmpty() || password.isEmpty()) {
            emptyFieldsText.setVisible(true);
            return;
        }
        File file = new File("src/main/userdata.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            if (scanner.nextLine().equals(username + " " + password)) {
                scanner.close();
                insertPane("PlayersScreen.fxml", baseAnchor, new PlayerScreenController());
                return;
            }
        }
        wrongPasswordText.setVisible(true);
        loginPassword.clear();
        scanner.close();
    }

    @FXML
    private void registerScreen() {
        insertPane("RegistrationScreen.fxml", baseAnchor, new RegistrationScreenController());
    }

    @FXML
    private void toTitleScreen() {
        insertPane("TitleScreen.fxml", baseAnchor, new TitleScreenController());
    }
}
