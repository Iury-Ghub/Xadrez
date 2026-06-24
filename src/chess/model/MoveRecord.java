package chess.model;

import chess.model.pieces.Piece;
import chess.model.pieces.King;

public final class MoveRecord {
    private final int plyNumber;
    private final Alliance alliance;
    private final Position from;
    private final Position to;
    private final Piece movedPiece;
    private final Piece capturedPiece;
    private final Piece resultingPiece;
    private final GameState resultingState;
    private final Position capturedPosition;
    private final boolean enPassant;

    public MoveRecord(int plyNumber,
                      Alliance alliance,
                      Position from,
                      Position to,
                      Piece movedPiece,
                      Piece capturedPiece,
                      Piece resultingPiece,
                      GameState resultingState,
                      Position capturedPosition,
                      boolean enPassant) {
        this.plyNumber = plyNumber;
        this.alliance = alliance;
        this.from = from;
        this.to = to;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.resultingPiece = resultingPiece;
        this.resultingState = resultingState;
        this.capturedPosition = capturedPosition;
        this.enPassant = enPassant;
    }

    public int getPlyNumber() {
        return plyNumber;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public Piece getResultingPiece() {
        return resultingPiece;
    }

    public GameState getResultingState() {
        return resultingState;
    }

    public boolean isCapture() {
        return capturedPiece != null;
    }

    public boolean isPromotion() {
        return resultingPiece != null
                && movedPiece != null
                && !resultingPiece.getClass().equals(movedPiece.getClass());
    }

    public boolean isCastling() {
        return movedPiece instanceof King
                && from != null
                && to != null
                && from.getRow() == to.getRow()
                && Math.abs(from.getCol() - to.getCol()) == 2;
    }
}
