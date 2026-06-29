package xadrez.modelo.pecas;

import java.util.ArrayList;
import java.util.List;

import xadrez.modelo.Alianca;
import xadrez.modelo.Tabuleiro;
import xadrez.modelo.Posicao;

public final class Cavalo extends Peca {
    private static final int[][] DESLOCAMENTOS = {
            {-2, -1},
            {-2, 1},
            {-1, -2},
            {-1, 2},
            {1, -2},
            {1, 2},
            {2, -1},
            {2, 1}
    };

    public Cavalo(Alianca alianca) {
        super(alianca, alianca == Alianca.BRANCAS ? "♘" : "♞");
    }

    @Override
    public String getNome() {
        return "Cavalo";
    }

    @Override
    public List<Posicao> getJogadasPseudoLegais(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> jogadas = new ArrayList<Posicao>();
        for (int i = 0; i < DESLOCAMENTOS.length; i++) {
            int linha = origem.getLinha() + DESLOCAMENTOS[i][0];
            int coluna = origem.getColuna() + DESLOCAMENTOS[i][1];
            if (!tabuleiro.estaDentro(linha, coluna)) {
                continue;
            }
            Posicao alvo = new Posicao(linha, coluna);
            if (podeOcupar(tabuleiro.getPeca(alvo))) {
                jogadas.add(alvo);
            }
        }
        return jogadas;
    }
}
