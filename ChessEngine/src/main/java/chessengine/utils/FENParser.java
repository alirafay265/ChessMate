package chessengine.utils;

import chessengine.ChessBoard;
import chessengine.PieceColor;
import chessengine.Player;
import chessengine.Position;
import chessengine.pieces.Pawn;
import chessengine.pieces.Piece;
import java.io.*;
import java.util.Scanner;

public abstract class FENParser {

    public static final String FEN_EXTENSION = "fen";

    public static final String DEFAULT_STRING = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static String readFENFromFile(File file) throws FileNotFoundException {
        String FENString = "";
        final Scanner scanner = new Scanner(file);
        FENString = scanner.nextLine();
        scanner.close();
        return FENString;
    }

    public static ChessBoard getBoardFromFEN(String input) throws IllegalFENException {
        System.out.println("Fen String: " + input);
        final Player white = new Player(PieceColor.WHITE);
        final Player black = new Player(PieceColor.BLACK);
        final ChessBoard board = new ChessBoard(white, black);
        final String[] data = input.split(" ");
        if (data.length != 6) {
            throw new IllegalFENException("Invalid FEN string");
        }
        final String[] rows = data[0].split("/");
        if (rows.length != 8) {
            throw new IllegalFENException("Invalid FEN string");
        }

        for (int i = 0; i < 8; i++) {
            final String row = rows[i];

            int stringIndex = 0;
            int boardIndex = 0;
            while (boardIndex < 8) {
                final char pieceCharacter = row.charAt(stringIndex);
                if (Character.isDigit(pieceCharacter)) {
                    boardIndex += Character.getNumericValue(pieceCharacter);
                } else {
                    final Player player = Character.isUpperCase(pieceCharacter) ? white : black;
                    Piece.placePiece(player, new Position(boardIndex, 7 - i), board, pieceCharacter);
                    boardIndex++;
                }
                stringIndex++;
            }
        }

        if (data[1].equals("w")) {
            board.setTurn(PieceColor.WHITE);
        } else if (data[1].equals("b")) {
            board.setTurn(PieceColor.BLACK);
            board.addMove("----");
        } else {
            throw new IllegalFENException("Invalid FEN string");
        }

        board.disableCastling();
        for (final char c : data[2].toCharArray()) {
            switch (c) {
                case 'K':
                    white.setCastling(true, false);
                    break;
                case 'Q':
                    white.setCastling(true, true);
                    break;
                case 'k':
                    black.setCastling(true, false);
                    break;
                case 'q':
                    black.setCastling(true, true);
                    break;
            }
        }

        String enPassantTarget = data[3].equals("-") ? null : data[3];
        if (enPassantTarget != null) {
            Position enPassantTargetPosition = new Position(enPassantTarget);
            int pawnY = (enPassantTargetPosition.getY() == 2 ? 3 : 4);
            Piece pawn = board.getPosition(new Position(enPassantTargetPosition.getX(), pawnY));
            pawn.addMoveCount();
            board.setLastPieceMoved(pawn);
        }

        board.setHalfMoves(Integer.parseInt(data[4]));

        board.setFullMoves(Integer.parseInt(data[5]));
        return board;
    }

    public static ChessBoard getBoardFromDefaultFEN() {
        try {
            return getBoardFromFEN(DEFAULT_STRING);
        } catch (final IllegalFENException e) {
            return null;
        }
    }

    public static String generateFEN(ChessBoard board) {
        final StringBuilder FENString = new StringBuilder();

        int emptySpaces = 0;
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                final Piece piece = board.getPosition(new Position(x, y));
                if (piece == null) {
                    emptySpaces++;
                    continue;
                }
                if (emptySpaces != 0) {
                    FENString.append(emptySpaces);
                    emptySpaces = 0;
                }
                FENString.append(piece.toChar());
            }
            if (emptySpaces != 0) {
                FENString.append(emptySpaces);
                emptySpaces = 0;
            }
            if (y != 0)
                FENString.append('/');
        }

        FENString.append(" " + board.getPlayerTurn().toChar());

        FENString.append(" " + board.getCastlingRights());

        Piece lastPieceMoved = board.getLastPieceMoved();
        if (lastPieceMoved instanceof Pawn && lastPieceMoved.getMoveCount() == 1
                && (lastPieceMoved.getPos().getY() == 3 || lastPieceMoved.getPos().getY() == 4)) {

            int direction = -lastPieceMoved.getOwner().getDir();
            Position targetPosition = lastPieceMoved.getPos().add(new Position(0, direction));
            FENString.append(" " + targetPosition.toString());

        } else {
            FENString.append(" -");
        }

        FENString.append(" " + board.getHalfMoves());

        FENString.append(" " + board.getFullMoves());

        return FENString.toString();
    }


    public static void saveToFile(String fenString, File file) throws IOException {
        File directory = file.getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(fenString);
            System.out.println("FEN saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error while saving the FEN to file: " + e.getMessage());
            throw e;
        }
    }
}