package xadrez.util;

import xadrez.modelo.Alianca;
import xadrez.modelo.RegistroJogada;
import xadrez.modelo.Posicao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivoJogo {
    public void salvarPartida(List<RegistroJogada> historico, String nomeArquivo) throws IOException {
        try (FileWriter escritor = new FileWriter(nomeArquivo)) {
            for (RegistroJogada registro : historico) {
                String jogada = registro.getOrigem().paraAlgebrica() + "-" + registro.getDestino().paraAlgebrica();
                if (registro.getPecaMovida().getAlianca().equals(Alianca.BRANCAS)) {
                    escritor.write(jogada + " ");
                } else {
                    escritor.write(jogada + "\n");
                }
            }
        }
    }

    public List<Posicao[]> carregarPartida(String nomeArquivo) throws IOException {
        List<Posicao[]> jogadas = new ArrayList<>();
        try (BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String aparada = linha.trim();
                if (aparada.isEmpty()) {
                    continue;
                }
                for (String jogadaStr : aparada.split("\\s+")) {
                    String[] partes = jogadaStr.split("-");
                    Posicao origem = Posicao.deAlgebrica(partes[0]);
                    Posicao destino = Posicao.deAlgebrica(partes[1]);
                    jogadas.add(new Posicao[]{origem, destino});
                }
            }
        }
        return jogadas;
    }
}
