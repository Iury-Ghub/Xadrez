package chess.model;

import java.util.List;

import chess.model.pieces.Bishop;
import chess.model.pieces.Knight;
import chess.model.pieces.Pawn;
import chess.model.pieces.Piece;
import chess.model.pieces.Queen;
import chess.model.pieces.King;
import chess.model.pieces.Rook;

public final class MoveNotationFormatter {
    public String formatMove(MoveRecord record) {
        if (record == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        Piece movedPiece = record.getMovedPiece();
        Position from = record.getFrom();
        Position to = record.getTo();

        if (record.isCastling()) {
            if (to.getCol() > from.getCol()) {
                builder.append("O-O");
            } else {
                builder.append("O-O-O");
            }
        } else if (movedPiece instanceof Pawn) {
            if (record.isCapture()) {
                builder.append(fileLetter(from)).append('x').append(to.toAlgebraic());
            } else {
                builder.append(to.toAlgebraic());
            }
        } else {
            builder.append(pieceLetter(movedPiece));
            if (record.isCapture()) {
                builder.append('x');
            }
            builder.append(to.toAlgebraic());
        }

        if (record.isPromotion()) {
            builder.append('=').append(pieceLetter(record.getResultingPiece()));
        }

        if (record.getResultingState() == GameState.CHECKMATE) {
            builder.append('#');
        } else if (record.getResultingState() == GameState.CHECK) {
            builder.append('+');
        }

        return builder.toString();
    }

    public String formatHistory(List<MoveRecord> history) {
        if (history == null || history.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (MoveRecord record : history) {
            if (record.getPlyNumber() % 2 == 1) {
                if (builder.length() > 0) {
                    builder.append('\n');
                }
                int moveNumber = (record.getPlyNumber() + 1) / 2;
                builder.append(moveNumber).append(". ").append(formatMove(record));
            } else {
                builder.append(' ').append(formatMove(record));
            }
        }
        return builder.toString();
    }

    private String pieceLetter(Piece piece) {
        if (piece instanceof Knight) {
            return "N";
        }
        if (piece instanceof Bishop) {
            return "B";
        }
        if (piece instanceof Rook) {
            return "R";
        }
        if (piece instanceof Queen) {
            return "Q";
        }
        if (piece instanceof King) {
            return "K";
        }
        return "";
    }

    private char fileLetter(Position position) {
        return (char) ('a' + position.getCol());
    }
}
