package org.oziemblo_parkitna.demo;

/**
 * Klasa PieceState reprezentuje stan pionka w grze w warcaby.
 */
public class PieceState {
    private PieceType type;
    private int x;
    private int y;
    private boolean isKing;

    /**
     * Konstruktor domyślny
     */
    public PieceState() {}

    /**
     * Konstruktor parametryczny
     *
     * @param type   typ pionka (DOG lub CAT)
     * @param x      współrzędna x pionka
     * @param y      współrzędna y pionka
     * @param isKing status króla pionka
     */
    public PieceState(PieceType type, int x, int y, boolean isKing) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.isKing = isKing;
    }

    /**
     * Zwraca typ pionka.
     *
     * @return typ pionka (DOG lub CAT)
     */
    public PieceType getType() {
        return type;
    }

    /**
     * Ustawia typ pionka.
     *
     * @param type typ pionka (DOG lub CAT)
     */
    public void setType(PieceType type) {
        this.type = type;
    }

    /**
     * Zwraca współrzędną x pionka.
     *
     * @return współrzędna x pionka
     */
    public int getX() {
        return x;
    }

    /**
     * Ustawia współrzędną x pionka.
     *
     * @param x nowa współrzędna x pionka
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Zwraca współrzędną y pionka.
     *
     * @return współrzędna y pionka
     */
    public int getY() {
        return y;
    }

    /**
     * Ustawia współrzędną y pionka.
     *
     * @param y nowa współrzędna y pionka
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sprawdza, czy pionek jest królem.
     *
     * @return true, jeśli pionek jest królem, w przeciwnym razie false
     */
    public boolean isKing() {
        return isKing;
    }

    /**
     * Ustawia status króla dla pionka.
     *
     * @param isKing true, jeśli pionek ma być królem, w przeciwnym razie false
     */
    public void setKing(boolean isKing) {
        this.isKing = isKing;
    }
}