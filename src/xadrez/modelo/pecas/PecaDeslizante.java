package xadrez.modelo.pecas;

import java.util.ArrayList;
import java.util.List;

import xadrez.modelo.Alianca;
import xadrez.modelo.Tabuleiro;
import xadrez.modelo.Posicao;

public abstract class PecaDeslizante extends Peca {
    protected PecaDeslizante(Alianca alianca, String simbolo) {
        super(alianca, simbolo);
    }
    protected List<Posicao> coletarJogadasDeslizantes(Tabuleiro tabuleiro, Posicao origem, int[][] direcoes) {
        List<Posicao> jogadas = new ArrayList<Posicao>();
        for (int i = 0; i < direcoes.length; i++) {
            int linha = origem.getLinha() + direcoes[i][0];
            int coluna = origem.getColuna() + direcoes[i][1];
            while (tabuleiro.estaDentro(linha, coluna)) {
                Posicao alvo = new Posicao(linha, coluna);
                Peca pecaAlvo = tabuleiro.getPeca(alvo);
                if (pecaAlvo == null) {
                    jogadas.add(alvo);
                } else {
                    if (pecaAlvo.getAlianca() != getAlianca()) {
                        jogadas.add(alvo);
                    }
                    break;
                }
                linha += direcoes[i][0];
                coluna += direcoes[i][1];
            }
        }
        return jogadas;
    }
}
