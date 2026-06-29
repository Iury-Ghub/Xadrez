package xadrez.modelo.pecas;

import java.util.List;

import xadrez.modelo.Alianca;
import xadrez.modelo.Tabuleiro;
import xadrez.modelo.Posicao;

public abstract class Peca {
    private final Alianca alianca;
    private final String simbolo;

    protected Peca(Alianca alianca, String simbolo) {
        this.alianca = alianca;
        this.simbolo = simbolo;
    }

    public Alianca getAlianca() {
        return alianca;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public abstract String getNome();

    public abstract List<Posicao> getJogadasPseudoLegais(Tabuleiro tabuleiro, Posicao origem);

    public List<Posicao> getCasasAtaque(Tabuleiro tabuleiro, Posicao origem) {
        return getJogadasPseudoLegais(tabuleiro, origem);
    }

    protected boolean ehInimigo(Peca peca) {
        return peca != null && peca.getAlianca() != alianca;
    }

    protected boolean podeOcupar(Peca peca) {
        return peca == null || ehInimigo(peca);
    }

    @Override
    public String toString() {
        return getNome();
    }
}
