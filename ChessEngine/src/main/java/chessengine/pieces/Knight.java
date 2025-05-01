package chessengine.pieces;

import chessengine.ChessBoard;
import chessengine.Player;
import chessengine.Position;

import java.util.ArrayList;
import java.util.Collection;

public class Knight extends Piece {

    private final static int[] dX = { 2, 1, -1, -2, -2, -1, 1, 2 };

    private final static int[] dY = { 1, 2, 2, 1, -1, -2, -2, -1 };

    public Knight(Position position, ChessBoard board, Player owner) {
        super(position, board, owner, "Knight");
    }

    @Override
    public boolean isValidMove(Position to) {
        return getLegalMoves().contains(to);
    }

    @Override
    public Collection<Position> getLegalMoves() {
        final Collection<Position> legalMoves = new ArrayList<Position>();

        for (int i = 0; i < dX.length; i++) {
            final Position to = new Position(getX() + dX[i], getY() + dY[i]);
            if (messesUpcheck(to)) {
                continue;
            }
            if (validOnBoard(to))
                legalMoves.add(to);
        }

        return legalMoves;
    }

    @Override
    protected boolean threatening(Position position) {
        for (int i = 0; i < dX.length; i++) {
            final Position to = new Position(getX() + dX[i], getY() + dY[i]);
            if (to.equals(position))
                return true;
        }
        return false;
    }

    private boolean validOnBoard(Position to) {
        if (!to.insideBoard())
            return false;
        if (board.getPosition(to) == null)
            return true;
        if (board.getPosition(to).getOwner() != owner)
            return true;
        return false;
    }
}