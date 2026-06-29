package xadrez.modelo.pecas;

import java.util.ArrayList;
import java.util.List;

import xadrez.modelo.Alianca;
import xadrez.modelo.Tabuleiro;
import xadrez.modelo.Posicao;

public final class Rei extends Peca {
    public Rei(Alianca alianca) {
        super(alianca, alianca == Alianca.BRANCAS ? "♔" : "♚");
    }

    @Override
    public String getNome() {
        return "Rei";
    }

    @Override
    public List<Posicao> getJogadasPseudoLegais(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> jogadas = new ArrayList<Posicao>();
        for (int deltaLinha = -1; deltaLinha <= 1; deltaLinha++) {
            for (int deltaColuna = -1; deltaColuna <= 1; deltaColuna++) {
                if (deltaLinha == 0 && deltaColuna == 0) {
                    continue;
                }
                int linha = origem.getLinha() + deltaLinha;
                int coluna = origem.getColuna() + deltaColuna;
                if (!tabuleiro.estaDentro(linha, coluna)) {
                    continue;
                }
                Posicao alvo = new Posicao(linha, coluna);
                if (podeOcupar(tabuleiro.getPeca(alvo))) {
                    jogadas.add(alvo);
                }
            }
        }
        return jogadas;
    }
}
