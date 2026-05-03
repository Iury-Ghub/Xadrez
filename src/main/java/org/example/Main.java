package org.example;

import org.example.Controller.ChessGame;
import org.example.model.Square;

public class Main {
    public static void main(String[] args) {
        ChessGame chessGame = new ChessGame();
        chessGame.viewGame();
        chessGame.pawnsMoveWhite(new Square('e', 2), new Square('e', 4));
        chessGame.viewGame();
        chessGame.pawnsMoveBlack("f5");
        chessGame.viewGame();
        chessGame.pawnsMoveWhite(new Square('e',4),new Square('d',5));
        chessGame.viewGame();
    }
}