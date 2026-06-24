package chess.model.pieces;

import java.util.List;

import chess.model.Alliance;
import chess.model.Board;
import chess.model.Position;

public abstract class Piece {
    private final Alliance alliance;
    private final String symbol;

    protected Piece(Alliance alliance, String symbol) {
        this.alliance = alliance;
        this.symbol = symbol;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public String getSymbol() {
        return symbol;
    }

    public abstract String getName();

    public abstract List<Position> getPseudoLegalMoves(Board board, Position from);

    public List<Position> getAttackSquares(Board board, Position from) {
        return getPseudoLegalMoves(board, from);
    }

    protected boolean isEnemy(Piece piece) {
        return piece != null && piece.getAlliance() != alliance;
    }

    protected boolean canOccupy(Piece piece) {
        return piece == null || isEnemy(piece);
    }

    @Override
    public String toString() {
        return getName();
    }
}
