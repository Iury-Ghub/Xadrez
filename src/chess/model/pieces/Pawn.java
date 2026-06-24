package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.model.Alliance;
import chess.model.Board;
import chess.model.Position;

public final class Pawn extends Piece {
    public Pawn(Alliance alliance) {
        super(alliance, alliance == Alliance.WHITE ? "♙" : "♟");
    }

    @Override
    public String getName() {
        return "Peão";
    }

    @Override
    public List<Position> getPseudoLegalMoves(Board board, Position from) {
        List<Position> moves = new ArrayList<Position>();
        int direction = getAlliance() == Alliance.WHITE ? -1 : 1;
        int startRow = getAlliance() == Alliance.WHITE ? 6 : 1;

        int oneStepRow = from.getRow() + direction;
        if (board.isInside(oneStepRow, from.getCol()) && board.getPiece(oneStepRow, from.getCol()) == null) {
            moves.add(new Position(oneStepRow, from.getCol()));

            int twoStepRow = from.getRow() + (2 * direction);
            if (from.getRow() == startRow
                    && board.isInside(twoStepRow, from.getCol())
                    && board.getPiece(twoStepRow, from.getCol()) == null) {
                moves.add(new Position(twoStepRow, from.getCol()));
            }
        }

        addCaptureMove(board, from, moves, direction, -1);
        addCaptureMove(board, from, moves, direction, 1);
        return moves;
    }

    @Override
    public List<Position> getAttackSquares(Board board, Position from) {
        List<Position> attacks = new ArrayList<Position>();
        int direction = getAlliance() == Alliance.WHITE ? -1 : 1;
        addAttackSquare(board, from, attacks, direction, -1);
        addAttackSquare(board, from, attacks, direction, 1);
        return attacks;
    }

    private void addCaptureMove(Board board, Position from, List<Position> moves, int direction, int colDelta) {
        int row = from.getRow() + direction;
        int col = from.getCol() + colDelta;
        if (!board.isInside(row, col)) {
            return;
        }
        Position target = new Position(row, col);
        Piece piece = board.getPiece(target);
        if (piece != null && piece.getAlliance() != getAlliance()) {
            moves.add(target);
        }
    }

    private void addAttackSquare(Board board, Position from, List<Position> attacks, int direction, int colDelta) {
        int row = from.getRow() + direction;
        int col = from.getCol() + colDelta;
        if (board.isInside(row, col)) {
            attacks.add(new Position(row, col));
        }
    }
}
