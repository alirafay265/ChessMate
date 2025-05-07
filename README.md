# ChessMate: JavaFX Chess Game with AI

Welcome to **ChessMate**, a complete chess application developed using **JavaFX** and **Stockfish AI engine**, built as a semester project by two passionate contributors. This game supports both **Multiplayer (Two Player)** and **Single Player (AI)** modes, includes modern chess rules like **castling**, **en passant**, **pawn promotion**, and has a fully working **GUI** integrated with **FXML** and **SceneBuilder**.

---

## ⚙️ Setup Instructions

### 1. Prerequisites

* Java 17 or above
* JavaFX SDK (added to IDE module path)
* SceneBuilder (for editing FXML)
* Stockfish binary (in `stockfishWin/` or `stockfishMac/` depending on OS)

### 2. Cloning the Repository

```bash
git clone https://github.com/alirafay265/ChessMate.git
```

### 3. Running the App

Make sure Stockfish executable is present in `stockfishWin/` or `stockfishMac/`.
Open the ChessEngine folder in your IDE and run the App.java file from your IDE.

---

## ✨ Features

* Classic chess rules implemented (including castling, en passant, stalemate, and checkmate detection)
* Smooth and responsive JavaFX GUI
* Interactive board using `GridPane` and `Rectangle` for tile coloring
* Pawn promotion UI with image click selection
* Move history tracking in sidebar (formatted as standard PGN notation)
* Play against Stockfish AI with selectable difficulty levels
* Load game from FEN string or export current board to a `.fen` file
* Highlights for legal moves and selected pieces

---

## 👥 Contributors

* [Ali Rafay](https://github.com/alirafay265) — Developer
* [Bilal Sarwar](https://github.com/iambillx) — Developer

Only the two of us contributed to the entire logic, frontend, backend, and integration of this project.

---

## 🎓 Technologies Used

* **Java 17+**
* **JavaFX** for GUI
* **FXML** for layout definitions (designed in SceneBuilder)
* **Stockfish** (external chess engine for AI)
* **Java Collections** (ArrayList, Map, etc.)
* **Object-Oriented Design** (Inheritance, Composition, Interfaces, Enums)

---

## 📚 Project Structure

```
chessengine/
|-- controllers/              // All FXML controller classes
|-- pieces/                  // All chess piece classes (Rook, Knight, etc.)
|-- utils/                   // Helper classes like FENParser, PopUp
|-- StockFishAPI/            // Handles communication with Stockfish engine
|-- ChessBoard.java          // Core game logic
|-- App.java                  // The runnable file
|-- ChessBoardIterator.java   // Iterator class for the chessboard
|-- Position.java            // Position abstraction (x, y)
|-- Player.java              // Player class with castling state
|-- PieceColor.java          // Enum for White and Black
```

---

## ⚖️ Rules Implemented

* **Pawn Promotion**: When a pawn reaches the back rank, a promotion popup is shown allowing you to choose Queen, Rook, Bishop, or Knight.
* **En Passant**: Captures are handled correctly when a pawn moves two squares forward.
* **Castling**: Legal checks are enforced (King and Rook unmoved, spaces empty, not moving through check).
* **Check, Checkmate & Stalemate**: Fully implemented and validated after each move.

---

## 🌐 AI Integration

We integrated Stockfish by launching it as a subprocess via `ProcessBuilder`:

* Communicates with Stockfish via its UCI (Universal Chess Interface)
* Converts board state to FEN using our custom `FENParser`
* Parses best move output returned by Stockfish
* Difficulty controlled by search depth (Easy: 1, Medium: 3, Hard: 6)

---

## 📹 Screenshots

![image](https://github.com/user-attachments/assets/3830cd0f-88bc-4dd5-9b36-ccf7dc386f38)
![image](https://github.com/user-attachments/assets/04d06eca-e06b-4ca6-b8b2-0237bcdb4121)
![image](https://github.com/user-attachments/assets/521607fa-8b5f-47d4-9b87-7d7b4371313d)
![image](https://github.com/user-attachments/assets/22c4672f-8838-4e78-a803-17b473994b2b)
![image](https://github.com/user-attachments/assets/c5a9442d-b717-49d1-ba27-30c9f99384dc)
![image](https://github.com/user-attachments/assets/2d4e7f07-c874-49d4-b458-9376a8918870)
![image](https://github.com/user-attachments/assets/09d5abdd-3ae4-4c5e-82e4-2db5817c5301)
![image](https://github.com/user-attachments/assets/45a0adfc-2f24-421c-add7-73a3f75dea7f)
![image](https://github.com/user-attachments/assets/d163fb84-c28a-4a69-ab64-8c5cf208984e)
![image](https://github.com/user-attachments/assets/0a345adc-c040-4916-8de1-9f2d9e6ed4c4)

---

## 📆 Trouble Shoot

* If you registering an account or logging into an account gives an error in the console, make sure you have the ChessEngine folder opened in the IDE and not the parent folder. the userdata.txt file is present in ChessEngine/src/main.
* If the AI is not making a move in the Single Player mode, make sure you have the ChessEngine folder opened in the IDE. If it still doesn't work, make sure the StockFish API is configured properly and has the executable file in it. Single Player mode is configured only for Mac and Windows currently.
* If any other error occurs, ask ChatGPT, it's 2025.

---

## 📆 Future Enhancements

(feel free to contribute to these)
* Add timers for each player
* Implement draw by repetition and insufficient material
* Add a game-saving feature that records full PGN moves with timestamps
* Add undo and redo functionality
* Enhance UI with sound effects and animations
* Implement user login and online multiplayer via sockets or Firebase

---

## 📄 License

[MIT License](https://opensource.org/licenses/MIT) (or your preferred license)

---

If you like this project or want to contribute, feel free to star it or reach out!

> *"Checkmate isn't just the end of a game, it's proof of a plan well executed."*
