package chessengine.StockFishAPI;

import java.io.*;

public class StockFish {
    private Process process;
    private BufferedWriter writer;
    private BufferedReader reader;

    public boolean startEngine(String path) {
        try {
            ProcessBuilder pb = new ProcessBuilder(path);
            pb.redirectErrorStream(true);
            process = pb.start();
            writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setPositionUsingFEN(String fen) {
        sendCommand("position fen " + fen);
    }

    private void sendCommand(String command) {
        try {
            writer.write(command + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBestMove(int depth) {
        sendCommand("go depth " + depth);
        return readOutput();
    }

    private String readOutput() {
        StringBuilder output = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                if (line.startsWith("bestmove")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public void stopEngine() {
        sendCommand("quit");
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
