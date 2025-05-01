package chessengine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader titleLoader = new FXMLLoader(App.class.getResource("TitleScreen.fxml"));
        Scene titleScene = new Scene(titleLoader.load(), 606, 491);
        stage.setScene(titleScene);
        stage.setTitle("Chess Engine");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch();
    }
}