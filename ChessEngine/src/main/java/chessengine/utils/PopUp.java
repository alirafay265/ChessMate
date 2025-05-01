package chessengine.utils;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;

public class PopUp {
    private Collection<Node> nodes = new ArrayList<>();
    private boolean closeable = true;
    private String windowTitle;

    public PopUp(String windowTitle, boolean closeable) {
        this.closeable = closeable;
        this.windowTitle = windowTitle;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void display() {
        Stage popupwindow = new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle(windowTitle);

        if (!closeable) {
            popupwindow.setOnCloseRequest(e -> e.consume());
        } else {
            Button close = new Button("Close");
            close.setOnAction(e -> popupwindow.close());
            nodes.add(close);
        }

        VBox layout = new VBox(10);
        layout.getChildren().addAll(nodes);
        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 400, 250);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();
    }

}
