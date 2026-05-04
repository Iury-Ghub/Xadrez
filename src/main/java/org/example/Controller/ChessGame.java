package org.example.Controller;

import org.example.Enums.ColorPiece;
import org.example.Enums.NamePiece;
import org.example.model.Piece;
import org.example.model.Square;

import java.util.Objects;

public class ChessGame {
    Piece[][] board = new Piece[8][8];
    Piece[] captureWhite = new Piece[15];

    public ChessGame() {
        resetGame();
    }
    public void viewGame(){
        int line = 8;
        for (int i = 0; i<board.length;i++) {
            for (int j = 0; j<board[i].length;j++) {
                if(j == 0){
                    System.out.print("\u001B[33m"+line--+"|\u001B[0m");
                }
                getBoard(i, j);
            }
            System.out.println();
        }
        System.out.println("\u001B[33m   _  _  _  _  _  _  _  _\u001B[0m");
        System.out.println("\u001B[33m#  a  b  c  d  e  f  g  h\u001B[0m");
        System.out.println();
    }

    public void viewGameBlack(){
        int line = 1;
        for (int i = board.length-1;i >= 0;i--) {
            for (int j = board[i].length-1; j >= 0;j--) {
                if(j == 7){
                    System.out.print("\u001B[33m"+line+++"|\u001B[0m");
                }
                getBoard(i, j);
            }
            System.out.println();
        }
        System.out.println("\u001B[33m   _  _  _  _  _  _  _  _\u001B[0m");
        System.out.println("\u001B[33m#  a  b  c  d  e  f  g  h\u001B[0m");
        System.out.println();
    }

    private void getBoard(int i, int j) {
        Piece piece = board[i][j];
        if (piece == null && (i+j)%2 == 0) {
            System.out.print("\u001B[47m   \u001B[0m");
        } else if (piece == null) {
            System.out.print("\u001B[44m   \u001B[0m");
        } else if(piece.getColor() == ColorPiece.WHITE && (i+j)%2 == 0){
            System.out.print("\u001B[47m "+piece+" \u001B[0m");
        } else if (piece.getColor() == ColorPiece.WHITE) {
            System.out.print("\u001B[44m " +piece+ " \u001B[0m");
        } else if (piece.getColor() == ColorPiece.BLACK && (i+j)%2 == 0){
            System.out.print(" \u001B[47m\u001B[30m"+piece+"\u001B[0m ");
        } else {
            System.out.print(" \u001B[44m\u001B[30m" + piece + "\u001B[0m ");
        }
    }

    public void MoveWhite(NamePiece namePiece, Square positionPiece, Square positionMove){
        if (Objects.requireNonNull(namePiece) == NamePiece.P) {
            pawnsMoveWhite(positionPiece, positionMove);
        }
    }

    public void pawnsMoveWhite(Square positionPiece, Square positionMove){
        int x1 = 8-positionPiece.getLine(), x2 = 8 -positionMove.getLine();
        int y1 = positionPiece.getColum(), y2 = positionMove.getColum();

        if(!(board[x1][y1] != null && board[x1][y1].getName() == NamePiece.P && board[x1][y1].getColor() == ColorPiece.WHITE)){
            return;
        }
        if(board[x2][y2] == null && y1 == y2){
            if(x1 == x2+1){
                board[x2][y2] = board[x1][y1];
                board[x1][y1] = null;
            } else if (x1 == 6  && x1 == x2+2) {
                board[x2][y2] = board[x1][y1];
                board[x1][y1] = null;
            }
        } else if (board[x2][y2] != null
                && board[x2][y2].getColor() == ColorPiece.BLACK
                && (x2 == x1-1 && y2 == y1+1 || y2 == y1 - 1)){
            Piece piece = board[x2][y2];
            board[x2][y2] = board[x1][y1];
            board[x1][y1] = null;
        }
    }
    public void pawnsMoveBlack(Square positionPiece, Square positionMove){
        int x1 = 8-positionPiece.getLine(), x2 = 8 -positionMove.getLine();
        int y1 = positionPiece.getColum(), y2 = positionMove.getColum();

        if(!(board[x1][y1] != null && board[x1][y1].getName() == NamePiece.P && board[x1][y1].getColor() == ColorPiece.BLACK)){
            return;
        }

        if(board[x2][y2] == null && y1 == y2){
            if(x1 == x2-1){
                board[x2][y2] = board[x1][y1];
                board[x1][y1] = null;
            } else if (x1 == 1 && x1 == x2-2) {
                board[x2][y2] = board[x1][y1];
                board[x1][y1] = null;
            }
        } else if (board[x2][y2] != null
                && board[x2][y2].getColor() == ColorPiece.WHITE
                && (x2 == x1+1 && y2 == y1+1 || y2 == y1 - 1)){
            Piece piece = board[x2][y2];
            board[x2][y2] = board[x1][y1];
            board[x1][y1] = null;
        }
    }



    public void resetGame(){
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[i].length;j++){

                //Pawns
                if(i==1){
                    board[i][j] = new Piece(NamePiece.P, new Square(i,j),ColorPiece.BLACK);
                } else if (i==6) {
                    board[i][j] = new Piece(NamePiece.P, new Square(i,j),ColorPiece.WHITE);
                }

                //Kings
                if(i==0 && j==4){
                    board[i][j] = new Piece(NamePiece.K,new Square(i,j),ColorPiece.BLACK);
                } else if (i==7 && j==3) {
                    board[i][j] = new Piece(NamePiece.K,new Square(i,j),ColorPiece.WHITE);
                }

                //Queen
                if(i==0 && j==3){
                    board[i][j] = new Piece(NamePiece.Q,new Square(i,j),ColorPiece.BLACK);
                } else if (i==7 && j == 4) {
                    board[i][j] = new Piece(NamePiece.Q,new Square(i,j),ColorPiece.WHITE);
                }

                //Bisp
                if((i==0 && j==2) || (i==0 && j==5)){
                    board[i][j] = new Piece(NamePiece.B,new Square(i,j),ColorPiece.BLACK);
                }else if ((i==7 && j == 2) || (i==7 && j == 5)) {
                    board[i][j] = new Piece(NamePiece.B,new Square(i,j),ColorPiece.WHITE);
                }

                //Knith
                if((i==0 && j==1) || (i==0 && j==6)){
                    board[i][j] = new Piece(NamePiece.N,new Square(i,j),ColorPiece.BLACK);
                }else if ((i==7 && j == 1) || (i==7 && j == 6)) {
                    board[i][j] = new Piece(NamePiece.N,new Square(i,j),ColorPiece.WHITE);
                }

                //Rock
                if((i==0 && j==0) || (i==0 && j==7)){
                    board[i][j] = new Piece(NamePiece.R,new Square(i,j),ColorPiece.BLACK);
                }else if ((i==7 && j == 0) || (i==7 && j == 7)) {
                    board[i][j] = new Piece(NamePiece.R,new Square(i,j),ColorPiece.WHITE);
                }
            }
        }
    }
}
