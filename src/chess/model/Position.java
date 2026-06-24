package chess.model;

public final class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String toAlgebraic() {
        char file = (char) ('a' + col);
        int rank = 8 - row;
        return String.valueOf(file) + rank;
    }

    public static Position fromAlgebraic(String algebraic) {
        int col = algebraic.charAt(0) - 'a';
        int row = 8 - (algebraic.charAt(1) - '0');
        return new Position(row, col);
    }

    @Override
    public String toString() {
        return toAlgebraic();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Position)) {
            return false;
        }
        Position other = (Position) obj;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}
