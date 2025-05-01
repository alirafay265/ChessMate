package chessengine.pieces;

import chessengine.ChessBoard;
import chessengine.Player;
import chessengine.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private boolean hasMoved = false;
    private boolean hasMadeAnPassant = false;

    public Pawn(Position position, ChessBoard board, Player owner) {
        super(position, board, owner, "Pawn");
        if (getY() != (owner.isWhite() ? 1 : 6)) {
            hasMoved = true;
        }
    }

    @Override
    public boolean isValidMove(Position to) {
        if (getX() != to.getX()) {
            return isValidCaptureMove(to);
        }
        return isValidStraightMove(to);
    }

    @Override
    public Collection<Position> getLegalMoves() {
        final Collection<Position> legalMoves = new ArrayList<>();

        final Collection<Position> testPostitions = new ArrayList<>(List.of(
                new Position(pos.getX(), pos.getY() + owner.getDir()), // 1 forward
                new Position(pos.getX() + 1, pos.getY() + owner.getDir()),
                new Position(pos.getX() - 1, pos.getY() + owner.getDir())
        ));

        if (!hasMoved) {
            testPostitions.add(new Position(pos.getX(), pos.getY() + 2 * owner.getDir()));
        }

        for (final Position p : testPostitions) {
            if (messesUpcheck(p))
                continue;
            if (isValidMove(p)) {
                legalMoves.add(p);
            }
        }

        return legalMoves;
    }

    @Override
    public void move(Position to) {
        if (!getLegalMoves().contains(to)) {
            throw new IllegalArgumentException("Illegal move");
        }
        if (moveIsEnPassant(to)) {
            this.hasMadeAnPassant = true;
        }
        board.move(this, to);
        this.pos = to;
        hasMoved = true;
    }

    public void setHasMadeAnPassant(boolean hasMadeAnPassant) {
        this.hasMadeAnPassant = hasMadeAnPassant;
    }

    public boolean getHasMadeEnPassant() {
        return hasMadeAnPassant;
    }

    @Override
    protected boolean threatening(Position position) {
        if (Math.abs(pos.getX() - position.getX()) == 1 && pos.getY() + owner.getDir() == position.getY()) {
            return true;
        }
        return false;
    }

    private boolean isValidStraightMove(Position to) {
        if (pos.getX() != to.getX()) {
            return false;
        }
        if (board.getPosition(to) != null) {
            return false;
        }
        if (pos.getY() + owner.getDir() == to.getY()) {
            return true;
        }
        if (!hasMoved && pos.getY() + 2 * owner.getDir() == to.getY()
                && board.getPosition(new Position(getX(), getY() + owner.getDir())) == null) {
            return true;
        }
        return false;
    }

    private boolean isValidCaptureMove(Position to) {
        if (Math.abs(pos.getX() - to.getX()) != 1) {
            return false;
        }
        if (pos.getY() + owner.getDir() != to.getY()) {
            return false;
        }
        if (moveIsEnPassant(to)) {
            return true;
        }
        if (board.getPosition(to) == null) {
            return false;
        }
        if (board.getPosition(to).getOwner() == owner) {
            return false;
        }
        return true;
    }

    private boolean moveIsEnPassant(Position to) {
        if (Math.abs(pos.getX() - to.getX()) != 1) {
            return false;
        }
        if (pos.getY() + owner.getDir() != to.getY()) {
            return false;
        }
        if (board.getPosition(to) != null) {
            return false;
        }
        final Piece possiblyTake = board.getPosition(new Position(to.getX(), to.getY() - owner.getDir()));
        if (!(possiblyTake instanceof Pawn)) {
            return false;
        }
        if (board.getLastPieceMoved() == possiblyTake && possiblyTake.getMoveCount() == 1) {
            return true;
        }
        return false;
    }
}