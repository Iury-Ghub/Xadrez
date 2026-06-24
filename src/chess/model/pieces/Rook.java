package chess.model.pieces;

import java.util.List;

import chess.model.Alliance;
import chess.model.Board;
import chess.model.Position;

public final class Rook extends SlidingPiece {
    private static final int[][] DIRECTIONS = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };

    public Rook(Alliance alliance) {
        super(alliance, alliance == Alliance.WHITE ? "♖" : "♜");
    }

    @Override
    public String getName() {
        return "Torre";
    }

    @Override
    public List<Position> getPseudoLegalMoves(Board board, Position from) {
        return collectSlidingMoves(board, from, DIRECTIONS);
    }
}
