package chessengine.pieces;

import chessengine.ChessBoard;
import chessengine.Player;
import chessengine.Position;

import java.util.ArrayList;
import java.util.Collection;

public class King extends Piece {

    public King(Position position, ChessBoard board, Player owner) {
        super(position, board, owner, "King");
    }

    public boolean inCheck() {
        return positionIsInCheck(this.pos);
    }

    private boolean positionIsInCheck(Position position) {
        for (final Piece piece : board) {
            if (piece.getOwner() != owner && piece.threatening(position)) {
                return true;
            }
        }
        return false;
    }

    private boolean canCastleQueen() {
        if (!owner.canCastleQueenSide()) {
            return false;
        }

        if (inCheck()) {
            return false;
        }

        if (positionIsInCheck(new Position(pos.getX() - 1, pos.getY()))) {
            return false;
        }

        if (positionIsInCheck(new Position(pos.getX() - 2, pos.getY()))) {
            return false;
        }

        for (int i = pos.getX() - 1; i > pos.getX() - 4; i--) {
            if (board.getPosition(new Position(i, pos.getY())) != null) {
                return false;
            }
        }

        return true;
    }

    private boolean canCastleKing() {
        if (!owner.canCastleKingSide()) {
            return false;
        }

        if (inCheck()) {
            return false;
        }

        if (positionIsInCheck(new Position(pos.getX() + 1, pos.getY()))) {
            return false;
        }

        if (positionIsInCheck(new Position(pos.getX() + 2, pos.getY()))) {
            return false;
        }

        for (int i = pos.getX() + 1; i < pos.getX() + 3; i++) {
            if (board.getPosition(new Position(i, pos.getY())) != null) {
                return false;
            }
        }
        return true;
    }

    private Collection<Position> getCastlingMoves() {
        final Collection<Position> legalMoves = new ArrayList<Position>();

        if (canCastleQueen()) {
            legalMoves.add(new Position(pos.getX() - 2, pos.getY()));
        }

        if (canCastleKing()) {
            legalMoves.add(new Position(pos.getX() + 2, pos.getY()));
        }

        return legalMoves;
    }

    @Override
    public Collection<Position> getLegalMoves() {
        final Collection<Position> legalMoves = new ArrayList<Position>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                final Position p = new Position(getX() + i, getY() + j);
                if (positionIsInCheck(p)) {
                    continue;
                }

                if (!p.insideBoard()) {
                    continue;
                }

                if (board.getPosition(p) != null && board.getPosition(p).getOwner() == owner) {
                    continue;
                }

                final Piece newPosPiece = board.getPosition(p);

                board.setPosition(p, this);
                board.setPosition(pos, null);
                final boolean posInCheck = positionIsInCheck(p);
                board.setPosition(p, newPosPiece);
                board.setPosition(pos, this);

                if (!posInCheck)
                    legalMoves.add(p);
            }
        }

        legalMoves.addAll(getCastlingMoves());
        return legalMoves;
    }

    @Override
    protected boolean threatening(Position position) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                final Position p = new Position(getX() + i, getY() + j);
                if (p.equals(position)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void move(Position to) throws IllegalArgumentException {
        if (!isValidMove(to)) {
            throw new IllegalArgumentException("Illegal move");
        }

        final int y = pos.getY();
        if (to.getX() == pos.getX() - 2) {
            board.move(board.getPosition(new Position(0, y)), new Position(3, y), true);
        } else if (to.getX() == pos.getX() + 2) {
            board.move(board.getPosition(new Position(7, y)), new Position(5, y), true);
        }

        board.move(this, to, true);
    }
}