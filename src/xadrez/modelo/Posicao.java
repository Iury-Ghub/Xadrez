package xadrez.modelo;

public final class Posicao {
    private final int linha;
    private final int coluna;

    public Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public String paraAlgebrica() {
        char arquivo = (char) ('a' + coluna);
        int rank = 8 - linha;
        return String.valueOf(arquivo) + rank;
    }

    public static Posicao deAlgebrica(String algebrica) {
        int coluna = algebrica.charAt(0) - 'a';
        int linha = 8 - (algebrica.charAt(1) - '0');
        return new Posicao(linha, coluna);
    }

    @Override
    public String toString() {
        return paraAlgebrica();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Posicao)) {
            return false;
        }
        Posicao outra = (Posicao) obj;
        return linha == outra.linha && coluna == outra.coluna;
    }

    @Override
    public int hashCode() {
        return 31 * linha + coluna;
    }
}
