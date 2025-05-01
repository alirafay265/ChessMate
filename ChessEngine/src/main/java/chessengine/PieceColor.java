package chessengine;

public enum PieceColor {
    WHITE(1), BLACK(-1);

    private final int dir;

    private PieceColor(int dir) { // assigns direction to the color
        this.dir = dir;
    }

    public int getDir() { // getter for direction
        return dir;
    }

}
