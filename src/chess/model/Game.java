package chess.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import chess.model.pieces.Pawn;
import chess.model.pieces.Piece;
import chess.model.pieces.Queen;
import chess.model.pieces.King;
import chess.model.pieces.Rook;
import chess.util.DebugLog;

public final class Game {
    private final Board board;
    private Alliance currentTurn;
    private Position selectedPosition;
    private List<Position> selectedLegalMoves;
    private List<MoveRecord> moveHistory;
    private GameState state;
    private String statusMessage;
    private boolean whiteCanCastleKingSide;
    private boolean whiteCanCastleQueenSide;
    private boolean blackCanCastleKingSide;
    private boolean blackCanCastleQueenSide;
    private Position enPassantTarget;
    private Alliance enPassantAvailableFor;

    public Game() {
        this.board = new Board();
        this.selectedLegalMoves = new ArrayList<Position>();
        this.moveHistory = new ArrayList<MoveRecord>();
        reset();
    }

    public void reset() {
        DebugLog.operation("JOGO", "Game.reset");
        board.reset();
        currentTurn = Alliance.WHITE;
        selectedPosition = null;
        selectedLegalMoves = new ArrayList<Position>();
        moveHistory = new ArrayList<MoveRecord>();
        whiteCanCastleKingSide = true;
        whiteCanCastleQueenSide = true;
        blackCanCastleKingSide = true;
        blackCanCastleQueenSide = true;
        enPassantTarget = null;
        enPassantAvailableFor = null;
        updateState();
    }

    public Board getBoard() {
        return board;
    }

    public Alliance getCurrentTurn() {
        return currentTurn;
    }

    public Position getSelectedPosition() {
        return selectedPosition;
    }

    public List<Position> getSelectedLegalMoves() {
        return Collections.unmodifiableList(selectedLegalMoves);
    }

    public GameState getState() {
        return state;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public List<MoveRecord> getMoveHistory() {
        return Collections.unmodifiableList(moveHistory);
    }

    public void click(Position position) {
        DebugLog.operation("JOGO", "Game.click: " + position);
        if (state == GameState.CHECKMATE || state == GameState.STALEMATE) {
            return;
        }

        Piece clickedPiece = board.getPiece(position);

        if (selectedPosition == null) {
            if (clickedPiece != null && clickedPiece.getAlliance() == currentTurn) {
                selectPiece(position);
            }
            return;
        }

        if (selectedPosition.equals(position)) {
            clearSelection();
            return;
        }

        if (selectedLegalMoves.contains(position)) {
            executeMove(position);
            return;
        }

        if (clickedPiece != null && clickedPiece.getAlliance() == currentTurn) {
            selectPiece(position);
        }
    }

    private void selectPiece(Position position) {
        selectedPosition = position;
        selectedLegalMoves = computeLegalMoves(position);
    }

    private void clearSelection() {
        selectedPosition = null;
        selectedLegalMoves = new ArrayList<Position>();
    }

    private void executeMove(Position target) {
        DebugLog.operation("JOGO", "Game.executeMove: " + selectedPosition + " -> " + target);
        int plyNumber = moveHistory.size() + 1;
        Alliance movingAlliance = currentTurn;
        Position from = selectedPosition;
        Piece movingPiece = board.getPiece(from);
        boolean enPassantMove = isEnPassantMove(movingPiece, from, target);
        Piece capturedPiece = applyMove(board, from, target, movingPiece, enPassantMove);
        boolean castlingMove = isCastlingMove(movingPiece, from, target);

        if (castlingMove) {
            moveCastlingRook(board, movingAlliance, target);
        }
        Piece resultingPiece = board.getPiece(target);

        updateCastlingRights(from, target, movingPiece, capturedPiece);
        updateEnPassantState(from, target, movingPiece, movingAlliance);

        if (movingPiece instanceof Pawn) {
            if (isPromotionSquare(target, movingPiece.getAlliance())) {
                board.setPiece(target, new Queen(movingPiece.getAlliance()));
                resultingPiece = board.getPiece(target);
            }
        }

        currentTurn = currentTurn.opposite();
        clearSelection();
        updateState();
        moveHistory.add(new MoveRecord(
                plyNumber,
                movingAlliance,
                from,
                target,
                movingPiece,
                capturedPiece,
                resultingPiece,
                state,
                capturedPiece == null ? null : (enPassantMove ? getEnPassantCapturedPosition(from, target) : target),
                enPassantMove));
    }

    private Piece applyMove(Board boardState, Position from, Position to, Piece movingPiece, boolean enPassantMove) {
        if (!enPassantMove) {
            return boardState.movePiece(from, to);
        }

        if (boardState.getPiece(to) != null) {
            throw new IllegalStateException("Invalid en passant destination " + to);
        }

        Position capturedPosition = getEnPassantCapturedPosition(from, to);
        Piece capturedPiece = boardState.getPiece(capturedPosition);
        if (!(capturedPiece instanceof Pawn) || capturedPiece.getAlliance() == movingPiece.getAlliance()) {
            throw new IllegalStateException("Invalid en passant capture from " + from + " to " + to);
        }

        boardState.movePiece(from, to);
        boardState.setPiece(capturedPosition, null);
        return capturedPiece;
    }

    private boolean isPromotionSquare(Position position, Alliance alliance) {
        return (alliance == Alliance.WHITE && position.getRow() == 0)
                || (alliance == Alliance.BLACK && position.getRow() == 7);
    }

    private boolean isCastlingMove(Piece movingPiece, Position from, Position to) {
        return movingPiece instanceof King
                && from != null
                && to != null
                && from.getRow() == to.getRow()
                && Math.abs(from.getCol() - to.getCol()) == 2;
    }

    private void moveCastlingRook(Board boardState, Alliance alliance, Position kingTarget) {
        Position rookFrom;
        Position rookTo;

        if (kingTarget.getCol() == 6) {
            rookFrom = alliance == Alliance.WHITE ? new Position(7, 7) : new Position(0, 7);
            rookTo = alliance == Alliance.WHITE ? new Position(7, 5) : new Position(0, 5);
        } else if (kingTarget.getCol() == 2) {
            rookFrom = alliance == Alliance.WHITE ? new Position(7, 0) : new Position(0, 0);
            rookTo = alliance == Alliance.WHITE ? new Position(7, 3) : new Position(0, 3);
        } else {
            throw new IllegalArgumentException("Invalid castling destination: " + kingTarget);
        }

        boardState.movePiece(rookFrom, rookTo);
    }

    private void updateCastlingRights(Position from, Position to, Piece movingPiece, Piece capturedPiece) {
        if (movingPiece instanceof King) {
            disableAllCastlingRights(movingPiece.getAlliance());
        } else if (movingPiece instanceof Rook) {
            disableRookCastlingRight(movingPiece.getAlliance(), from);
        }

        if (capturedPiece instanceof Rook) {
            disableRookCastlingRight(capturedPiece.getAlliance(), to);
        }
    }

    private void updateEnPassantState(Position from, Position to, Piece movingPiece, Alliance movingAlliance) {
        enPassantTarget = null;
        enPassantAvailableFor = null;

        if (movingPiece instanceof Pawn && Math.abs(from.getRow() - to.getRow()) == 2) {
            int middleRow = (from.getRow() + to.getRow()) / 2;
            enPassantTarget = new Position(middleRow, from.getCol());
            enPassantAvailableFor = movingAlliance.opposite();
        }
    }

    private void disableAllCastlingRights(Alliance alliance) {
        if (alliance == Alliance.WHITE) {
            whiteCanCastleKingSide = false;
            whiteCanCastleQueenSide = false;
        } else {
            blackCanCastleKingSide = false;
            blackCanCastleQueenSide = false;
        }
    }

    private void disableRookCastlingRight(Alliance alliance, Position square) {
        if (alliance == Alliance.WHITE) {
            if (isHomeRookSquare(square, Alliance.WHITE, true)) {
                whiteCanCastleKingSide = false;
            } else if (isHomeRookSquare(square, Alliance.WHITE, false)) {
                whiteCanCastleQueenSide = false;
            }
        } else {
            if (isHomeRookSquare(square, Alliance.BLACK, true)) {
                blackCanCastleKingSide = false;
            } else if (isHomeRookSquare(square, Alliance.BLACK, false)) {
                blackCanCastleQueenSide = false;
            }
        }
    }

    private boolean isHomeRookSquare(Position square, Alliance alliance, boolean kingSide) {
        if (square == null) {
            return false;
        }

        if (alliance == Alliance.WHITE) {
            return kingSide
                    ? square.getRow() == 7 && square.getCol() == 7
                    : square.getRow() == 7 && square.getCol() == 0;
        }

        return kingSide
                ? square.getRow() == 0 && square.getCol() == 7
                : square.getRow() == 0 && square.getCol() == 0;
    }

    private boolean canCastleKingSide(Alliance alliance) {
        return canCastle(alliance, true);
    }

    private boolean canCastleQueenSide(Alliance alliance) {
        return canCastle(alliance, false);
    }

    private boolean canCastle(Alliance alliance, boolean kingSide) {
        if (alliance == Alliance.WHITE) {
            if (kingSide && !whiteCanCastleKingSide) {
                return false;
            }
            if (!kingSide && !whiteCanCastleQueenSide) {
                return false;
            }
        } else {
            if (kingSide && !blackCanCastleKingSide) {
                return false;
            }
            if (!kingSide && !blackCanCastleQueenSide) {
                return false;
            }
        }

        Position kingFrom = getKingHomeSquare(alliance);
        Position rookFrom = getRookHomeSquare(alliance, kingSide);
        Position throughSquare = kingSide ? getKingSideThroughSquare(alliance) : getQueenSideThroughSquare(alliance);
        Position destination = kingSide ? getKingSideDestinationSquare(alliance) : getQueenSideDestinationSquare(alliance);
        Alliance enemy = alliance.opposite();

        Piece kingPiece = board.getPiece(kingFrom);
        Piece rookPiece = board.getPiece(rookFrom);
        if (!(kingPiece instanceof King) || kingPiece.getAlliance() != alliance) {
            return false;
        }
        if (!(rookPiece instanceof Rook) || rookPiece.getAlliance() != alliance) {
            return false;
        }

        if (isInCheck(alliance)) {
            return false;
        }

        if (!isSquareEmpty(throughSquare) || !isSquareEmpty(destination)) {
            return false;
        }

        if (!kingSide && !isSquareEmpty(getQueenSideExtraSquare(alliance))) {
            return false;
        }

        if (board.isSquareAttacked(throughSquare, enemy)) {
            return false;
        }

        return true;
    }

    private Position getKingHomeSquare(Alliance alliance) {
        return alliance == Alliance.WHITE ? new Position(7, 4) : new Position(0, 4);
    }

    private Position getRookHomeSquare(Alliance alliance, boolean kingSide) {
        if (alliance == Alliance.WHITE) {
            return kingSide ? new Position(7, 7) : new Position(7, 0);
        }
        return kingSide ? new Position(0, 7) : new Position(0, 0);
    }

    private Position getKingSideThroughSquare(Alliance alliance) {
        return alliance == Alliance.WHITE ? new Position(7, 5) : new Position(0, 5);
    }

    private Position getKingSideDestinationSquare(Alliance alliance) {
        return alliance == Alliance.WHITE ? new Position(7, 6) : new Position(0, 6);
    }

    private Position getQueenSideThroughSquare(Alliance alliance) {
        return alliance == Alliance.WHITE ? new Position(7, 3) : new Position(0, 3);
    }

    private Position getQueenSideDestinationSquare(Alliance alliance) {
        return alliance == Alliance.WHITE ? new Position(7, 2) : new Position(0, 2);
    }

    private Position getQueenSideExtraSquare(Alliance alliance) {
        return alliance == Alliance.WHITE ? new Position(7, 1) : new Position(0, 1);
    }

    private boolean isSquareEmpty(Position square) {
        return board.getPiece(square) == null;
    }

    private boolean isEnPassantMove(Piece movingPiece, Position from, Position to) {
        if (!(movingPiece instanceof Pawn) || from == null || to == null || enPassantTarget == null) {
            return false;
        }

        if (movingPiece.getAlliance() != enPassantAvailableFor || !to.equals(enPassantTarget)) {
            return false;
        }

        if (board.getPiece(to) != null) {
            return false;
        }

        if (Math.abs(from.getCol() - to.getCol()) != 1) {
            return false;
        }

        int direction = movingPiece.getAlliance() == Alliance.WHITE ? -1 : 1;
        if (from.getRow() != to.getRow() - direction) {
            return false;
        }

        Position capturedPosition = getEnPassantCapturedPosition(from, to);
        Piece capturedPiece = board.getPiece(capturedPosition);
        return capturedPiece instanceof Pawn && capturedPiece.getAlliance() != movingPiece.getAlliance();
    }

    private Position getEnPassantCapturedPosition(Position from, Position to) {
        return new Position(from.getRow(), to.getCol());
    }

    private List<Position> computeLegalMoves(Position from) {
        Piece piece = board.getPiece(from);

        if (piece == null || piece.getAlliance() != currentTurn) {
            return Collections.emptyList();
        }

        List<Position> pseudoMoves = new ArrayList<Position>(piece.getPseudoLegalMoves(board, from));
        if (piece instanceof King) {
            pseudoMoves.addAll(collectCastlingMoves(piece.getAlliance(), from));
        }
        if (piece instanceof Pawn) {
            pseudoMoves.addAll(collectEnPassantMoves(piece, from));
        }
        List<Position> legalMoves = new ArrayList<Position>();

        for (Position target : pseudoMoves) {
            Piece targetPiece = board.getPiece(target);
            if (targetPiece instanceof King) {
                continue;
            }

            Board snapshot = board.copy();
            applyMove(snapshot, from, target, piece, isEnPassantMove(piece, from, target));
            if (isCastlingMove(piece, from, target)) {
                moveCastlingRook(snapshot, piece.getAlliance(), target);
            }

            Position kingPosition = snapshot.findKing(piece.getAlliance());
            if (kingPosition == null) {
                continue;
            }

            if (!snapshot.isSquareAttacked(kingPosition, piece.getAlliance().opposite())) {
                legalMoves.add(target);
            }
        }

        return legalMoves;
    }

    private List<Position> collectEnPassantMoves(Piece piece, Position from) {
        List<Position> moves = new ArrayList<Position>();
        if (!(piece instanceof Pawn) || enPassantTarget == null || enPassantAvailableFor != piece.getAlliance()) {
            return moves;
        }

        int direction = piece.getAlliance() == Alliance.WHITE ? -1 : 1;
        if (from.getRow() != enPassantTarget.getRow() - direction) {
            return moves;
        }

        if (Math.abs(from.getCol() - enPassantTarget.getCol()) != 1) {
            return moves;
        }

        Position capturedPosition = getEnPassantCapturedPosition(from, enPassantTarget);
        Piece capturedPiece = board.getPiece(capturedPosition);
        if (!(capturedPiece instanceof Pawn) || capturedPiece.getAlliance() == piece.getAlliance()) {
            return moves;
        }

        if (board.getPiece(enPassantTarget) != null) {
            return moves;
        }

        moves.add(enPassantTarget);
        return moves;
    }

    private List<Position> collectCastlingMoves(Alliance alliance, Position from) {
        List<Position> moves = new ArrayList<Position>();

        if (alliance == Alliance.WHITE && from.equals(new Position(7, 4))) {
            if (canCastleKingSide(Alliance.WHITE)) {
                moves.add(new Position(7, 6)); // g1
            }
            if (canCastleQueenSide(Alliance.WHITE)) {
                moves.add(new Position(7, 2)); // c1
            }
        }

        if (alliance == Alliance.BLACK && from.equals(new Position(0, 4))) {
            if (canCastleKingSide(Alliance.BLACK)) {
                moves.add(new Position(0, 6)); // g8
            }
            if (canCastleQueenSide(Alliance.BLACK)) {
                moves.add(new Position(0, 2)); // c8
            }
        }

        return moves;
    }

    private void updateState() {
        DebugLog.operation("JOGO", "Game.updateState");
        boolean inCheck = isInCheck(currentTurn);
        boolean hasMove = hasAnyLegalMove();

        if (inCheck && !hasMove) {
            state = GameState.CHECKMATE;
            statusMessage = "Xeque-mate. " + currentTurn.opposite().getDisplayName() + " venceram.";
            return;
        }

        if (!inCheck && !hasMove) {
            state = GameState.STALEMATE;
            statusMessage = "Empate por afogamento.";
            return;
        }

        if (inCheck) {
            state = GameState.CHECK;
            statusMessage = currentTurn.getDisplayName() + " estao em xeque.";
            return;
        }

        state = GameState.ACTIVE;
        statusMessage = "Vez das " + currentTurn.getDisplayName().toLowerCase(Locale.ROOT) + ".";
    }

    private boolean hasAnyLegalMove() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getAlliance() == currentTurn) {
                    List<Position> moves = computeLegalMoves(new Position(row, col));
                    if (!moves.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isInCheck(Alliance alliance) {
        Position kingPosition = board.findKing(alliance);
        if (kingPosition == null) {
            return true;
        }
        return board.isSquareAttacked(kingPosition, alliance.opposite());
    }
}
