package xadrez.visao;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import xadrez.controlador.ControladorXadrez;
import xadrez.modelo.Alianca;
import xadrez.modelo.Posicao;
import xadrez.util.GerenciadorArquivoJogo;

public final class JanelaXadrez extends JFrame {
    private final ControladorXadrez controlador;
    private final PainelTabuleiro painelTabuleiro;
    private final JLabel rotuloTurno;
    private final JLabel rotuloStatus;
    private final JTextArea areaHistorico;
    private final JButton botaoReiniciar;
    private final JButton botaoSalvar;
    private final JButton botaoCarregar;
    private final String nomeJogadorBrancas;
    private final String nomeJogadorPretas;

    public JanelaXadrez(ControladorXadrez controladorXadrez, String nomeJogadorBrancas, String nomeJogadorPretas) {
        super("Xadrez");

        if (controladorXadrez == null) {
            throw new IllegalArgumentException("controlador não pode ser nulo");
        }

        this.controlador = controladorXadrez;
        this.nomeJogadorBrancas = (nomeJogadorBrancas != null && !nomeJogadorBrancas.trim().isEmpty()) ? nomeJogadorBrancas : "Brancas";
        this.nomeJogadorPretas = (nomeJogadorPretas != null && !nomeJogadorPretas.trim().isEmpty()) ? nomeJogadorPretas : "Pretas";
        this.rotuloTurno = new JLabel();
        this.rotuloStatus = new JLabel();
        this.areaHistorico = new JTextArea();
        this.botaoReiniciar = new JButton("Novo jogo");
        this.botaoSalvar = new JButton("Salvar");
        this.botaoCarregar = new JButton("Carregar");
        this.painelTabuleiro = new PainelTabuleiro(new OuvinteCasaClicada() {
            @Override
            public void aoClicarCasa(Posicao posicao) {
                controlador.tratarCliqueCasa(posicao);
                atualizarVisao();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        construirLayout(nomeJogadorBrancas, nomeJogadorPretas);
        pack();
        setLocationRelativeTo(null);
        atualizarVisao();
    }

    private void construirLayout(String nomeJogadorBrancas, String nomeJogadorPretas) {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(raiz);

        JPanel painelLateral = new JPanel();
        painelLateral.setLayout(new BoxLayout(painelLateral, BoxLayout.Y_AXIS));
        painelLateral.setPreferredSize(new Dimension(200, 640));

        JLabel rotuloJogadores = new JLabel(this.nomeJogadorBrancas + " vs " + this.nomeJogadorPretas);
        rotuloJogadores.setAlignmentX(Component.LEFT_ALIGNMENT);

        rotuloTurno.setFont(new Font("SansSerif", Font.BOLD, 14));
        rotuloTurno.setAlignmentX(Component.LEFT_ALIGNMENT);

        rotuloStatus.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rotuloHistorico = new JLabel("Histórico:");
        rotuloHistorico.setFont(new Font("SansSerif", Font.BOLD, 13));
        rotuloHistorico.setAlignmentX(Component.LEFT_ALIGNMENT);

        areaHistorico.setEditable(false);
        areaHistorico.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane rolagemHistorico = new JScrollPane(areaHistorico);
        rolagemHistorico.setAlignmentX(Component.LEFT_ALIGNMENT);
        rolagemHistorico.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        botaoReiniciar.setAlignmentX(Component.LEFT_ALIGNMENT);
        botaoReiniciar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        botaoReiniciar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controlador.tratarNovoJogo();
                atualizarVisao();
            }
        });

        botaoSalvar.setAlignmentX(Component.LEFT_ALIGNMENT);
        botaoSalvar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        botaoSalvar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    new GerenciadorArquivoJogo().salvarPartida(controlador.getHistoricoJogadas(), "partida.txt");
                    JOptionPane.showMessageDialog(null, "Partida salva.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao salvar: " + ex.getMessage());
                }
            }
        });

        botaoCarregar.setAlignmentX(Component.LEFT_ALIGNMENT);
        botaoCarregar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        botaoCarregar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    List<Posicao[]> jogadas = new GerenciadorArquivoJogo().carregarPartida("partida.txt");
                    controlador.tratarNovoJogo();
                    for (Posicao[] jogada : jogadas) {
                        controlador.aplicarJogada(jogada[0], jogada[1]);
                    }
                    atualizarVisao();
                    JOptionPane.showMessageDialog(null, "Partida carregada.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao carregar: " + ex.getMessage());
                }
            }
        });

        painelLateral.add(rotuloJogadores);
        painelLateral.add(Box.createVerticalStrut(8));
        painelLateral.add(rotuloTurno);
        painelLateral.add(Box.createVerticalStrut(4));
        painelLateral.add(rotuloStatus);
        painelLateral.add(Box.createVerticalStrut(12));
        painelLateral.add(rotuloHistorico);
        painelLateral.add(Box.createVerticalStrut(4));
        painelLateral.add(rolagemHistorico);
        painelLateral.add(Box.createVerticalGlue());
        painelLateral.add(botaoReiniciar);
        painelLateral.add(Box.createVerticalStrut(4));
        painelLateral.add(botaoSalvar);
        painelLateral.add(Box.createVerticalStrut(4));
        painelLateral.add(botaoCarregar);

        raiz.add(painelTabuleiro, BorderLayout.CENTER);
        raiz.add(painelLateral, BorderLayout.EAST);
    }

    private void atualizarVisao() {
        painelTabuleiro.renderizar(controlador.getTabuleiro(), controlador.getPosicaoSelecionada(), controlador.getJogadasLegaisSelecionadas());
        String nomeAtual = controlador.getTurnoAtual() == Alianca.BRANCAS ? nomeJogadorBrancas : nomeJogadorPretas;
        rotuloTurno.setText("Turno: " + nomeAtual);
        rotuloStatus.setText(controlador.getMensagemStatus());

        String historico = controlador.getHistoricoJogadasFormatado();
        areaHistorico.setText(historico.isEmpty() ? "Sem jogadas ainda." : historico);
        areaHistorico.setCaretPosition(areaHistorico.getDocument().getLength());
    }
}
