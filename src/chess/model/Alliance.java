package chess.model;

public enum Alliance {
    WHITE("Brancas"),
    BLACK("Pretas");

    private final String displayName;

    Alliance(String displayName) {
        this.displayName = displayName;
    }

    public Alliance opposite() {
        return this == WHITE ? BLACK : WHITE;
    }

    public String getDisplayName() {
        return displayName;
    }
}
