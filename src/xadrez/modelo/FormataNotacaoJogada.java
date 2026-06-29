package xadrez.modelo;

import java.util.List;

import xadrez.modelo.pecas.Bispo;
import xadrez.modelo.pecas.Cavalo;
import xadrez.modelo.pecas.Peao;
import xadrez.modelo.pecas.Peca;
import xadrez.modelo.pecas.Rainha;
import xadrez.modelo.pecas.Rei;
import xadrez.modelo.pecas.Torre;

public final class FormataNotacaoJogada {
    public String formatarJogada(RegistroJogada registro) {
        if (registro == null) {
            return "";
        }

        StringBuilder construtor = new StringBuilder();
        Peca pecaMovida = registro.getPecaMovida();
        Posicao origem = registro.getOrigem();
        Posicao destino = registro.getDestino();

        if (registro.ehRoque()) {
            if (destino.getColuna() > origem.getColuna()) {
                construtor.append("O-O");
            } else {
                construtor.append("O-O-O");
            }
        } else if (pecaMovida instanceof Peao) {
            if (registro.ehCaptura()) {
                construtor.append(letraColuna(origem)).append('x').append(destino.paraAlgebrica());
            } else {
                construtor.append(destino.paraAlgebrica());
            }
        } else {
            construtor.append(letraPeca(pecaMovida));
            if (registro.ehCaptura()) {
                construtor.append('x');
            }
            construtor.append(destino.paraAlgebrica());
        }

        if (registro.ehPromocao()) {
            construtor.append('=').append(letraPeca(registro.getPecaResultante()));
        }

        if (registro.getEstadoResultante() == EstadoJogo.XEQUE_MATE) {
            construtor.append('#');
        } else if (registro.getEstadoResultante() == EstadoJogo.XEQUE) {
            construtor.append('+');
        }

        return construtor.toString();
    }

    public String formatarHistorico(List<RegistroJogada> historico) {
        if (historico == null || historico.isEmpty()) {
            return "";
        }

        StringBuilder construtor = new StringBuilder();
        for (RegistroJogada registro : historico) {
            if (registro.getNumeroPly() % 2 == 1) {
                if (construtor.length() > 0) {
                    construtor.append('\n');
                }
                int numeroJogada = (registro.getNumeroPly() + 1) / 2;
                construtor.append(numeroJogada).append(". ").append(formatarJogada(registro));
            } else {
                construtor.append(' ').append(formatarJogada(registro));
            }
        }
        return construtor.toString();
    }

    private String letraPeca(Peca peca) {
        if (peca instanceof Cavalo) {
            return "N";
        }
        if (peca instanceof Bispo) {
            return "B";
        }
        if (peca instanceof Torre) {
            return "R";
        }
        if (peca instanceof Rainha) {
            return "Q";
        }
        if (peca instanceof Rei) {
            return "K";
        }
        return "";
    }

    private char letraColuna(Posicao posicao) {
        return (char) ('a' + posicao.getColuna());
    }
}
