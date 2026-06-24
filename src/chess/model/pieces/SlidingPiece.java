package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.model.Alliance;
import chess.model.Board;
import chess.model.Position;

public abstract class SlidingPiece extends Piece {
    protected SlidingPiece(Alliance alliance, String symbol) {
        super(alliance, symbol);
    }

    protected List<Position> collectSlidingMoves(Board board, Position from, int[][] directions) {
        List<Position> moves = new ArrayList<Position>();
        for (int i = 0; i < directions.length; i++) {
            int row = from.getRow() + directions[i][0];
            int col = from.getCol() + directions[i][1];
            while (board.isInside(row, col)) {
                Position target = new Position(row, col);
                Piece targetPiece = board.getPiece(target);
                if (targetPiece == null) {
                    moves.add(target);
                } else {
                    if (targetPiece.getAlliance() != getAlliance()) {
                        moves.add(target);
                    }
                    break;
                }
                row += directions[i][0];
                col += directions[i][1];
            }
        }
        return moves;
    }
}
