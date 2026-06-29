package xadrez.modelo;

import xadrez.modelo.pecas.Peca;
import xadrez.modelo.pecas.Rei;

public final class RegistroJogada {
    private final int numeroPly;
    private final Alianca alianca;
    private final Posicao origem;
    private final Posicao destino;
    private final Peca pecaMovida;
    private final Peca pecaCapturada;
    private final Peca pecaResultante;
    private final EstadoJogo estadoResultante;
    private final Posicao posicaoCapturada;
    private final boolean enPassant;

    public RegistroJogada(int numeroPly,
                          Alianca alianca,
                          Posicao origem,
                          Posicao destino,
                          Peca pecaMovida,
                          Peca pecaCapturada,
                          Peca pecaResultante,
                          EstadoJogo estadoResultante,
                          Posicao posicaoCapturada,
                          boolean enPassant) {
        this.numeroPly = numeroPly;
        this.alianca = alianca;
        this.origem = origem;
        this.destino = destino;
        this.pecaMovida = pecaMovida;
        this.pecaCapturada = pecaCapturada;
        this.pecaResultante = pecaResultante;
        this.estadoResultante = estadoResultante;
        this.posicaoCapturada = posicaoCapturada;
        this.enPassant = enPassant;
    }

    public int getNumeroPly() {
        return numeroPly;
    }
    public Posicao getOrigem() {
        return origem;
    }
    public Posicao getDestino() {
        return destino;
    }
    public Peca getPecaMovida() {
        return pecaMovida;
    }
    public Peca getPecaCapturada() {
        return pecaCapturada;
    }
    public Peca getPecaResultante() {
        return pecaResultante;
    }
    public EstadoJogo getEstadoResultante() {
        return estadoResultante;
    }
    public boolean ehCaptura() {
        return pecaCapturada != null;
    }
    public boolean ehPromocao() {
        return pecaResultante != null
                && pecaMovida != null
                && !pecaResultante.getClass().equals(pecaMovida.getClass());
    }
    public boolean ehRoque() {
        return pecaMovida instanceof Rei
                && origem != null
                && destino != null
                && origem.getLinha() == destino.getLinha()
                && Math.abs(origem.getColuna() - destino.getColuna()) == 2;
    }
}
