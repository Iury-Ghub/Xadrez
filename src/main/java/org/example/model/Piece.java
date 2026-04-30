package org.example.model;

import org.example.Enums.ColorPiece;
import org.example.Enums.NamePiece;

public class Piece {
    NamePiece name;
    ColorPiece color;
    SquarePiece square;

    public Piece(NamePiece name, ColorPiece color) {
        this.name = name;
        this.color = color;
    }

    public NamePiece getName() {
        return name;
    }

    public ColorPiece getColor() {
        return color;
    }

    @Override
    public String toString() {
        return name.name();
    }
}
