package chessengine.controllers;

import chessengine.StockFishAPI.StockFishAPI;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class DifficultyLevelScreenController extends SceneSwitcher{

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @FXML
    private void easyMode(){
        StockFishAPI.depth = 1;
        insertPane("GamePanel.fxml", baseAnchor, new GamePanelController());
    }

    @FXML
    private void mediumMode(){
        StockFishAPI.depth = 3;
        insertPane("GamePanel.fxml", baseAnchor, new GamePanelController());
    }

    @FXML
    private void hardMode(){
        StockFishAPI.depth = 6;
        insertPane("GamePanel.fxml", baseAnchor, new GamePanelController());
    }

    @FXML
    private void toPlayersScreen() {
        insertPane("PlayersScreen.fxml", baseAnchor, new PlayerScreenController());
    }
}