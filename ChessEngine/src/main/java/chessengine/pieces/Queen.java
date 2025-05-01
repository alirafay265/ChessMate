package chessengine.pieces;

import chessengine.ChessBoard;
import chessengine.Player;
import chessengine.Position;

import java.util.List;

public class Queen extends LinearPiece {

    public Queen(Position position, ChessBoard board, Player owner) {
        super(position, board, owner, "Queen");

        super.setDirections(List.of(
                new Position(0, 1),
                new Position(0, -1),
                new Position(1, 0),
                new Position(-1, 0),
                new Position(1, 1),
                new Position(1, -1),
                new Position(-1, 1),
                new Position(-1, -1)
        ));
    }
}
