module chessengine {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.desktop;

    // might be wrong
    opens chessengine.controllers;
    exports chessengine to javafx.fxml, javafx.graphics;
    exports chessengine.controllers to javafx.fxml, javafx.graphics;
    opens chessengine;
}