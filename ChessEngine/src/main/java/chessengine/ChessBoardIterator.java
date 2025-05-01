package chessengine;

import chessengine.pieces.Piece;

import java.util.Iterator;

public class ChessBoardIterator implements Iterator<Piece> {

    private ChessBoard board;
    private int x = 0;
    private int y = 0;

    public ChessBoardIterator(ChessBoard board) {
        this.board = board;
    }

    @Override
    public boolean hasNext() {
        for (int i = y; i < 8; i++) {
            int startIndex = (i == y ? x : 0);
            for (int j = startIndex; j < 8; j++) {
                if (board.getPosition(new Position(j, i)) != null) {
                    x = j;
                    y = i;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Piece next() {
        Piece piece = board.getPosition(new Position(x, y));
        x = (x + 1) % 8;
        if (x == 0)
            y++;
        return piece;
    }
}