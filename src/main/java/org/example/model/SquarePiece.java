package org.example.model;

public class SquarePiece {
    char line;
    char colum;

    public SquarePiece(char line, char colum) {
        this.line = line;
        this.colum = colum;
    }

    public char getLine() {
        return line;
    }

    public void setLine(char line) {
        this.line = line;
    }

    public char getColum() {
        return colum;
    }

    public void setColum(char colum) {
        this.colum = colum;
    }
}
