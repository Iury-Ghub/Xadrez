package chess;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JOptionPane;

import chess.controller.ChessController;
import chess.ui.ChessFrame;
import chess.util.DebugLog;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        DebugLog.operation("INICIALIZACAO", "Main.main");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DebugLog.operation("INICIALIZACAO", "Main.Runnable.run");
                String whiteName = JOptionPane.showInputDialog(null, "Nome do jogador (Brancas):", "Jogadores", JOptionPane.PLAIN_MESSAGE);
                if (whiteName == null || whiteName.trim().isEmpty()) {
                    whiteName = "Brancas";
                }
                String blackName = JOptionPane.showInputDialog(null, "Nome do adversário (Pretas):", "Jogadores", JOptionPane.PLAIN_MESSAGE);
                if (blackName == null || blackName.trim().isEmpty()) {
                    blackName = "Pretas";
                }
                ChessController controller = new ChessController();
                ChessFrame frame = new ChessFrame(controller, whiteName, blackName);
                frame.setVisible(true);
            }
        });
    }
}
