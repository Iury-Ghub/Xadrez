package xadrez.controlador;

import java.util.List;

import xadrez.modelo.Alianca;
import xadrez.modelo.Tabuleiro;
import xadrez.modelo.Jogo;
import xadrez.modelo.EstadoJogo;
import xadrez.modelo.FormataNotacaoJogada;
import xadrez.modelo.RegistroJogada;
import xadrez.modelo.Posicao;
import xadrez.modelo.pecas.Peca;
import xadrez.util.LogDepuracao;

public final class ControladorXadrez {
    private final Jogo jogo;
    private final FormataNotacaoJogada formataNotacao;

    public ControladorXadrez() {
        this.jogo = new Jogo();
        this.formataNotacao = new FormataNotacaoJogada();
    }
    public void tratarCliqueCasa(Posicao posicao) {
        LogDepuracao.operacao("ACOES", "ControladorXadrez.tratarCliqueCasa: " + posicao);
        if (posicao == null) {
            return;
        }
        jogo.clicar(posicao);
    }
    public void tratarNovoJogo() {
        LogDepuracao.operacao("ACOES", "ControladorXadrez.tratarNovoJogo");
        jogo.reiniciar();
    }
    public void aplicarJogada(Posicao origem, Posicao destino) {
        jogo.clicar(origem);
        jogo.clicar(destino);
    }
    public Tabuleiro getTabuleiro() {
        return jogo.getTabuleiro();
    }
    public Alianca getTurnoAtual() {
        return jogo.getTurnoAtual();
    }
    public Posicao getPosicaoSelecionada() {
        return jogo.getPosicaoSelecionada();
    }
    public List<Posicao> getJogadasLegaisSelecionadas() {
        return jogo.getJogadasLegaisSelecionadas();
    }
    public EstadoJogo getEstado() {
        return jogo.getEstado();
    }
    public String getMensagemStatus() {
        return jogo.getMensagemStatus();
    }
    public List<RegistroJogada> getHistoricoJogadas() {
        return jogo.getHistoricoJogadas();
    }
    public String getHistoricoJogadasFormatado() {
        return formataNotacao.formatarHistorico(jogo.getHistoricoJogadas());
    }
    public Peca getPecaSelecionada() {
        Posicao posicaoSelecionada = jogo.getPosicaoSelecionada();
        if (posicaoSelecionada == null) {
            return null;
        }
        return jogo.getTabuleiro().getPeca(posicaoSelecionada);
    }
}
