package chess.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import chess.controller.ChessController;
import chess.model.Alliance;
import chess.model.Position;
import chess.util.GameFileManeger;

public final class ChessFrame extends JFrame {
    private final ChessController controller;
    private final ChessBoardPanel boardPanel;
    private final JLabel turnLabel;
    private final JLabel statusLabel;
    private final JTextArea historyArea;
    private final JButton resetButton;
    private final JButton saveButton;
    private final JButton loadButton;
    private final String whitePlayerName;
    private final String blackPlayerName;

    public ChessFrame(ChessController chessController, String whitePlayerName, String blackPlayerName) {
        super("Xadrez");

        if (chessController == null) {
            throw new IllegalArgumentException("controller must not be null");
        }

        this.controller = chessController;
        this.whitePlayerName = (whitePlayerName != null && !whitePlayerName.trim().isEmpty()) ? whitePlayerName : "Brancas";
        this.blackPlayerName = (blackPlayerName != null && !blackPlayerName.trim().isEmpty()) ? blackPlayerName : "Pretas";
        this.turnLabel = new JLabel();
        this.statusLabel = new JLabel();
        this.historyArea = new JTextArea();
        this.resetButton = new JButton("Novo jogo");
        this.saveButton = new JButton("Salvar");
        this.loadButton = new JButton("Carregar");
        this.boardPanel = new ChessBoardPanel(new SquareClickListener() {
            @Override
            public void onSquareClicked(Position position) {
                controller.handleSquareClick(position);
                refreshView();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        buildLayout(whitePlayerName, blackPlayerName);
        pack();
        setLocationRelativeTo(null);
        refreshView();
    }

    private void buildLayout(String whitePlayerName, String blackPlayerName) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(root);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(200, 640));

        JLabel playersLabel = new JLabel(this.whitePlayerName + " vs " + this.blackPlayerName);
        playersLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        turnLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        turnLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel historyLabel = new JLabel("Historico:");
        historyLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        historyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        historyScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        resetButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.handleNewGame();
                refreshView();
            }
        });

        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    new GameFileManeger().salvarPartida(controller.getMoveHistory(), "partida.txt");
                    JOptionPane.showMessageDialog(null, "Partida salva.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao salvar: " + ex.getMessage());
                }
            }
        });

        loadButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loadButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    List<Position[]> moves = new GameFileManeger().carregarPartida("partida.txt");
                    controller.handleNewGame();
                    for (Position[] move : moves) {
                        controller.applyMove(move[0], move[1]);
                    }
                    refreshView();
                    JOptionPane.showMessageDialog(null, "Partida carregada.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao carregar: " + ex.getMessage());
                }
            }
        });

        sidePanel.add(playersLabel);
        sidePanel.add(Box.createVerticalStrut(8));
        sidePanel.add(turnLabel);
        sidePanel.add(Box.createVerticalStrut(4));
        sidePanel.add(statusLabel);
        sidePanel.add(Box.createVerticalStrut(12));
        sidePanel.add(historyLabel);
        sidePanel.add(Box.createVerticalStrut(4));
        sidePanel.add(historyScroll);
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(resetButton);
        sidePanel.add(Box.createVerticalStrut(4));
        sidePanel.add(saveButton);
        sidePanel.add(Box.createVerticalStrut(4));
        sidePanel.add(loadButton);

        root.add(boardPanel, BorderLayout.CENTER);
        root.add(sidePanel, BorderLayout.EAST);
    }

    private void refreshView() {
        boardPanel.render(controller.getBoard(), controller.getSelectedPosition(), controller.getSelectedLegalMoves());
        String currentName = controller.getCurrentTurn() == Alliance.WHITE ? whitePlayerName : blackPlayerName;
        turnLabel.setText("Turno: " + currentName);
        statusLabel.setText(controller.getStatusMessage());

        String history = controller.getFormattedMoveHistory();
        historyArea.setText(history.isEmpty() ? "Sem jogadas ainda." : history);
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }
}
