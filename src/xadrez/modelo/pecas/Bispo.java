package xadrez.modelo.pecas;

import java.util.List;

import xadrez.modelo.Alianca;
import xadrez.modelo.Tabuleiro;
import xadrez.modelo.Posicao;

public final class Bispo extends PecaDeslizante {
    private static final int[][] DIRECOES = {
            {-1, -1},
            {-1, 1},
            {1, -1},
            {1, 1}
    };

    public Bispo(Alianca alianca) {
        super(alianca, alianca == Alianca.BRANCAS ? "♗" : "♝");
    }

    @Override
    public String getNome() {
        return "Bispo";
    }

    @Override
    public List<Posicao> getJogadasPseudoLegais(Tabuleiro tabuleiro, Posicao origem) {
        return coletarJogadasDeslizantes(tabuleiro, origem, DIRECOES);
    }
}
