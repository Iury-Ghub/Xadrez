package xadrez.modelo;

public enum Alianca {
    BRANCAS("Brancas"),
    PRETAS("Pretas");

    private final String nomeExibicao;

    Alianca(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public Alianca oposto() {
        return this == BRANCAS ? PRETAS : BRANCAS;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }
}
