package chessengine.pieces;

import chessengine.ChessBoard;
import chessengine.Player;
import chessengine.Position;

import java.util.Collection;

public abstract class Piece {

    public static Piece placePiece(Player player, Position pos, ChessBoard board, char c) {
        Piece piece;

        switch (c) {
            case 'K':
            case 'k':
                piece = new King(pos, board, player);
                break;
            case 'Q':
            case 'q':
                piece = new Queen(pos, board, player);
                break;
            case 'R':
            case 'r':
                piece = new Rook(pos, board, player);
                break;
            case 'B':
            case 'b':
                piece = new Bishop(pos, board, player);
                break;
            case 'N':
            case 'n':
                piece = new Knight(pos, board, player);
                break;
            case 'P':
            case 'p':
                piece = new Pawn(pos, board, player);
                break;
            default:
                piece = null;
        }
        return piece;
    }

    protected Position pos;
    protected ChessBoard board;
    protected Player owner;
    protected String name;

    protected int moveCount = 0;

    public Piece(Position position, ChessBoard board, Player owner, String name) {
        this.pos = position;
        this.board = board;
        this.owner = owner;
        this.name = name;
        if (board.getPosition(position) != this) {
            board.setPosition(position, this);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public char toChar() {
        char out = name.toLowerCase().toCharArray()[0];
        if (this instanceof Knight)
            out = 'n';

        if (owner.isWhite())
            out = Character.toUpperCase(out);
        return out;
    }

    public abstract Collection<Position> getLegalMoves();

    public int getX() {
        return pos.getX();
    }

    public int getY() {
        return pos.getY();
    }

    public boolean isValidMove(Position to) {
        return getLegalMoves().contains(to);
    }

    public boolean isWhite() {
        return owner.isWhite();
    }

    public Position getPos() {
        return new Position(pos);
    }

    public void setPos(Position pos) {
        if (pos.getX() < 0 || pos.getX() > 7 || pos.getY() < 0 || pos.getY() > 7)
            throw new IllegalArgumentException("Not a valid position");
        this.pos = pos;
    }

    public void move(Position to) throws IllegalArgumentException {
        if (!isValidMove(to)) {
            throw new IllegalArgumentException("Illegal move");
        }
        board.move(this, to);
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void addMoveCount() {
        moveCount++;
    }

    public Player getOwner() {
        return owner;
    }

    protected abstract boolean threatening(Position position);

    protected boolean messesUpcheck(Position to) {
        if (!to.insideBoard())
            return false;

        final Piece tmp = board.getPosition(to);
        board.setPosition(to, this);
        board.setPosition(pos, null);

        final boolean messesUp = board.inCheck(owner);
        board.setPosition(pos, this);
        board.setPosition(to, tmp);
        return messesUp;
    }
}