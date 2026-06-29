package xadrez;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JOptionPane;

import xadrez.controlador.ControladorXadrez;
import xadrez.visao.JanelaXadrez;
import xadrez.util.LogDepuracao;

public final class Principal {
    private Principal() {
    }

    public static void main(String[] args) {
        LogDepuracao.operacao("INICIALIZACAO", "Principal.main");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignorado) {
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LogDepuracao.operacao("INICIALIZACAO", "Principal.Runnable.run");
                String nomeBrancas = JOptionPane.showInputDialog(null, "Nome do jogador (Brancas):", "Jogadores", JOptionPane.PLAIN_MESSAGE);
                if (nomeBrancas == null || nomeBrancas.trim().isEmpty()) {
                    nomeBrancas = "Brancas";
                }
                String nomePretas = JOptionPane.showInputDialog(null, "Nome do adversário (Pretas):", "Jogadores", JOptionPane.PLAIN_MESSAGE);
                if (nomePretas == null || nomePretas.trim().isEmpty()) {
                    nomePretas = "Pretas";
                }
                ControladorXadrez controlador = new ControladorXadrez();
                JanelaXadrez janela = new JanelaXadrez(controlador, nomeBrancas, nomePretas);
                janela.setVisible(true);
            }
        });
    }
}
