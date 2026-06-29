package xadrez.modelo;

import java.util.Arrays;

import xadrez.modelo.pecas.Bispo;
import xadrez.modelo.pecas.Cavalo;
import xadrez.modelo.pecas.Peao;
import xadrez.modelo.pecas.Peca;
import xadrez.modelo.pecas.Rainha;
import xadrez.modelo.pecas.Rei;
import xadrez.modelo.pecas.Torre;
import xadrez.util.LogDepuracao;

public final class Tabuleiro {
    private final Peca[][] casas;

    public Tabuleiro() {
        this.casas = new Peca[8][8];
    }
    public void reiniciar() {
        LogDepuracao.operacao("TABULEIRO", "Tabuleiro.reiniciar");
        configurarPosicaoInicial();
    }
    public Tabuleiro copiar() {
        Tabuleiro copia = new Tabuleiro();
        for (int linha = 0; linha < 8; linha++) {
            System.arraycopy(this.casas[linha], 0, copia.casas[linha], 0, 8);
        }
        return copia;
    }
    public void limpar() {
        for (Peca[] linha : casas) {
            Arrays.fill(linha, null);
        }
    }
    public void configurarPosicaoInicial() {
        limpar();
        colocarFileiraTraseira(0, Alianca.PRETAS);
        for (int coluna = 0; coluna < 8; coluna++) {
            casas[1][coluna] = new Peao(Alianca.PRETAS);
            casas[6][coluna] = new Peao(Alianca.BRANCAS);
        }
        colocarFileiraTraseira(7, Alianca.BRANCAS);
    }
    public Peca getPeca(Posicao posicao) {
        if (posicao == null || !estaDentro(posicao)) {
            return null;
        }
        return casas[posicao.getLinha()][posicao.getColuna()];
    }
    public Peca getPeca(int linha, int coluna) {
        if (!estaDentro(linha, coluna)) {
            return null;
        }
        return casas[linha][coluna];
    }
    public void setPeca(Posicao posicao, Peca peca) {
        if (posicao == null || !estaDentro(posicao)) {
            throw new IllegalArgumentException("Posição fora do tabuleiro");
        }
        casas[posicao.getLinha()][posicao.getColuna()] = peca;
    }
    public Peca moverPeca(Posicao origem, Posicao destino) {
        LogDepuracao.operacao("TABULEIRO", "Tabuleiro.moverPeca: " + origem + " -> " + destino);
        Peca pecaMovendo = getPeca(origem);
        if (pecaMovendo == null) {
            throw new IllegalStateException("Nenhuma peça na posição de origem " + origem);
        }
        Peca pecaCapturada = getPeca(destino);
        setPeca(destino, pecaMovendo);
        setPeca(origem, null);
        return pecaCapturada;
    }
    public boolean estaDentro(Posicao posicao) {
        return posicao != null && estaDentro(posicao.getLinha(), posicao.getColuna());
    }
    public boolean estaDentro(int linha, int coluna) {
        return linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8;
    }
    public Posicao encontrarRei(Alianca alianca) {
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                Peca peca = casas[linha][coluna];
                if (peca instanceof Rei && peca.getAlianca() == alianca) {
                    return new Posicao(linha, coluna);
                }
            }
        }
        return null;
    }
    public boolean casaEstaAtacada(Posicao alvo, Alianca porAlianca) {
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                Peca peca = casas[linha][coluna];
                if (peca != null && peca.getAlianca() == porAlianca) {
                    Posicao origem = new Posicao(linha, coluna);
                    if (peca.getCasasAtaque(this, origem).contains(alvo)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private void colocarFileiraTraseira(int linha, Alianca alianca) {
        casas[linha][0] = new Torre(alianca);
        casas[linha][1] = new Cavalo(alianca);
        casas[linha][2] = new Bispo(alianca);
        casas[linha][3] = new Rainha(alianca);
        casas[linha][4] = new Rei(alianca);
        casas[linha][5] = new Bispo(alianca);
        casas[linha][6] = new Cavalo(alianca);
        casas[linha][7] = new Torre(alianca);
    }
}
