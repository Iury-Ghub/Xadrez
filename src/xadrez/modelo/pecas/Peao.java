package xadrez.modelo.pecas;

import java.util.ArrayList;
import java.util.List;

import xadrez.modelo.Alianca;
import xadrez.modelo.Tabuleiro;
import xadrez.modelo.Posicao;

public final class Peao extends Peca {
    public Peao(Alianca alianca) {
        super(alianca, alianca == Alianca.BRANCAS ? "♙" : "♟");
    }

    @Override
    public String getNome() {
        return "Peão";
    }

    @Override
    public List<Posicao> getJogadasPseudoLegais(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> jogadas = new ArrayList<Posicao>();
        int direcao = getAlianca() == Alianca.BRANCAS ? -1 : 1;
        int linhaInicial = getAlianca() == Alianca.BRANCAS ? 6 : 1;

        int linhaUmPasso = origem.getLinha() + direcao;
        if (tabuleiro.estaDentro(linhaUmPasso, origem.getColuna()) && tabuleiro.getPeca(linhaUmPasso, origem.getColuna()) == null) {
            jogadas.add(new Posicao(linhaUmPasso, origem.getColuna()));

            int linhaDoisPassos = origem.getLinha() + (2 * direcao);
            if (origem.getLinha() == linhaInicial
                    && tabuleiro.estaDentro(linhaDoisPassos, origem.getColuna())
                    && tabuleiro.getPeca(linhaDoisPassos, origem.getColuna()) == null) {
                jogadas.add(new Posicao(linhaDoisPassos, origem.getColuna()));
            }
        }

        adicionarJogadaCaptura(tabuleiro, origem, jogadas, direcao, -1);
        adicionarJogadaCaptura(tabuleiro, origem, jogadas, direcao, 1);
        return jogadas;
    }

    @Override
    public List<Posicao> getCasasAtaque(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> ataques = new ArrayList<Posicao>();
        int direcao = getAlianca() == Alianca.BRANCAS ? -1 : 1;
        adicionarCasaAtaque(tabuleiro, origem, ataques, direcao, -1);
        adicionarCasaAtaque(tabuleiro, origem, ataques, direcao, 1);
        return ataques;
    }

    private void adicionarJogadaCaptura(Tabuleiro tabuleiro, Posicao origem, List<Posicao> jogadas, int direcao, int deltaColuna) {
        int linha = origem.getLinha() + direcao;
        int coluna = origem.getColuna() + deltaColuna;
        if (!tabuleiro.estaDentro(linha, coluna)) {
            return;
        }
        Posicao alvo = new Posicao(linha, coluna);
        Peca peca = tabuleiro.getPeca(alvo);
        if (peca != null && peca.getAlianca() != getAlianca()) {
            jogadas.add(alvo);
        }
    }

    private void adicionarCasaAtaque(Tabuleiro tabuleiro, Posicao origem, List<Posicao> ataques, int direcao, int deltaColuna) {
        int linha = origem.getLinha() + direcao;
        int coluna = origem.getColuna() + deltaColuna;
        if (tabuleiro.estaDentro(linha, coluna)) {
            ataques.add(new Posicao(linha, coluna));
        }
    }
}
