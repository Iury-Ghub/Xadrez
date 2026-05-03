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
                    board[i][j] = new Piece(NamePiece.K,new Square(i,j),ColorPiece.BLACK);
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
    public void viewGame(){
        int line = 8,cont = 0;
        for (Piece[] pieces : board) {
            for (Piece piece : pieces) {
                if(cont == 0){
                    System.out.print(line--+"|");
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
        System.out.println("   _  _  _  _  _  _  _  _");
        System.out.println("#  a  b  c  d  e  f  g  h");
        System.out.println();
    }

    public void MoveWhite(NamePiece namePiece, Square positionPiece, Square positionMove){
        if (Objects.requireNonNull(namePiece) == NamePiece.P) {
            pawnsMoveWhite(positionPiece, positionMove);
        }
    }

    public void pawnsMoveWhite(Square positionPiece, Square positionMove){
        int x1 = 8-positionPiece.getLine(), x2 = 8 -positionMove.getLine();
        int y1 = positionPiece.getColum(), y2 = positionMove.getColum();

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
                && (board[x2+1][y2-1] == board[x1][x2] || board[x2+1][y2+1] == board[x1][x2])) {
            Piece piece = board[x2][y2];
            board[x2][y2] = board[x1][y1];
            board[x1][y1] = null;
        }
    }
    public void pawnsMoveBlack(String position){
        int line = 8-(Integer.parseInt(position.substring(1,2)));
        int colum = position.charAt(0)-'a';
        //Pawns Move
        if (board[line][colum] == null) {
            if (board[line - 1][colum] != null && board[line - 1][colum].toString().equals("P") && board[line-1][colum].getColor() == ColorPiece.BLACK) {
                board[line][colum] = board[line + 1][colum];
                board[line - 1][colum] = null;
            } else if (line == 3 && board[1][colum] != null && board[1][colum].toString().equals("P") && board[2][colum] == null && board[1][colum].getColor() == ColorPiece.BLACK) {
                board[line][colum] = board[1][colum];
                board[1][colum] = null;
            }
        }
        //Pawns Capture In Passant
        if (board[line][colum] != null && board[line][colum].getColor() == ColorPiece.WHITE) {
            if (board[line - 1][colum - 1].toString().equals("P") && board[line -1][colum -1].getColor() == ColorPiece.BLACK) {
                board[line][colum] = board[line - 1][colum - 1];
                board[line - 1][colum - 1] = null;
            } else if (board[line - 1][colum + 1].toString().equals("P") && board[line -1][colum -1].getColor() == ColorPiece.BLACK) {
                board[line][colum] = board[line - 1][colum + 1];
                board[line - 1][colum + 1] = null;
            }
        }
    }

    public void resetGame(){}
}
