package chessengine.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;


public class PlayerScreenController extends SceneSwitcher{

    public static boolean isSinglePlayer = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @FXML
    public void toGamePanel() {
        isSinglePlayer = false;
        insertPane("GamePanel.fxml", baseAnchor, new GamePanelController());
    }

    @FXML
    public void toDifficultyLevelScreen() {
        isSinglePlayer = true;
        insertPane("DifficultyLevelScreen.fxml", baseAnchor, new DifficultyLevelScreenController());

    }

    @FXML
    public void toTitleScreen() {
        insertPane("TitleScreen.fxml", baseAnchor, new TitleScreenController());
    }

    @FXML
    public void exit(){
        System.exit(0);
    }

}
