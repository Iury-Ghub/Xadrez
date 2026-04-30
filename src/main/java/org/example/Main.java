package org.example;

import org.example.Controller.ChessGame;

public class Main {
    public static void main(String[] args) {
        ChessGame chessGame = new ChessGame();
        chessGame.viewGame();
        chessGame.MoveWhite("e3");
        chessGame.viewGame();
    }
}