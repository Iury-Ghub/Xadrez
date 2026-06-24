package chess.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;

import chess.model.Alliance;
import chess.model.Board;
import chess.model.Position;
import chess.model.pieces.Piece;

public final class ChessBoardPanel extends JPanel {
    private static final Color LIGHT_SQUARE = new Color(255, 185, 93);
    private static final Color DARK_SQUARE = new Color(85, 47, 0);
    private static final Color SELECTED_SQUARE = new Color(90, 130, 90);
    private static final Color MOVE_SQUARE = new Color(180, 170, 60);
    private static final Color WHITE_PIECE_COLOR = new Color(250, 250, 250);
    private static final Color BLACK_PIECE_COLOR = new Color(15, 15, 15);
    private static final Font PIECE_FONT = new Font("Serif", Font.BOLD, 34);

    private final JButton[][] buttons;
    private final SquareClickListener listener;

    public ChessBoardPanel(SquareClickListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }

        this.listener = listener;
        this.buttons = new JButton[8][8];
        setLayout(new java.awt.GridLayout(8, 8));
        setPreferredSize(new Dimension(640, 640));
        buildButtons();
    }

    public void render(Board board, Position selected, List<Position> legalMoves) {
        Set<Position> legalSet = new HashSet<Position>(legalMoves);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position position = new Position(row, col);
                Piece piece = board.getPiece(position);
                JButton button = buttons[row][col];

                button.setText(piece == null ? "" : piece.getSymbol());
                boolean isWhitePiece = piece != null && piece.getAlliance() == Alliance.WHITE;
                button.setForeground(isWhitePiece ? WHITE_PIECE_COLOR : BLACK_PIECE_COLOR);

                Color background = isLightSquare(row, col) ? LIGHT_SQUARE : DARK_SQUARE;
                if (selected != null && selected.equals(position)) {
                    background = SELECTED_SQUARE;
                } else if (legalSet.contains(position)) {
                    background = MOVE_SQUARE;
                }

                button.setBackground(background);
            }
        }
    }

    private void buildButtons() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                final int buttonRow = row;
                final int buttonCol = col;
                JButton button = new JButton();
                button.setFont(PIECE_FONT);
                button.setMargin(new Insets(0, 0, 0, 0));
                button.setFocusPainted(false);
                button.setContentAreaFilled(false);
                button.setOpaque(true);
                button.setBorderPainted(false);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                button.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        listener.onSquareClicked(new Position(buttonRow, buttonCol));
                    }
                });
                buttons[row][col] = button;
                add(button);
            }
        }
    }

    private boolean isLightSquare(int row, int col) {
        return (row + col) % 2 != 0;
    }
}
