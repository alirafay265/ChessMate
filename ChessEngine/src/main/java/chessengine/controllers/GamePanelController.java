package chessengine.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;

import static chessengine.controllers.PlayerScreenController.isSinglePlayer;

// controller class for GamePanel.fxml

public class GamePanelController extends SceneSwitcher {

    public static String customFEN = null;

    @FXML
    private Label loadGameText;

    @FXML
    private Button loadGameButton, loadGameCross;

    @FXML
    private TextField loadGameField;

    @FXML
    private Rectangle loadGameBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadGameBox.setVisible(false);
        loadGameField.setVisible(false);
        loadGameButton.setVisible(false);
        loadGameText.setVisible(false);
        loadGameCross.setVisible(false);
    }

    @FXML
    private void playChessDefault() {
        insertPane("ChessBoard.fxml", baseAnchor, new ChessGameController());
    }

    @FXML
    private void loadGameFromString() {
        loadGameBox.setVisible(true);
        loadGameField.setVisible(true);
        loadGameButton.setVisible(true);
        loadGameText.setVisible(true);
        loadGameCross.setVisible(true);
    }

    @FXML
    private void playChess() {
        customFEN = loadGameField.getText();
        insertPane("ChessBoard.fxml", baseAnchor, new ChessGameController(customFEN));
    }

    @FXML
    private void exitGame() {
        System.exit(0);
    }

    @FXML
    private void toPlayersScreen() {
        if (isSinglePlayer)
            insertPane("DifficultyLevelScreen.fxml", baseAnchor, new DifficultyLevelScreenController());
        else
            insertPane("PlayersScreen.fxml", baseAnchor, new PlayerScreenController());
    }

    @FXML
    private void cross() {
        loadGameBox.setVisible(false);
        loadGameField.setVisible(false);
        loadGameButton.setVisible(false);
        loadGameText.setVisible(false);
        loadGameCross.setVisible(false);
    }
}