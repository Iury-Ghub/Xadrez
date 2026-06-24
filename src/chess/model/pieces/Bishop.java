package chess.model.pieces;

import java.util.List;

import chess.model.Alliance;
import chess.model.Board;
import chess.model.Position;

public final class Bishop extends SlidingPiece {
    private static final int[][] DIRECTIONS = {
            {-1, -1},
            {-1, 1},
            {1, -1},
            {1, 1}
    };

    public Bishop(Alliance alliance) {
        super(alliance, alliance == Alliance.WHITE ? "♗" : "♝");
    }

    @Override
    public String getName() {
        return "Bispo";
    }

    @Override
    public List<Position> getPseudoLegalMoves(Board board, Position from) {
        return collectSlidingMoves(board, from, DIRECTIONS);
    }
}
