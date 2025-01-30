package org.oziemblo_parkitna.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Klasa Piece reprezentuje pionek w grze w warcaby.
 */
public class Piece extends StackPane {
    private final PieceType type;
    private int tileX, tileY;
    private boolean isKing;
    private ImageView imageView;

    /**
     * Konstruktor tworzący nowy pionek.
     *
     * @param type  typ pionka (DOG lub CAT)
     * @param tileX współrzędna x pionka na planszy
     * @param tileY współrzędna y pionka na planszy
     */
    public Piece(PieceType type, int tileX, int tileY) {
        this.type = type;
        this.tileX = tileX;
        this.tileY = tileY;
        this.isKing = false;

        updatePieceImage();

        // Ustawienie pozycji pionka na planszy (po środku pola)
        setTranslateX(tileX * 75 + 75 / 2 - 30);
        setTranslateY(tileY * 75 + 75 / 2 - 30);
    }

    /**
     * Metoda aktualizująca obraz pionka w zależności od jego typu i statusu króla.
     */
    public void updatePieceImage() {
        String imagePath;

        if (isKing) {
            imagePath = (type == PieceType.DOG) ? "/dog_king.png" : "/cat_king.png";
        } else {
            imagePath = (type == PieceType.DOG) ? "/dog.png" : "/cat.png";
        }

        Image pieceImage = new Image(getClass().getResourceAsStream(imagePath));

        if (pieceImage.isError()) {
            System.out.println("Error loading image: " + imagePath);
        }

        imageView = new ImageView(pieceImage);
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);

        getChildren().clear();
        getChildren().add(imageView);
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
     * Zwraca współrzędną x pionka na planszy.
     *
     * @return współrzędna x pionka
     */
    public int getTileX() {
        return tileX;
    }

    /**
     * Ustawia współrzędną x pionka na planszy.
     *
     * @param tileX nowa współrzędna x pionka
     */
    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    /**
     * Zwraca współrzędną y pionka na planszy.
     *
     * @return współrzędna y pionka
     */
    public int getTileY() {
        return tileY;
    }

    /**
     * Ustawia współrzędną y pionka na planszy.
     *
     * @param tileY nowa współrzędna y pionka
     */
    public void setTileY(int tileY) {
        this.tileY = tileY;
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
        updatePieceImage();
    }

    /**
     * Zwraca ImageView reprezentujący obraz pionka.
     *
     * @return ImageView obrazu pionka
     */
    public ImageView getImageView() {
        return imageView;
    }
}