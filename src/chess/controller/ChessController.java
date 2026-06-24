package chess.controller;

import java.util.List;

import chess.model.Alliance;
import chess.model.Board;
import chess.model.Game;
import chess.model.GameState;
import chess.model.MoveNotationFormatter;
import chess.model.MoveRecord;
import chess.model.Position;
import chess.model.pieces.Piece;
import chess.util.DebugLog;

public final class ChessController {
    private final Game game;
    private final MoveNotationFormatter notationFormatter;

    public ChessController() {
        this.game = new Game();
        this.notationFormatter = new MoveNotationFormatter();
    }

    public void handleSquareClick(Position position) {
        DebugLog.operation("ACOES", "ChessController.handleSquareClick: " + position);
        if (position == null) {
            return;
        }
        game.click(position);
    }

    public void handleNewGame() {
        DebugLog.operation("ACOES", "ChessController.handleNewGame");
        game.reset();
    }

    public void applyMove(Position from, Position to) {
        game.click(from);
        game.click(to);
    }

    public Board getBoard() {
        return game.getBoard();
    }

    public Alliance getCurrentTurn() {
        return game.getCurrentTurn();
    }

    public Position getSelectedPosition() {
        return game.getSelectedPosition();
    }

    public List<Position> getSelectedLegalMoves() {
        return game.getSelectedLegalMoves();
    }

    public GameState getState() {
        return game.getState();
    }

    public String getStatusMessage() {
        return game.getStatusMessage();
    }

    public List<MoveRecord> getMoveHistory() {
        return game.getMoveHistory();
    }

    public String getFormattedMoveHistory() {
        return notationFormatter.formatHistory(game.getMoveHistory());
    }

    public Piece getSelectedPiece() {
        Position selectedPosition = game.getSelectedPosition();
        if (selectedPosition == null) {
            return null;
        }
        return game.getBoard().getPiece(selectedPosition);
    }
}
