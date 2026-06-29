package xadrez.visao;

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

import xadrez.modelo.Alianca;
import xadrez.modelo.Tabuleiro;
import xadrez.modelo.Posicao;
import xadrez.modelo.pecas.Peca;

public final class PainelTabuleiro extends JPanel {
    private static final Color CASA_CLARA = new Color(255, 185, 93);
    private static final Color CASA_ESCURA = new Color(85, 47, 0);
    private static final Color CASA_SELECIONADA = new Color(90, 130, 90);
    private static final Color CASA_JOGADA = new Color(180, 170, 60);
    private static final Color COR_PECA_BRANCA = new Color(250, 250, 250);
    private static final Color COR_PECA_PRETA = new Color(15, 15, 15);
    private static final Font FONTE_PECA = new Font("Serif", Font.BOLD, 34);

    private final JButton[][] botoes;
    private final OuvinteCasaClicada ouvinte;

    public PainelTabuleiro(OuvinteCasaClicada ouvinte) {
        if (ouvinte == null) {
            throw new IllegalArgumentException("ouvinte não pode ser nulo");
        }

        this.ouvinte = ouvinte;
        this.botoes = new JButton[8][8];
        setLayout(new java.awt.GridLayout(8, 8));
        setPreferredSize(new Dimension(640, 640));
        construirBotoes();
    }

    public void renderizar(Tabuleiro tabuleiro, Posicao selecionada, List<Posicao> jogadasLegais) {
        Set<Posicao> conjuntoLegais = new HashSet<Posicao>(jogadasLegais);

        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                Posicao posicao = new Posicao(linha, coluna);
                Peca peca = tabuleiro.getPeca(posicao);
                JButton botao = botoes[linha][coluna];

                botao.setText(peca == null ? "" : peca.getSimbolo());
                boolean ehPecaBranca = peca != null && peca.getAlianca() == Alianca.BRANCAS;
                botao.setForeground(ehPecaBranca ? COR_PECA_BRANCA : COR_PECA_PRETA);

                Color fundoCor = ehCasaClara(linha, coluna) ? CASA_CLARA : CASA_ESCURA;
                if (selecionada != null && selecionada.equals(posicao)) {
                    fundoCor = CASA_SELECIONADA;
                } else if (conjuntoLegais.contains(posicao)) {
                    fundoCor = CASA_JOGADA;
                }

                botao.setBackground(fundoCor);
            }
        }
    }

    private void construirBotoes() {
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                final int linhaBotao = linha;
                final int colunaBotao = coluna;
                JButton botao = new JButton();
                botao.setFont(FONTE_PECA);
                botao.setMargin(new Insets(0, 0, 0, 0));
                botao.setFocusPainted(false);
                botao.setContentAreaFilled(false);
                botao.setOpaque(true);
                botao.setBorderPainted(false);
                botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                botao.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        ouvinte.aoClicarCasa(new Posicao(linhaBotao, colunaBotao));
                    }
                });
                botoes[linha][coluna] = botao;
                add(botao);
            }
        }
    }

    private boolean ehCasaClara(int linha, int coluna) {
        return (linha + coluna) % 2 != 0;
    }
}
