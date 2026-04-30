package org.example.Controller;

import org.example.Enums.ColorPiece;
import org.example.Enums.NamePiece;
import org.example.model.Piece;
import org.example.model.SquarePiece;

import java.util.Objects;

public class ChessGame {
    Piece[][] board = new Piece[8][8];

    public ChessGame() {
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[i].length;j++){

                //Pawns
                if(i==1){
                    board[i][j] = new Piece(NamePiece.P,ColorPiece.BLACK);
                } else if (i==6) {
                    board[i][j] = new Piece(NamePiece.P,ColorPiece.WHITE);
                }

                //Kings
                if(i==0 && j==4){
                    board[i][j] = new Piece(NamePiece.K,ColorPiece.BLACK);
                } else if (i==7 && j==3) {
                    board[i][j] = new Piece(NamePiece.K,ColorPiece.BLACK);
                }

                //Queen
                if(i==0 && j==3){
                    board[i][j] = new Piece(NamePiece.Q,ColorPiece.BLACK);
                } else if (i==7 && j == 4) {
                    board[i][j] = new Piece(NamePiece.Q,ColorPiece.WHITE);
                }

                //Bisp
                if((i==0 && j==2) || (i==0 && j==5)){
                    board[i][j] = new Piece(NamePiece.B,ColorPiece.BLACK);
                }else if ((i==7 && j == 2) || (i==7 && j == 5)) {
                    board[i][j] = new Piece(NamePiece.B,ColorPiece.WHITE);
                }

                //Knith
                if((i==0 && j==1) || (i==0 && j==6)){
                    board[i][j] = new Piece(NamePiece.N,ColorPiece.BLACK);
                }else if ((i==7 && j == 1) || (i==7 && j == 6)) {
                    board[i][j] = new Piece(NamePiece.N,ColorPiece.WHITE);
                }

                //Rock
                if((i==0 && j==0) || (i==0 && j==7)){
                    board[i][j] = new Piece(NamePiece.R,ColorPiece.BLACK);
                }else if ((i==7 && j == 0) || (i==7 && j == 7)) {
                    board[i][j] = new Piece(NamePiece.R,ColorPiece.WHITE);
                }
            }
        }
    }
    public void viewGame(){
        int line = 8,cont = 0;
        for (Piece[] pieces : board) {
            for (Piece piece : pieces) {
                if(cont == 0){
                    System.out.print(line--+" ");
                }
                cont ++;
                if (piece == null) {
                    System.out.print(" . ");
                } else {
                    System.out.print(" "+piece+" ");
                }
            }
            System.out.println();
            cont = 0;
        }
        System.out.println("#  a  b  c  d  e  f  g  h");
        System.out.println();
    }

    public void MoveWhite(String position){
        int line = 8-(Integer.parseInt(position.substring(1,2)));
        int colum = position.charAt(0)-'a';
        
        //Pawns
        if(board[line][colum] == null){
            if(board[line+1][colum].toString().equals("P")){
                board[line][colum] = board[line+1][colum];
                board[line+1][colum] = null;
            }
        }
    }
    public void resetGame(){}
}
