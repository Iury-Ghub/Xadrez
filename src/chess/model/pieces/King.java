package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.model.Alliance;
import chess.model.Board;
import chess.model.Position;

public final class King extends Piece {
    public King(Alliance alliance) {
        super(alliance, alliance == Alliance.WHITE ? "♔" : "♚");
    }

    @Override
    public String getName() {
        return "Rei";
    }

    @Override
    public List<Position> getPseudoLegalMoves(Board board, Position from) {
        List<Position> moves = new ArrayList<Position>();
        for (int rowDelta = -1; rowDelta <= 1; rowDelta++) {
            for (int colDelta = -1; colDelta <= 1; colDelta++) {
                if (rowDelta == 0 && colDelta == 0) {
                    continue;
                }
                int row = from.getRow() + rowDelta;
                int col = from.getCol() + colDelta;
                if (!board.isInside(row, col)) {
                    continue;
                }
                Position target = new Position(row, col);
                if (canOccupy(board.getPiece(target))) {
                    moves.add(target);
                }
            }
        }
        return moves;
    }
}
