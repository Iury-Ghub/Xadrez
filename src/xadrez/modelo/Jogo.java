package xadrez.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import xadrez.modelo.pecas.Peao;
import xadrez.modelo.pecas.Peca;
import xadrez.modelo.pecas.Rainha;
import xadrez.modelo.pecas.Rei;
import xadrez.modelo.pecas.Torre;
import xadrez.util.LogDepuracao;

public final class Jogo {
    private final Tabuleiro tabuleiro;
    private Alianca turnoAtual;
    private Posicao posicaoSelecionada;
    private List<Posicao> jogadasLegaisSelecionadas;
    private List<RegistroJogada> historicoJogadas;
    private EstadoJogo estado;
    private String mensagemStatus;
    private boolean brancasPodeRoqueRei;
    private boolean brancasPodeRoqueDama;
    private boolean pretasPodeRoqueRei;
    private boolean pretasPodeRoqueDama;
    private Posicao alvoEnPassant;
    private Alianca enPassantDisponivelPara;

    public Jogo() {
        this.tabuleiro = new Tabuleiro();
        this.jogadasLegaisSelecionadas = new ArrayList<Posicao>();
        this.historicoJogadas = new ArrayList<RegistroJogada>();
        reiniciar();
    }
    public void reiniciar() {
        LogDepuracao.operacao("JOGO", "Jogo.reiniciar");
        tabuleiro.reiniciar();
        turnoAtual = Alianca.BRANCAS;
        posicaoSelecionada = null;
        jogadasLegaisSelecionadas = new ArrayList<Posicao>();
        historicoJogadas = new ArrayList<RegistroJogada>();
        brancasPodeRoqueRei = true;
        brancasPodeRoqueDama = true;
        pretasPodeRoqueRei = true;
        pretasPodeRoqueDama = true;
        alvoEnPassant = null;
        enPassantDisponivelPara = null;
        atualizarEstado();
    }
    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }
    public Alianca getTurnoAtual() {
        return turnoAtual;
    }
    public Posicao getPosicaoSelecionada() {
        return posicaoSelecionada;
    }
    public List<Posicao> getJogadasLegaisSelecionadas() {
        return Collections.unmodifiableList(jogadasLegaisSelecionadas);
    }
    public EstadoJogo getEstado() {
        return estado;
    }
    public String getMensagemStatus() {
        return mensagemStatus;
    }
    public List<RegistroJogada> getHistoricoJogadas() {
        return Collections.unmodifiableList(historicoJogadas);
    }
    public void clicar(Posicao posicao) {
        LogDepuracao.operacao("JOGO", "Jogo.clicar: " + posicao);
        if (estado == EstadoJogo.XEQUE_MATE || estado == EstadoJogo.AFOGAMENTO) {
            return;
        }

        Peca pecaClicada = tabuleiro.getPeca(posicao);

        if (posicaoSelecionada == null) {
            if (pecaClicada != null && pecaClicada.getAlianca() == turnoAtual) {
                selecionarPeca(posicao);
            }
            return;
        }

        if (posicaoSelecionada.equals(posicao)) {
            limparSelecao();
            return;
        }

        if (jogadasLegaisSelecionadas.contains(posicao)) {
            executarJogada(posicao);
            return;
        }

        if (pecaClicada != null && pecaClicada.getAlianca() == turnoAtual) {
            selecionarPeca(posicao);
        }
    }
    private void selecionarPeca(Posicao posicao) {
        posicaoSelecionada = posicao;
        jogadasLegaisSelecionadas = calcularJogadasLegais(posicao);
    }
    private void limparSelecao() {
        posicaoSelecionada = null;
        jogadasLegaisSelecionadas = new ArrayList<Posicao>();
    }
    private void executarJogada(Posicao alvo) {
        LogDepuracao.operacao("JOGO", "Jogo.executarJogada: " + posicaoSelecionada + " -> " + alvo);
        int numeroPly = historicoJogadas.size() + 1;
        Alianca aliancaMovendo = turnoAtual;
        Posicao origem = posicaoSelecionada;
        Peca pecaMovendo = tabuleiro.getPeca(origem);
        boolean jogadaEnPassant = ehJogadaEnPassant(pecaMovendo, origem, alvo);
        Peca pecaCapturada = aplicarJogada(tabuleiro, origem, alvo, pecaMovendo, jogadaEnPassant);
        boolean jogadaRoque = ehJogadaRoque(pecaMovendo, origem, alvo);

        if (jogadaRoque) {
            moverTorreRoque(tabuleiro, aliancaMovendo, alvo);
        }
        Peca pecaResultante = tabuleiro.getPeca(alvo);

        atualizarDireitosRoque(origem, alvo, pecaMovendo, pecaCapturada);
        atualizarEstadoEnPassant(origem, alvo, pecaMovendo, aliancaMovendo);

        if (pecaMovendo instanceof Peao) {
            if (ehCasaPromocao(alvo, pecaMovendo.getAlianca())) {
                tabuleiro.setPeca(alvo, new Rainha(pecaMovendo.getAlianca()));
                pecaResultante = tabuleiro.getPeca(alvo);
            }
        }

        turnoAtual = turnoAtual.oposto();
        limparSelecao();
        atualizarEstado();
        historicoJogadas.add(new RegistroJogada(
                numeroPly,
                aliancaMovendo,
                origem,
                alvo,
                pecaMovendo,
                pecaCapturada,
                pecaResultante,
                estado,
                pecaCapturada == null ? null : (jogadaEnPassant ? getPosicaoCapturadaEnPassant(origem, alvo) : alvo),
                jogadaEnPassant));
    }
    private Peca aplicarJogada(Tabuleiro estadoTabuleiro, Posicao origem, Posicao destino, Peca pecaMovendo, boolean jogadaEnPassant) {
        if (!jogadaEnPassant) {
            return estadoTabuleiro.moverPeca(origem, destino);
        }

        if (estadoTabuleiro.getPeca(destino) != null) {
            throw new IllegalStateException("Destino inválido para en passant " + destino);
        }

        Posicao posicaoCapturada = getPosicaoCapturadaEnPassant(origem, destino);
        Peca pecaCapturada = estadoTabuleiro.getPeca(posicaoCapturada);
        if (!(pecaCapturada instanceof Peao) || pecaCapturada.getAlianca() == pecaMovendo.getAlianca()) {
            throw new IllegalStateException("Captura en passant inválida de " + origem + " para " + destino);
        }

        estadoTabuleiro.moverPeca(origem, destino);
        estadoTabuleiro.setPeca(posicaoCapturada, null);
        return pecaCapturada;
    }
    private boolean ehCasaPromocao(Posicao posicao, Alianca alianca) {
        return (alianca == Alianca.BRANCAS && posicao.getLinha() == 0)
                || (alianca == Alianca.PRETAS && posicao.getLinha() == 7);
    }
    private boolean ehJogadaRoque(Peca pecaMovendo, Posicao origem, Posicao destino) {
        return pecaMovendo instanceof Rei
                && origem != null
                && destino != null
                && origem.getLinha() == destino.getLinha()
                && Math.abs(origem.getColuna() - destino.getColuna()) == 2;
    }
    private void moverTorreRoque(Tabuleiro estadoTabuleiro, Alianca alianca, Posicao destinoRei) {
        Posicao origemTorre;
        Posicao destinoTorre;

        if (destinoRei.getColuna() == 6) {
            origemTorre = alianca == Alianca.BRANCAS ? new Posicao(7, 7) : new Posicao(0, 7);
            destinoTorre = alianca == Alianca.BRANCAS ? new Posicao(7, 5) : new Posicao(0, 5);
        } else if (destinoRei.getColuna() == 2) {
            origemTorre = alianca == Alianca.BRANCAS ? new Posicao(7, 0) : new Posicao(0, 0);
            destinoTorre = alianca == Alianca.BRANCAS ? new Posicao(7, 3) : new Posicao(0, 3);
        } else {
            throw new IllegalArgumentException("Destino de roque inválido: " + destinoRei);
        }

        estadoTabuleiro.moverPeca(origemTorre, destinoTorre);
    }
    private void atualizarDireitosRoque(Posicao origem, Posicao destino, Peca pecaMovendo, Peca pecaCapturada) {
        if (pecaMovendo instanceof Rei) {
            desabilitarTodosDireitosRoque(pecaMovendo.getAlianca());
        } else if (pecaMovendo instanceof Torre) {
            desabilitarDireitoRoqueTorre(pecaMovendo.getAlianca(), origem);
        }

        if (pecaCapturada instanceof Torre) {
            desabilitarDireitoRoqueTorre(pecaCapturada.getAlianca(), destino);
        }
    }
    private void atualizarEstadoEnPassant(Posicao origem, Posicao destino, Peca pecaMovendo, Alianca aliancaMovendo) {
        alvoEnPassant = null;
        enPassantDisponivelPara = null;

        if (pecaMovendo instanceof Peao && Math.abs(origem.getLinha() - destino.getLinha()) == 2) {
            int linhaIntermediaria = (origem.getLinha() + destino.getLinha()) / 2;
            alvoEnPassant = new Posicao(linhaIntermediaria, origem.getColuna());
            enPassantDisponivelPara = aliancaMovendo.oposto();
        }
    }
    private void desabilitarTodosDireitosRoque(Alianca alianca) {
        if (alianca == Alianca.BRANCAS) {
            brancasPodeRoqueRei = false;
            brancasPodeRoqueDama = false;
        } else {
            pretasPodeRoqueRei = false;
            pretasPodeRoqueDama = false;
        }
    }
    private void desabilitarDireitoRoqueTorre(Alianca alianca, Posicao casa) {
        if (alianca == Alianca.BRANCAS) {
            if (ehCasaInicialTorre(casa, Alianca.BRANCAS, true)) {
                brancasPodeRoqueRei = false;
            } else if (ehCasaInicialTorre(casa, Alianca.BRANCAS, false)) {
                brancasPodeRoqueDama = false;
            }
        } else {
            if (ehCasaInicialTorre(casa, Alianca.PRETAS, true)) {
                pretasPodeRoqueRei = false;
            } else if (ehCasaInicialTorre(casa, Alianca.PRETAS, false)) {
                pretasPodeRoqueDama = false;
            }
        }
    }
    private boolean ehCasaInicialTorre(Posicao casa, Alianca alianca, boolean ladoRei) {
        if (casa == null) {
            return false;
        }

        if (alianca == Alianca.BRANCAS) {
            return ladoRei
                    ? casa.getLinha() == 7 && casa.getColuna() == 7
                    : casa.getLinha() == 7 && casa.getColuna() == 0;
        }

        return ladoRei
                ? casa.getLinha() == 0 && casa.getColuna() == 7
                : casa.getLinha() == 0 && casa.getColuna() == 0;
    }
    private boolean podeFazerRoqueRei(Alianca alianca) {
        return podeFazerRoque(alianca, true);
    }
    private boolean podeFazerRoqueDama(Alianca alianca) {
        return podeFazerRoque(alianca, false);
    }
    private boolean podeFazerRoque(Alianca alianca, boolean ladoRei) {
        if (alianca == Alianca.BRANCAS) {
            if (ladoRei && !brancasPodeRoqueRei) {
                return false;
            }
            if (!ladoRei && !brancasPodeRoqueDama) {
                return false;
            }
        } else {
            if (ladoRei && !pretasPodeRoqueRei) {
                return false;
            }
            if (!ladoRei && !pretasPodeRoqueDama) {
                return false;
            }
        }

        Posicao casaInicialRei = getCasaInicialRei(alianca);
        Posicao casaInicialTorre = getCasaInicialTorre(alianca, ladoRei);
        Posicao casaPassagem = ladoRei ? getCasaPassagemRoqueRei(alianca) : getCasaPassagemRoqueDama(alianca);
        Posicao casaDestino = ladoRei ? getCasaDestinoRoqueRei(alianca) : getCasaDestinoRoqueDama(alianca);
        Alianca inimigo = alianca.oposto();

        Peca pecaRei = tabuleiro.getPeca(casaInicialRei);
        Peca pecaTorre = tabuleiro.getPeca(casaInicialTorre);
        if (!(pecaRei instanceof Rei) || pecaRei.getAlianca() != alianca) {
            return false;
        }
        if (!(pecaTorre instanceof Torre) || pecaTorre.getAlianca() != alianca) {
            return false;
        }

        if (estaEmXeque(alianca)) {
            return false;
        }

        if (!casaEstaVazia(casaPassagem) || !casaEstaVazia(casaDestino)) {
            return false;
        }

        if (!ladoRei && !casaEstaVazia(getCasaExtraRoqueDama(alianca))) {
            return false;
        }

        if (tabuleiro.casaEstaAtacada(casaPassagem, inimigo)) {
            return false;
        }

        return true;
    }
    private Posicao getCasaInicialRei(Alianca alianca) {
        return alianca == Alianca.BRANCAS ? new Posicao(7, 4) : new Posicao(0, 4);
    }
    private Posicao getCasaInicialTorre(Alianca alianca, boolean ladoRei) {
        if (alianca == Alianca.BRANCAS) {
            return ladoRei ? new Posicao(7, 7) : new Posicao(7, 0);
        }
        return ladoRei ? new Posicao(0, 7) : new Posicao(0, 0);
    }
    private Posicao getCasaPassagemRoqueRei(Alianca alianca) {
        return alianca == Alianca.BRANCAS ? new Posicao(7, 5) : new Posicao(0, 5);
    }
    private Posicao getCasaDestinoRoqueRei(Alianca alianca) {
        return alianca == Alianca.BRANCAS ? new Posicao(7, 6) : new Posicao(0, 6);
    }
    private Posicao getCasaPassagemRoqueDama(Alianca alianca) {
        return alianca == Alianca.BRANCAS ? new Posicao(7, 3) : new Posicao(0, 3);
    }
    private Posicao getCasaDestinoRoqueDama(Alianca alianca) {
        return alianca == Alianca.BRANCAS ? new Posicao(7, 2) : new Posicao(0, 2);
    }
    private Posicao getCasaExtraRoqueDama(Alianca alianca) {
        return alianca == Alianca.BRANCAS ? new Posicao(7, 1) : new Posicao(0, 1);
    }
    private boolean casaEstaVazia(Posicao casa) {
        return tabuleiro.getPeca(casa) == null;
    }
    private boolean ehJogadaEnPassant(Peca pecaMovendo, Posicao origem, Posicao destino) {
        if (!(pecaMovendo instanceof Peao) || origem == null || destino == null || alvoEnPassant == null) {
            return false;
        }

        if (pecaMovendo.getAlianca() != enPassantDisponivelPara || !destino.equals(alvoEnPassant)) {
            return false;
        }

        if (tabuleiro.getPeca(destino) != null) {
            return false;
        }

        if (Math.abs(origem.getColuna() - destino.getColuna()) != 1) {
            return false;
        }

        int direcao = pecaMovendo.getAlianca() == Alianca.BRANCAS ? -1 : 1;
        if (origem.getLinha() != destino.getLinha() - direcao) {
            return false;
        }

        Posicao posicaoCapturada = getPosicaoCapturadaEnPassant(origem, destino);
        Peca pecaCapturada = tabuleiro.getPeca(posicaoCapturada);
        return pecaCapturada instanceof Peao && pecaCapturada.getAlianca() != pecaMovendo.getAlianca();
    }
    private Posicao getPosicaoCapturadaEnPassant(Posicao origem, Posicao destino) {
        return new Posicao(origem.getLinha(), destino.getColuna());
    }
    private List<Posicao> calcularJogadasLegais(Posicao origem) {
        Peca peca = tabuleiro.getPeca(origem);

        if (peca == null || peca.getAlianca() != turnoAtual) {
            return Collections.emptyList();
        }

        List<Posicao> jogadasPseudo = new ArrayList<Posicao>(peca.getJogadasPseudoLegais(tabuleiro, origem));
        if (peca instanceof Rei) {
            jogadasPseudo.addAll(coletarJogadasRoque(peca.getAlianca(), origem));
        }
        if (peca instanceof Peao) {
            jogadasPseudo.addAll(coletarJogadasEnPassant(peca, origem));
        }
        List<Posicao> jogadasLegais = new ArrayList<Posicao>();

        for (Posicao alvo : jogadasPseudo) {
            Peca pecaAlvo = tabuleiro.getPeca(alvo);
            if (pecaAlvo instanceof Rei) {
                continue;
            }

            Tabuleiro instantaneo = tabuleiro.copiar();
            aplicarJogada(instantaneo, origem, alvo, peca, ehJogadaEnPassant(peca, origem, alvo));
            if (ehJogadaRoque(peca, origem, alvo)) {
                moverTorreRoque(instantaneo, peca.getAlianca(), alvo);
            }

            Posicao posicaoRei = instantaneo.encontrarRei(peca.getAlianca());
            if (posicaoRei == null) {
                continue;
            }

            if (!instantaneo.casaEstaAtacada(posicaoRei, peca.getAlianca().oposto())) {
                jogadasLegais.add(alvo);
            }
        }

        return jogadasLegais;
    }
    private List<Posicao> coletarJogadasEnPassant(Peca peca, Posicao origem) {
        List<Posicao> jogadas = new ArrayList<Posicao>();
        if (!(peca instanceof Peao) || alvoEnPassant == null || enPassantDisponivelPara != peca.getAlianca()) {
            return jogadas;
        }

        int direcao = peca.getAlianca() == Alianca.BRANCAS ? -1 : 1;
        if (origem.getLinha() != alvoEnPassant.getLinha() - direcao) {
            return jogadas;
        }

        if (Math.abs(origem.getColuna() - alvoEnPassant.getColuna()) != 1) {
            return jogadas;
        }

        Posicao posicaoCapturada = getPosicaoCapturadaEnPassant(origem, alvoEnPassant);
        Peca pecaCapturada = tabuleiro.getPeca(posicaoCapturada);
        if (!(pecaCapturada instanceof Peao) || pecaCapturada.getAlianca() == peca.getAlianca()) {
            return jogadas;
        }

        if (tabuleiro.getPeca(alvoEnPassant) != null) {
            return jogadas;
        }

        jogadas.add(alvoEnPassant);
        return jogadas;
    }
    private List<Posicao> coletarJogadasRoque(Alianca alianca, Posicao origem) {
        List<Posicao> jogadas = new ArrayList<Posicao>();

        if (alianca == Alianca.BRANCAS && origem.equals(new Posicao(7, 4))) {
            if (podeFazerRoqueRei(Alianca.BRANCAS)) {
                jogadas.add(new Posicao(7, 6)); // g1
            }
            if (podeFazerRoqueDama(Alianca.BRANCAS)) {
                jogadas.add(new Posicao(7, 2)); // c1
            }
        }

        if (alianca == Alianca.PRETAS && origem.equals(new Posicao(0, 4))) {
            if (podeFazerRoqueRei(Alianca.PRETAS)) {
                jogadas.add(new Posicao(0, 6)); // g8
            }
            if (podeFazerRoqueDama(Alianca.PRETAS)) {
                jogadas.add(new Posicao(0, 2)); // c8
            }
        }

        return jogadas;
    }
    private void atualizarEstado() {
        LogDepuracao.operacao("JOGO", "Jogo.atualizarEstado");
        boolean emXeque = estaEmXeque(turnoAtual);
        boolean temJogada = temAlgumaJogadaLegal();

        if (emXeque && !temJogada) {
            estado = EstadoJogo.XEQUE_MATE;
            mensagemStatus = "Xeque-mate. " + turnoAtual.oposto().getNomeExibicao() + " venceram.";
            return;
        }

        if (!emXeque && !temJogada) {
            estado = EstadoJogo.AFOGAMENTO;
            mensagemStatus = "Empate por afogamento.";
            return;
        }

        if (emXeque) {
            estado = EstadoJogo.XEQUE;
            mensagemStatus = turnoAtual.getNomeExibicao() + " estão em xeque.";
            return;
        }

        estado = EstadoJogo.ATIVO;
        mensagemStatus = "Vez das " + turnoAtual.getNomeExibicao().toLowerCase(Locale.ROOT) + ".";
    }
    private boolean temAlgumaJogadaLegal() {
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                Peca peca = tabuleiro.getPeca(linha, coluna);
                if (peca != null && peca.getAlianca() == turnoAtual) {
                    List<Posicao> jogadas = calcularJogadasLegais(new Posicao(linha, coluna));
                    if (!jogadas.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private boolean estaEmXeque(Alianca alianca) {
        Posicao posicaoRei = tabuleiro.encontrarRei(alianca);
        if (posicaoRei == null) {
            return true;
        }
        return tabuleiro.casaEstaAtacada(posicaoRei, alianca.oposto());
    }
}
