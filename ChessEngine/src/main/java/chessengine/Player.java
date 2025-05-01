package chessengine;

public class Player {

    private boolean white;
    private boolean hasTurn = false;
    private int dir;
    private boolean canCastleKingSide = true;
    private boolean canCastleQueenSide = true;

    public Player(PieceColor color) {
        switch (color) {
            case WHITE:
                this.white = true;
                break;
            case BLACK:
                this.white = false;
                break;
            default:
                throw new IllegalArgumentException("Invalid color: " + color);
        }
        dir = this.white ? 1 : -1;
    }

    public void toggleHasTurn() {
        hasTurn = !hasTurn;
    }

    public boolean hasTurn() {
        return hasTurn;
    }

    @Override
    public String toString() {
        return (white ? "White" : "Black");
    }

    public char toChar() {
        return (white ? 'w' : 'b');
    }

    public int getDir() {
        return dir;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean iswhite) {
        white = iswhite;
    }

    public boolean canCastleKingSide() {
        return canCastleKingSide;
    }

    public boolean canCastleQueenSide() {
        return canCastleQueenSide;
    }

    public void setCastling(boolean value, boolean queenSide) {
        if (queenSide) {
            this.canCastleQueenSide = value;
        } else {
            this.canCastleKingSide = value;
        }
    }

    public void disableCastling() {
        setCastling(false, true);
        setCastling(false, false);
    }

    public String getCastlingRights() {
        String rights = "";
        if (canCastleKingSide) {
            rights += "k";
        }
        if (canCastleQueenSide) {
            rights += "q";
        }

        if (white)
            rights = rights.toUpperCase();

        return rights;
    }
}
