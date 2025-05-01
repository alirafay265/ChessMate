package chessengine.pieces;

import chessengine.ChessBoard;
import chessengine.Player;
import chessengine.Position;

import java.util.List;

public class Bishop extends LinearPiece {
    public Bishop(Position position, ChessBoard board, Player owner) {
        super(position, board, owner, "Bishop");

        super.setDirections(List.of(
                new Position(1, 1),
                new Position(1, -1),
                new Position(-1, 1),
                new Position(-1, -1)
        ));
    }
}
