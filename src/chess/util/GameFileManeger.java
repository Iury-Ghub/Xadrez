package chess.util;

import chess.model.Alliance;
import chess.model.MoveRecord;
import chess.model.Position;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameFileManeger {
    public void salvarPartida(List<MoveRecord> historico, String nomeArquivo) throws IOException {
        FileWriter fileWriter = new FileWriter(nomeArquivo);
        for (MoveRecord moveRecord : historico) {
            String move = moveRecord.getFrom().toAlgebraic() + "-" + moveRecord.getTo().toAlgebraic();
            if (moveRecord.getMovedPiece().getAlliance().equals(Alliance.WHITE)) {
                fileWriter.write(move + " ");
            } else {
                fileWriter.write(move + "\n");
            }
        }
        fileWriter.close();
    }

    public List<Position[]> carregarPartida(String nomeArquivo) throws IOException {
        List<Position[]> moves = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo));
        String linha;
        while ((linha = reader.readLine()) != null) {
            String trimmed = linha.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            for (String moveStr : trimmed.split("\\s+")) {
                String[] parts = moveStr.split("-");
                Position from = Position.fromAlgebraic(parts[0]);
                Position to = Position.fromAlgebraic(parts[1]);
                moves.add(new Position[]{from, to});
            }
        }
        reader.close();
        return moves;
    }
}
