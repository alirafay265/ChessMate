package chessengine.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class RegistrationScreenController extends SceneSwitcher{
    String username, password;

    @FXML
    Label usernameTakenText;
    @FXML
    TextField registerUsername, registerPassword;
    @FXML
    Label emptyFieldsText;

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameTakenText.setVisible(false);
        emptyFieldsText.setVisible(false);
    }

    @FXML
    private void register() throws IOException {
        usernameTakenText.setVisible(false);
        emptyFieldsText.setVisible(false);

        username = registerUsername.getText();
        password = registerPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            emptyFieldsText.setVisible(true);
            return;
        }

        File myFile = new File("src/main/userdata.txt");
        if (!myFile.exists()) {
            myFile.createNewFile();
        }

        try (Scanner scanner = new Scanner(myFile)) {
            while (scanner.hasNext()) {
                if (scanner.next().equals(username)) {
                    usernameTakenText.setVisible(true);
                    registerUsername.clear();
                    registerPassword.clear();
                    return;
                }
            }
        }

        try (FileWriter writer = new FileWriter(myFile, true)) {
            writer.write(username + " " + password + "\n");
        }

        insertPane("LoginScreen.fxml", baseAnchor, new LoginScreenController());
    }

    @FXML
    private void loginScreen() {
        insertPane("LoginScreen.fxml", baseAnchor, new LoginScreenController());
    }

    @FXML
    private void toTitleScreen() {
        insertPane("TitleScreen.fxml", baseAnchor, new TitleScreenController());
    }
}
