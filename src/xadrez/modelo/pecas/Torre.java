package xadrez.modelo.pecas;

import java.util.List;

import xadrez.modelo.Alianca;
import xadrez.modelo.Tabuleiro;
import xadrez.modelo.Posicao;

public final class Torre extends PecaDeslizante {
    private static final int[][] DIRECOES = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };

    public Torre(Alianca alianca) {
        super(alianca, alianca == Alianca.BRANCAS ? "♖" : "♜");
    }

    @Override
    public String getNome() {
        return "Torre";
    }

    @Override
    public List<Posicao> getJogadasPseudoLegais(Tabuleiro tabuleiro, Posicao origem) {
        return coletarJogadasDeslizantes(tabuleiro, origem, DIRECOES);
    }
}
