package chessengine.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UpgradePawnController implements Initializable {
    @FXML
    private ImageView queen, rook, bishop, knight;

    private static String upgradeChoice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        queen.setOnMouseClicked(e -> choose("queen"));
        rook.setOnMouseClicked(e -> choose("rook"));
        bishop.setOnMouseClicked(e -> choose("bishop"));
        knight.setOnMouseClicked(e -> choose("knight"));
    }

    private void choose(String pieceType) {
        upgradeChoice = pieceType;

        Stage stage = (Stage) queen.getScene().getWindow();
        stage.close();
    }

    public static String getUpgradeChoice() {
        return upgradeChoice;
    }
}

