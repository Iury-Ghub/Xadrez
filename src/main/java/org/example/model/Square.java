package org.example.model;

public class Square {
    int line;
    int colum;

    public Square(int line, int colum) {
        this.line = line;
        this.colum = colum;
    }
    public Square(char colum, int line){
        this.colum = colum-'a';
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public int getColum() {
        return colum;
    }
}
