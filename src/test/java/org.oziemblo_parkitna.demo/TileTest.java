package org.oziemblo_parkitna.demo;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa TileTest testuje klasę Tile.
 */
public class TileTest {

    /**
     * Testuje inicjalizację pola.
     */
    @Test
    public void testTileInitialization() {
        Tile tile = new Tile(Color.WHITE, 2, 3);
        assertEquals(2, tile.getTileX());
        assertEquals(3, tile.getTileY());
        assertEquals(Color.WHITE, tile.getFill());
    }

    /**
     * Testuje zarządzanie pionkami na polu.
     */
    @Test
    public void testPieceManagement() {
        Tile tile = new Tile(Color.WHITE, 0, 0);
        Piece piece = new Piece(PieceType.DOG, 0, 0);

        assertFalse(tile.hasPiece());
        tile.setPiece(piece);
        assertTrue(tile.hasPiece());
        assertEquals(piece, tile.getPiece());

        tile.removePiece();
        assertFalse(tile.hasPiece());
        assertNull(tile.getPiece());
    }
}