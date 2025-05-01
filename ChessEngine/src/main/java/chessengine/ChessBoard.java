package chessengine;

import chessengine.pieces.*;
import chessengine.utils.*;

import java.util.ArrayList;
import java.util.Iterator;

public class ChessBoard implements Iterable<Piece> {

    private final ArrayList<ArrayList<Piece>> board;
    private final ArrayList<String> moves = new ArrayList<>();
    private Piece selectedPiece;
    private Piece lastMovedPiece;
    private Player white;
    private Player black;
    private Player turn;
    private int halfMoves = 0;
    private int fullMoves = 1;
    private boolean gameFinished = false;
    private String gameMessage = "";
    private Pawn upgradablePawn = null;

    public ChessBoard() {
        this(new Player(PieceColor.WHITE), new Player(PieceColor.BLACK));
    }

    public ChessBoard(Player white, Player black) {
        board = new ArrayList<ArrayList<Piece>>();
        for (int i = 0; i < 8; i++) {
            board.add(new ArrayList<Piece>());
            for (int j = 0; j < 8; j++) {
                board.get(i).add(null);
            }
        }

        this.white = white;
        this.black = black;

        if (white == null)
            this.white = new Player(PieceColor.WHITE);
        if (black == null)
            this.black = new Player(PieceColor.BLACK);

        turn = white;
    }

    public Piece getPosition(Position position) {
        if (position.getX() < 0 || position.getX() > 7 || position.getY() < 0 || position.getY() > 7)
            return null;
        return board.get(position.getY()).get(position.getX());
    }

    public void setPosition(Position position, Piece piece) {
        if (getPosition(position) == piece)
            return;
        board.get(position.getY()).set(position.getX(), piece);
    }

    public void move(Piece piece, Position to, boolean isCastle) {
        if (!piece.isValidMove(to) && !isCastle)
            throw new IllegalArgumentException("Invalid move");

        final Position originalPos = piece.getPos();
        boolean pieceWasCaptured = (getPosition(to) != null);

        setPosition(piece.getPos(), null);
        setPosition(to, piece);
        piece.setPos(to);

        handleCastlingDisabling(piece, originalPos);

        pieceWasCaptured |= handleEnPassantMove(piece);

        lastMovedPiece = piece;
        piece.addMoveCount();

        if (!isCastle || piece instanceof King) {
            moves.add(originalPos.toString() + to.toString());
            handleHalfMove(piece);
            handleFullMove(piece, pieceWasCaptured);
            turn = (turn == white ? black : white);
        }

        if (piece instanceof Pawn && (to.getY() == 0 || to.getY() == 7)) {
            setPromotePawn((Pawn) piece);
        }

        checkGameFinished();
    }

    public void move(Piece piece, Position to) {
        move(piece, to, false);
    }

    public boolean inCheck(Player player) {
        for (final Piece piece : this) {
            if (piece.getOwner() == player && piece instanceof King) {
                final King king = (King) piece;
                return king.inCheck();
            }
        }
        return false;
    }

    public String getMoves() {
        String movesString = "";
        for (int i = 0; i < moves.size(); i++) {
            if (i % 2 == 0) {
                final int moveNr = (i / 2 + 1);
                final String moveNrString = String.format("%2s", moveNr);
                movesString += moveNrString + ". ";
                movesString += moves.get(i);
            } else {
                movesString += "  | " + moves.get(i) + "\n";
            }
        }
        return movesString;
    }

    @Override
    public Iterator<Piece> iterator() {
        return new ChessBoardIterator(this);
    }

    public Piece getLastPieceMoved() {
        return lastMovedPiece;
    }

    @Override
    public String toString() {
        return "ChessBoard [board=" + FENParser.generateFEN(this) + "]";
    }

    public void setTurn(PieceColor color) {
        this.turn = (color == PieceColor.WHITE ? white : black);
    }

    public void setLastPieceMoved(Piece piece) {
        this.lastMovedPiece = piece;
    }

    public void disableCastling() {
        white.disableCastling();
        black.disableCastling();
    }

    public String getCastlingRights() {
        String rights = white.getCastlingRights() + black.getCastlingRights();
        if (rights.length() == 0)
            rights = "-";
        return rights;
    }

    public String getFEN() {
        return FENParser.generateFEN(this);
    }

    public Player getPlayerTurn() {
        return turn;
    }

    public void setHalfMoves(int moves) {
        if (moves < 0)
            throw new IllegalArgumentException("Number of half moves must be positive.");
        halfMoves = moves;
    }

    public int getHalfMoves() {
        return halfMoves;
    }

    public void setFullMoves(int moves) {
        if (moves < 0)
            throw new IllegalArgumentException("Number of full moves must be positive.");
        fullMoves = moves;
    }

    public int getFullMoves() {
        return fullMoves;
    }

    public boolean getGameFinished() {
        return gameFinished;
    }

    public String getGameMessage() {
        return gameMessage;
    }

    public void setSelectedPiece(Piece piece) {
        selectedPiece = piece;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public Pawn getUpgradablePawn() {
        return this.upgradablePawn;
    }

    private void handleCastlingDisabling(Piece piece, Position originalPos) {
        if (piece instanceof King) {
            piece.getOwner().disableCastling();
            return;
        }
        if (!(piece instanceof Rook))
            return;

        if (piece.getMoveCount() != 0) {
            return;
        }

        if (originalPos.getX() == 7) {
            piece.getOwner().setCastling(false, false);
        } else if (originalPos.getX() == 0) {
            piece.getOwner().setCastling(false, true);
        }
    }

    private boolean handleEnPassantMove(Piece piece) {
        if (piece instanceof Pawn && ((Pawn) piece).getHasMadeEnPassant()) {
            System.out.println("Pawn has made en passant");
            final Position pos = new Position(piece.getX(), piece.getY() - piece.getOwner().getDir());
            setPosition(pos, null);
            return true;
        }
        return false;
    }

    private void setPromotePawn(Pawn piece) {
        this.upgradablePawn = piece;
    }

    public void promotePawn(Pawn piece, Piece upgrade) {
        setPosition(piece.getPos(), upgrade);
        checkGameFinished();
        this.upgradablePawn = null;
    }

    private void handleHalfMove(Piece piece) {
        if (!piece.getOwner().isWhite()) {
            fullMoves++;
        }
    }

    private void handleFullMove(Piece piece, boolean pieceWasCaptured) {
        if (!(piece instanceof Pawn || pieceWasCaptured)) {
            halfMoves++;
        } else {
            halfMoves = 0;
        }
    }

    private void checkGameFinished() {
        if (gameFinished)
            return;

        if (inCheckmate(turn)) {
            gameFinished = true;
            gameMessage = turn + " got checkmated.";
        } else if (inStalemate(turn)) {
            gameFinished = true;
            gameMessage = turn + " got stalemated. Draw!";
        } else if (inDraw()) {
            gameFinished = true;
            gameMessage = "The game resulted in draw.";
        }
    }

    private boolean inDraw() {
        if (halfMoves >= 50)
            return true;

        return false;
    }

    private boolean inStalemate(Player player) {
        if (inCheck(player))
            return false;
        for (final Piece piece : this) {
            if (piece.getOwner() == player) {
                for (final Position pos : piece.getLegalMoves()) {
                    if (piece.isValidMove(pos))
                        return false;
                }
            }
        }
        return true;
    }

    private boolean inCheckmate(Player player) {
        if (!inCheck(player))
            return false;
        for (final Piece piece : this) {
            if (piece.getOwner() == player) {
                for (final Position pos : piece.getLegalMoves()) {
                    if (piece.isValidMove(pos))
                        return false;
                }
            }
        }
        return true;
    }

    public void addMove(String string) {
        moves.add(string);
    }
}
