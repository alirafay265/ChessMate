package chessengine.pieces;

import chessengine.ChessBoard;
import chessengine.Player;
import chessengine.Position;

import java.util.List;

public class Rook extends LinearPiece {

    public Rook(Position position, ChessBoard board, Player owner) {
        super(position, board, owner, "Rook");

        super.setDirections(List.of(
                new Position(0, 1),
                new Position(0, -1),
                new Position(1, 0),
                new Position(-1, 0)
        ));
    }
}
