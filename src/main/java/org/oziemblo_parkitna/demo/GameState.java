package org.oziemblo_parkitna.demo;

import java.util.List;

/**
 * Klasa GameState reprezentuje stan gry w warcaby.
 */
public class GameState {
    private PieceType currentPlayer;
    private List<PieceState> pieces;

    /**
     * Zwraca obecnego gracza.
     *
     * @return obecny gracz
     */
    public PieceType getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Ustawia obecnego gracza.
     *
     * @param currentPlayer obecny gracz
     */
    public void setCurrentPlayer(PieceType currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Zwraca listę pionków.
     *
     * @return lista pionków
     */
    public List<PieceState> getPieces() {
        return pieces;
    }

    /**
     * Ustawia listę pionków.
     *
     * @param pieces lista pionków
     */
    public void setPieces(List<PieceState> pieces) {
        this.pieces = pieces;
    }

}