package org.oziemblo_parkitna.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa PieceTest testuje klasę Piece.
 */
public class PieceTest {

    /**
     * Testuje inicjalizację pionka.
     */
    @Test
    public void testPieceInitialization() {
        Piece piece = new Piece(PieceType.DOG, 2, 3);

        assertEquals(PieceType.DOG, piece.getType());
        assertEquals(2, piece.getTileX());
        assertEquals(3, piece.getTileY());
        assertFalse(piece.isKing());
    }

    /**
     * Testuje ustawianie pozycji pionka.
     */
    @Test
    public void testPiecePosition() {
        Piece piece = new Piece(PieceType.CAT, 1, 4);

        assertEquals(1, piece.getTileX());
        assertEquals(4, piece.getTileY());

        piece.setTileX(3);
        piece.setTileY(5);

        assertEquals(3, piece.getTileX());
        assertEquals(5, piece.getTileY());
    }

    /**
     * Testuje promowanie pionka na króla.
     */
    @Test
    public void testPromotionToKing() {
        Piece piece = new Piece(PieceType.DOG, 0, 7);

        assertFalse(piece.isKing());
        piece.setKing(true);
        assertTrue(piece.isKing());
    }

    /**
     * Testuje aktualizację obrazu pionka.
     */
    @Test
    public void testPieceImageUpdate() {
        Piece piece = new Piece(PieceType.DOG, 0, 0);

        assertNotNull(piece.getImageView());
        piece.setKing(true); // Promowanie na króla
        assertNotNull(piece.getImageView()); // Upewnienie się, że obraz został poprawnie zaktualizowany
    }
}