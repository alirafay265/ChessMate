package chessengine.controllers;

import chessengine.StockFishAPI.StockFishAPI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import chessengine.ChessBoard;
import chessengine.Position;
import chessengine.pieces.*;
import chessengine.utils.FENParser;
import chessengine.utils.IllegalFENException;
import chessengine.utils.PopUp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import static chessengine.controllers.GamePanelController.customFEN;

public class ChessGameController extends SceneSwitcher {

    boolean isFirstTime = true;

    @FXML
    private TextArea moves;

    @FXML
    private GridPane grid, backgroundBoard;

    private ChessBoard chessboard;
    private String FENString = "";
    private boolean hasShownGameOver = false;
    private ArrayList<ArrayList<Color>> gridBackgroundColor;
    private static Image allPiecesImg;
    private final static Map<Character, Integer> imgColIdx = Map.of('k', 0, 'q', 1, 'b', 2, 'n', 3, 'r', 4, 'p', 5);

    public ChessGameController() {
        this.FENString = FENParser.DEFAULT_STRING;
    }

    public ChessGameController(String FENString) {
        this.FENString = FENString;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        resetGridBackgroundMatrix();
        handleRestartGame();
    }

    @FXML
    private void toGamePanel() {
        insertPane("GamePanel.fxml", baseAnchor, new GamePanelController());
    }

    @FXML
    private void handleRestartGame() {
        try {
            if (!Objects.equals(customFEN, null)){
                FENString = customFEN;
                customFEN = null;
            }
            else
                FENString = FENParser.DEFAULT_STRING;

            chessboard = FENParser.getBoardFromFEN(FENString);
            boolean playerIsWhite = getCurrentPlayerFullName(FENString);
            chessboard.getPlayerTurn().setWhite(playerIsWhite);
            if (!playerIsWhite) {
                new Thread(() -> {
                    try {
                        Thread.sleep(300);
                        String bestAIMove = getAIMove();
                        applyAIMove(bestAIMove);

                        javafx.application.Platform.runLater(() -> {
                            drawBoard();
                            showGameOver();
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (NullPointerException | IllegalFENException e) {
            chessboard = FENParser.getBoardFromDefaultFEN();
            PopUp popup = new PopUp("Error loading FEN", true);
            popup.addNode(new Text("Could not read the FEN String\nUsing default FEN String"));
            popup.display();
        }

        hasShownGameOver = false;
        allPiecesImg = new Image(ChessBoard.class.getResource("images/pieces.png").toString());
        drawBoard();
    }


    @FXML
    private void handleGridPaneClicked(MouseEvent event) {
        positionPressed(new Position(event));
        drawBoard();
        showGameOver();
    }

    @FXML
    private void drawBoard() {
        placePieces();
        regenerateBackgroundBoard();
        colorBackgrounds();
        updateMoves();
    }

    @FXML
    private void toTitleScreen() {
        insertPane("TitleScreen.fxml", baseAnchor, new TitleScreenController());
    }

    @FXML
    private void exportGame() throws IOException {
        String position = chessboard.getFEN();
        PopUp popUp = new PopUp("Export game", true);

        TextField textField = new TextField(position);
        textField.setEditable(false);
        popUp.addNode(textField);

        Label label = new Label("Name of the FEN String:");
        popUp.addNode(label);
        TextField myFile = new TextField();
        textField.setEditable(true);
        popUp.addNode(myFile);

        Button toFileButton = new Button("Export to .fen File");

        Label emptyname = new Label("Name can not be empty!");
        emptyname.setVisible(false);
        Label fileExists = new Label("A file with this name already exists. Try a different name!");
        fileExists.setVisible(false);
        Label fileSaved = new Label("File has been saved");
        fileSaved.setVisible(false);

        toFileButton.setOnAction(e -> {
            String fileName = myFile.getText();
            if (Objects.equals(fileName, "")) {
                fileExists.setVisible(false);
                fileSaved.setVisible(false);
                emptyname.setVisible(true);
            }
            else {
                emptyname.setVisible(false);
                File file = new File("src/main/playedGames/" + fileName + ".fen");
                if (file.exists()) {
                    fileSaved.setVisible(false);
                    fileExists.setVisible(true);
                    myFile.clear();
                }
                else {
                    try {
                        fileExists.setVisible(false);
                        FENParser.saveToFile(position, file);
                        fileSaved.setVisible(true);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        popUp.addNode(toFileButton);
        popUp.addNode(emptyname);
        popUp.addNode(fileExists);
        popUp.addNode(fileSaved);

        popUp.display();
    }

    public static boolean getCurrentPlayerFullName(String fen) {
        String[] fields = fen.split(" ");
        if (fields[1].equals("w")) {
            return true;
        } else if (fields[1].equals("b")) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid FEN string: Unknown player identifier.");
        }
    }

    private void positionPressed(Position position) {
        if (chessboard.getGameFinished()) {
            return;
        }

        resetGridBackgroundMatrix();
        if (getSelectedPiece() == null) {
            chessboard.setSelectedPiece(chessboard.getPosition(position));

            Piece selected = getSelectedPiece();
            if (selected != null && selected.getOwner() != chessboard.getPlayerTurn()) {
                chessboard.setSelectedPiece(null);
                return;
            }

            if (selected != null) {
                setGridBackgroundColor(position.getX(), position.getY(), Color.YELLOW);
                setLegalMovesBackground(getSelectedPiece());
            }
            return;
        }

        if (chessboard.getPlayerTurn() != getSelectedPiece().getOwner()) {
            return;
        }

        try {
            getSelectedPiece().move(position);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid move");
            chessboard.setSelectedPiece(null);
            return;
        }

        chessboard.setSelectedPiece(null);
        checkPawnPromotion();
        drawBoard();
        showGameOver();

        if (PlayerScreenController.isSinglePlayer){
            if (!chessboard.getGameFinished() && !chessboard.getPlayerTurn().isWhite()) {
                new Thread(() -> {
                    try {
                        Thread.sleep(300);

                        String bestAIMove = getAIMove();
                        applyAIMove(bestAIMove);

                        javafx.application.Platform.runLater(() -> {
                            drawBoard();
                            showGameOver();
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    private void checkPawnPromotion() {
        Pawn pawn = chessboard.getUpgradablePawn();
        if (pawn != null) {
            promotePawn(pawn);
        }
    }

    private void promotePawn(Pawn piece) {
        Piece newPiece;
        try {
            final URL url = getClass().getResource("/chessengine/UpgradePawnScreen.fxml");
            final FXMLLoader loader = new FXMLLoader(url);

            final Node node = loader.load();
            final PopUp popUp = new PopUp("Promote pawn", false);
            popUp.addNode(node);
            popUp.display();
            switch (UpgradePawnController.getUpgradeChoice()) {
                case "rook":
                    newPiece = new Rook(piece.getPos(), chessboard, piece.getOwner());
                    break;
                case "bishop":
                    newPiece = new Bishop(piece.getPos(), chessboard, piece.getOwner());
                    break;
                case "knight":
                    newPiece = new Knight(piece.getPos(), chessboard, piece.getOwner());
                    break;
                case "queen":
                default:
                    newPiece = new Queen(piece.getPos(), chessboard, piece.getOwner());
                    break;
            }

        } catch (IOException e) {
            System.out.println("Could not load fxml file, using queen as default promotion");
            newPiece = new Queen(piece.getPos(), chessboard, piece.getOwner());
        }

        chessboard.promotePawn(piece, newPiece);
    }

    private ImageView cropPieceImage(Character pieceType, boolean white) {
        ImageView img = new ImageView(getAllPiecesImg());

        int imgY = (white ? 0 : 1);
        int coloumnIndex = getPieceImageIndex(pieceType);
        final int size = 50;

        img.setViewport(new Rectangle2D(coloumnIndex * size, imgY * size, size, size));

        img.setFitWidth(48);
        img.setPreserveRatio(true);
        return img;
    }

    private Piece getSelectedPiece() {
        return chessboard.getSelectedPiece();
    }

    private void resetGridBackgroundMatrix() {
        gridBackgroundColor = new ArrayList<ArrayList<Color>>();
        for (int i = 0; i < 8; i++) {
            gridBackgroundColor.add(new ArrayList<Color>());
            for (int j = 0; j < 8; j++) {
                gridBackgroundColor.get(i).add((i + j) % 2 == 0 ? Color.rgb(113, 93, 76) : Color.WHITE);
            }
        }
    }

    private void showGameOver() {
        if (hasShownGameOver)
            return;

        if (chessboard.getGameFinished()) {
            System.out.println(chessboard.getGameMessage());
            PopUp popUp = new PopUp("Game Over", true);
            popUp.addNode(new Text(chessboard.getGameMessage()));
            popUp.display();
            hasShownGameOver = true;
        }
    }

    private void placePieces() {
        Node gridLines = grid.getChildren().get(0);
        grid.getChildren().clear();
        grid.getChildren().add(0, gridLines);
        for (Piece piece : chessboard) {
            grid.add(getImage(piece), piece.getX(), 7 - piece.getY());
        }
    }

    private ImageView getImage(Piece piece) {
        final char pieceChar = Character.toLowerCase(piece.toChar());
        return cropPieceImage(pieceChar, piece.isWhite());
    }

    private void regenerateBackgroundBoard() {
        if (backgroundBoard.getChildren().size() != 64) {
            backgroundBoard.getChildren().clear();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    backgroundBoard.add(new Rectangle(50, 50), j, i);
                }
            }
        }
    }

    private void colorBackgrounds() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ((Rectangle) backgroundBoard.getChildren().get((7 - i) * 8 + j))
                        .setFill(getGridBackgroundColor(j, i));
            }
        }
    }

    private void updateMoves() {
        moves.setText("    WHITE | BLACK\n" + chessboard.getMoves());
    }

    public static Image getAllPiecesImg() {
        return allPiecesImg;
    }

    public static int getPieceImageIndex(Character pieceType) {
        return imgColIdx.get(pieceType);
    }

    public Color getGridBackgroundColor(int x, int y) {
        return gridBackgroundColor.get(y).get(x);
    }

    public void setGridBackgroundColor(int x, int y, Color color) {
        gridBackgroundColor.get(y).set(x, color);
    }

    public void setLegalMovesBackground(Piece piece) {
        for (Position pos : piece.getLegalMoves()) {
            setGridBackgroundColor(pos.getX(), pos.getY(), Color.LIGHTGREEN);
        }
    }

    public String getAIMove(){
        StockFishAPI api = new StockFishAPI();
        String currentFEN = chessboard.getFEN();

        String AImove = api.getAIMove(currentFEN);

        if (!AImove.equals("0-0") && !AImove.equals("0-0-0")){
            if (AImove.length() == 5)
                AImove = AImove.substring(0, 4);
            else if (AImove.length() == 6)
                AImove = AImove.substring(0, 5);
        }
        return AImove;
    }

    private void applyAIMove(String move) {

        if (move.length() == 4) {
            if (move.equals("0-0")) {
                getSelectedPiece().move(new Position(6, 0));
            } else if (move.equals("0-0-0")) {
                getSelectedPiece().move(new Position(2, 0));
            } else {
                Position from = new Position(move.substring(0, 2));
                Position to = new Position(move.substring(2, 4));
                Piece currentPiece = chessboard.getPosition(from);
                currentPiece.move(to);
                System.out.println("AI Move: " + move);
            }
        }
        else if (move.length() == 5){
            Position from = new Position(move.substring(0, 2));
            Position to = new Position(move.substring(2, 4));
            if (move.length() == 5) {
                char promotionPiece = move.charAt(4);
                handlePromotion(from, to, promotionPiece);
            } else {
                Piece selectedPiece = chessboard.getPosition(from);
                selectedPiece.move(to);
            }
        }
    }

    private void handlePromotion (Position from, Position to,char promotionPiece){
        Piece pawn = chessboard.getPosition(from);

        if (!(pawn instanceof Pawn)) {
            throw new IllegalArgumentException("Promotion must be a pawn");
        }

        Piece newPiece = switch (promotionPiece) {
            case 'q' ->
                    new Queen(to, chessboard, pawn.getOwner());
            case 'r' ->
                    new Rook(to, chessboard, pawn.getOwner());
            case 'b' ->
                    new Bishop(to, chessboard, pawn.getOwner());
            case 'n' ->
                    new Knight(to, chessboard, pawn.getOwner());
            default -> throw new IllegalArgumentException("Invalid promotion piece");
        };
        System.out.println("Promotion Piece: " + promotionPiece);

        chessboard.setPosition(from, null);
        chessboard.setPosition(to, newPiece);
        newPiece.setPos(to);
    }
}