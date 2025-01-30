package org.oziemblo_parkitna.demo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Klasa Tile reprezentuje pojedyncze pole na planszy w grze w warcaby.
 */
public class Tile extends Rectangle {
    private Piece piece;
    private final int tileX, tileY;

    /**
     * Konstruktor tworzący nowe pole na planszy.
     *
     * @param color kolor pola
     * @param x     współrzędna x pola
     * @param y     współrzędna y pola
     */
    public Tile(Color color, int x, int y) {
        this.tileX = x;
        this.tileY = y;

        setWidth(75);
        setHeight(75);

        setFill(color);
        setStroke(Color.BLACK);

        setTranslateX(x * 75); // Ustawienie pozycji na planszy
        setTranslateY(y * 75);
    }

    /**
     * Sprawdza, czy pole ma pionek.
     *
     * @return true, jeśli pole ma pionek, w przeciwnym razie false
     */
    public boolean hasPiece() {
        return piece != null;
    }

    /**
     * Zwraca pionek na tym polu.
     *
     * @return pionek na tym polu
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Ustawia pionek na tym polu.
     *
     * @param piece pionek do ustawienia
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Usuwa pionek z tego pola.
     */
    public void removePiece() {
        this.piece = null;
    }

    /**
     * Zwraca współrzędną x pola.
     *
     * @return współrzędna x pola
     */
    public int getTileX() {
        return tileX;
    }

    /**
     * Zwraca współrzędną y pola.
     *
     * @return współrzędna y pola
     */
    public int getTileY() {
        return tileY;
    }
}