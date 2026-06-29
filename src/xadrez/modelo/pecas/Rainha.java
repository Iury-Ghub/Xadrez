package xadrez.modelo.pecas;

import java.util.List;

import xadrez.modelo.Alianca;
import xadrez.modelo.Tabuleiro;
import xadrez.modelo.Posicao;

public final class Rainha extends PecaDeslizante {
    private static final int[][] DIRECOES = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1},
            {-1, -1},
            {-1, 1},
            {1, -1},
            {1, 1}
    };

    public Rainha(Alianca alianca) {
        super(alianca, alianca == Alianca.BRANCAS ? "♕" : "♛");
    }

    @Override
    public String getNome() {
        return "Rainha";
    }

    @Override
    public List<Posicao> getJogadasPseudoLegais(Tabuleiro tabuleiro, Posicao origem) {
        return coletarJogadasDeslizantes(tabuleiro, origem, DIRECOES);
    }
}
