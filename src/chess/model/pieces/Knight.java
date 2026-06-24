package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.model.Alliance;
import chess.model.Board;
import chess.model.Position;

public final class Knight extends Piece {
    private static final int[][] OFFSETS = {
            {-2, -1},
            {-2, 1},
            {-1, -2},
            {-1, 2},
            {1, -2},
            {1, 2},
            {2, -1},
            {2, 1}
    };

    public Knight(Alliance alliance) {
        super(alliance, alliance == Alliance.WHITE ? "♘" : "♞");
    }

    @Override
    public String getName() {
        return "Cavalo";
    }

    @Override
    public List<Position> getPseudoLegalMoves(Board board, Position from) {
        List<Position> moves = new ArrayList<Position>();
        for (int i = 0; i < OFFSETS.length; i++) {
            int row = from.getRow() + OFFSETS[i][0];
            int col = from.getCol() + OFFSETS[i][1];
            if (!board.isInside(row, col)) {
                continue;
            }
            Position target = new Position(row, col);
            if (canOccupy(board.getPiece(target))) {
                moves.add(target);
            }
        }
        return moves;
    }
}
