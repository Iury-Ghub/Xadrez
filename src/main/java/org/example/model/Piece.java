package org.example.model;

import org.example.Enums.ColorPiece;
import org.example.Enums.NamePiece;

public class Piece {
    NamePiece name;
    ColorPiece color;
    Square square;

    public Piece(NamePiece name, Square square,ColorPiece color) {
        this.name = name;
        this.square = square;
        this.color = color;
    }

    public NamePiece getName() {
        return name;
    }

    public ColorPiece getColor() {
        return color;
    }

    public Square getSquare(){
        return square;
    }

    @Override
    public String toString() {
        return name.name();
    }
}
