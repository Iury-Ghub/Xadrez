package chess.model;

import java.util.Arrays;

import chess.model.pieces.Bishop;
import chess.model.pieces.Knight;
import chess.model.pieces.Pawn;
import chess.model.pieces.Piece;
import chess.model.pieces.Queen;
import chess.model.pieces.King;
import chess.model.pieces.Rook;
import chess.util.DebugLog;

public final class Board {
    private final Piece[][] squares;

    public Board() {
        this.squares = new Piece[8][8];
    }

    public void reset() {
        DebugLog.operation("TABULEIRO", "Board.reset");
        setupInitialPosition();
    }

    public Board copy() {
        Board copy = new Board();
        for (int row = 0; row < 8; row++) {
            System.arraycopy(this.squares[row], 0, copy.squares[row], 0, 8);
        }
        return copy;
    }

    public void clear() {
        for (Piece[] row : squares) {
            Arrays.fill(row, null);
        }
    }

    public void setupInitialPosition() {
        clear();
        placeBackRank(0, Alliance.BLACK);
        for (int col = 0; col < 8; col++) {
            squares[1][col] = new Pawn(Alliance.BLACK);
            squares[6][col] = new Pawn(Alliance.WHITE);
        }
        placeBackRank(7, Alliance.WHITE);
    }

    public Piece getPiece(Position position) {
        if (position == null || !isInside(position)) {
            return null;
        }
        return squares[position.getRow()][position.getCol()];
    }

    public Piece getPiece(int row, int col) {
        if (!isInside(row, col)) {
            return null;
        }
        return squares[row][col];
    }

    public void setPiece(Position position, Piece piece) {
        if (position == null || !isInside(position)) {
            throw new IllegalArgumentException("Position outside board");
        }
        squares[position.getRow()][position.getCol()] = piece;
    }

    public Piece movePiece(Position from, Position to) {
        DebugLog.operation("TABULEIRO", "Board.movePiece: " + from + " -> " + to);
        Piece movingPiece = getPiece(from);
        if (movingPiece == null) {
            throw new IllegalStateException("No piece at source position " + from);
        }
        Piece capturedPiece = getPiece(to);
        setPiece(to, movingPiece);
        setPiece(from, null);
        return capturedPiece;
    }

    public boolean isInside(Position position) {
        return position != null && isInside(position.getRow(), position.getCol());
    }

    public boolean isInside(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public Position findKing(Alliance alliance) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = squares[row][col];
                if (piece instanceof King && piece.getAlliance() == alliance) {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }

    public boolean isSquareAttacked(Position target, Alliance byAlliance) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = squares[row][col];
                if (piece != null && piece.getAlliance() == byAlliance) {
                    Position from = new Position(row, col);
                    if (piece.getAttackSquares(this, from).contains(target)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void placeBackRank(int row, Alliance alliance) {
        squares[row][0] = new Rook(alliance);
        squares[row][1] = new Knight(alliance);
        squares[row][2] = new Bishop(alliance);
        squares[row][3] = new Queen(alliance);
        squares[row][4] = new King(alliance);
        squares[row][5] = new Bishop(alliance);
        squares[row][6] = new Knight(alliance);
        squares[row][7] = new Rook(alliance);
    }
}
