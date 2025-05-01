package chessengine.StockFishAPI;

public class StockFishAPI {
    private StockFish api;
    public static int depth = 0;

    public StockFishAPI() {
        api = new StockFish();

        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win"))
            api.startEngine("stockfishWin/stockfish-windows-x86-64-avx2");
        else if (osName.contains("mac"))
            api.startEngine("/Users/macos/Documents/ChessEngine/stockfishMac/stockfish-macos-x86-64");
        else
            System.out.println("The program is running on an unknown OS");
    }

    public String getAIMove(String fen) {
        api.setPositionUsingFEN(fen);
        String response = api.getBestMove(depth);
        String bestMove = response.split("bestmove ")[1].split(" ")[0];
        return bestMove;
    }

    public void stopEngine() {
        api.stopEngine();
    }
}
