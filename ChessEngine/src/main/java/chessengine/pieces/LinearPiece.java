package chessengine.pieces;

import chessengine.ChessBoard;
import chessengine.Player;
import chessengine.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class LinearPiece extends Piece {

    private Collection<Position> legalDirections = new ArrayList<Position>();

    public LinearPiece(Position position, ChessBoard board, Player owner, String name) {
        super(position, board, owner, name);
    }

    @Override
    public Collection<Position> getLegalMoves() {
        final Collection<Position> legalMoves = new ArrayList<Position>();

        for (final Position tmpDir : legalDirections) {
            Position currentPosition = new Position(pos).add(tmpDir);

            while (currentPosition.insideBoard()) {
                if (board.getPosition(currentPosition) != null
                        && board.getPosition(currentPosition).getOwner() == this.owner) {
                    break;
                }

                if (messesUpcheck(currentPosition)) {
                    if (board.getPosition(currentPosition) != null) {
                        break;
                    }
                    currentPosition = currentPosition.add(tmpDir);
                    continue;
                }

                if (board.getPosition(currentPosition) == null) {
                    legalMoves.add(currentPosition);
                }
                else {
                    if (board.getPosition(currentPosition).getOwner() != this.owner) {
                        legalMoves.add(currentPosition);
                    }
                    break;
                }
                currentPosition = currentPosition.add(tmpDir);
            }
        }
        return legalMoves;
    }

    @Override
    protected boolean threatening(Position position) {
        for (final Position tmpDir : legalDirections) {
            Position currentPosition = new Position(pos).add(tmpDir);
            while (currentPosition.insideBoard()) {
                if (currentPosition.equals(position)) {
                    return true;
                }
                if (board.getPosition(currentPosition) != null) {
                    break;
                }
                currentPosition = currentPosition.add(tmpDir);
            }
        }
        return false;
    }

    protected void setDirections(List<Position> directions) {
        legalDirections = new ArrayList<Position>(directions);
    }
}
