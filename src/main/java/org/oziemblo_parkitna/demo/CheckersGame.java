package org.oziemblo_parkitna.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa CheckersGame reprezentuje grę w warcaby z GUI i logiką gry.
 */
public class CheckersGame {
    private static final int TILE_SIZE = 75;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    private static final int KING_ROW_CAT = 0;
    private static final int KING_ROW_DOG = 7;

    private final Pane boardPane = new Pane();
    public final Tile[][] board = new Tile[WIDTH][HEIGHT];
    private final Group tileGroup = new Group();
    private final Group pieceGroup = new Group();
    private final StackPane root = new StackPane();
    private final BorderPane layout = new BorderPane();
    private final Stage primaryStage;

    private PieceType playerRole;
    private Piece selectedPiece;
    private PieceType currentPlayer;
    private ClientUI clientUI;
    public Label turnLabel;

    /**
     * Konstruktor tworzący nową grę w warcaby z określoną rolą gracza i główną sceną.
     *
     * @param playerRole    rola gracza (DOG lub CAT)
     * @param primaryStage  główna scena aplikacji JavaFX
     */
    public CheckersGame(PieceType playerRole, Stage primaryStage) {
        this(playerRole, primaryStage, null);
    }

    /**
     * Konstruktor tworzący nową grę w warcaby z określoną rolą gracza, główną sceną i stanem gry.
     *
     * @param playerRole    rola gracza (DOG lub CAT)
     * @param primaryStage  główna scena aplikacji JavaFX
     * @param gameState     początkowy stan gry
     */
    public CheckersGame(PieceType playerRole, Stage primaryStage, GameState gameState) {
        this.playerRole = playerRole;
        this.currentPlayer = PieceType.DOG; //Pies jest pierwszym graczem
        this.primaryStage = primaryStage;

        boardPane.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        boardPane.getChildren().addAll(tileGroup, pieceGroup);

        turnLabel = new Label("Tura: " + getPlayerTurnMessage());
        turnLabel.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");

        root.setStyle("-fx-background-color: B6C5B0");
        root.setPrefSize(800, 800);

        boardPane.setStyle("-fx-background-color: transparent;");
        root.getChildren().add(boardPane);
        StackPane.setAlignment(boardPane, javafx.geometry.Pos.CENTER);

        layout.setCenter(root);
        layout.setTop(turnLabel);

        Button mainMenuButton = new Button("Menu");
        mainMenuButton.setOnAction(e -> showMainMenu());

        VBox menuBox = new VBox(mainMenuButton);
        menuBox.setStyle("-fx-alignment: center; -fx-padding: 10px;");
        layout.setBottom(menuBox);

        if (gameState != null) {
            loadGameState(gameState);
        } else {
            initializeBoard();
        }
    }

    /**
     * Ustawia instancję ClientUI dla gry.
     *
     * @param clientUI instancja ClientUI
     */
    public void setClientUI(ClientUI clientUI) {
        this.clientUI = clientUI;
    }

    /**
     * Inicjalizuje planszę z polami i pionkami.
     */
    public void initializeBoard() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                Color tileColor = ((row + col) % 2 == 0) ? Color.web("#A2C2E0") : Color.web("#F1E1A6");

                Tile tile = new Tile(tileColor, col, row);
                board[col][row] = tile;
                tileGroup.getChildren().add(tile);

                if (row <= 2 && (row + col) % 2 != 0) {
                    Piece piece = createPiece(PieceType.DOG, col, row);
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);
                }

                if (row >= 5 && (row + col) % 2 != 0) {
                    Piece piece = createPiece(PieceType.CAT, col, row);
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);
                }

                tile.setOnMouseClicked(e -> {
                    if (selectedPiece != null) {
                        int newX = tile.getTileX();
                        int newY = tile.getTileY();
                        if (isValidMove(selectedPiece, board[selectedPiece.getTileX()][selectedPiece.getTileY()], tile)) {
                            int fromX = selectedPiece.getTileX();
                            int fromY = selectedPiece.getTileY();
                            if (clientUI != null) {
                                clientUI.sendMove(fromX, fromY, newX, newY);
                            }
                            selectedPiece = null;
                        } else {
                            System.out.println("Invalid move!");
                        }
                    }
                });
            }
        }
    }

    /**
     * Tworzy nowy pionek z określonym typem i pozycją.
     *
     * @param type typ pionka (DOG lub CAT)
     * @param x    współrzędna x pionka
     * @param y    współrzędna y pionka
     * @return utworzony pionek
     */
    private Piece createPiece(PieceType type, int x, int y) {
        Piece piece = new Piece(type, x, y);

        piece.setOnMouseClicked(e -> {
            if (piece.getType() == playerRole && piece.getType() == currentPlayer) {
                selectedPiece = piece;
                System.out.println("Piece selected: " + piece.getType());
            } else {
                System.out.println("It's not your turn or you cannot move this piece!");
            }
        });

        return piece;
    }

    /**
     * Przesuwa pionek z jednej pozycji na inną.
     *
     * @param fromX współrzędna x pozycji źródłowej
     * @param fromY współrzędna y pozycji źródłowej
     * @param toX   współrzędna x pozycji docelowej
     * @param toY   współrzędna y pozycji docelowej
     */
    public void movePiece(int fromX, int fromY, int toX, int toY) {
        Piece piece = board[fromX][fromY].getPiece();
        if (piece != null) {
            movePiece(piece, toX, toY);
            PieceType winner = checkWinCondition();
            if (winner != null) {
                displayWinner(winner);
            }
        } else {
            System.out.println("Invalid move: No piece at the source location.");
        }
    }

    /**
     * Sprawdza, czy ruch jest prawidłowy.
     *
     * @param piece    pionek do przesunięcia
     * @param oldTile  pole źródłowe
     * @param newTile  pole docelowe
     * @return true, jeśli ruch jest prawidłowy, w przeciwnym razie false
     */
    public boolean isValidMove(Piece piece, Tile oldTile, Tile newTile) {
        if (newTile == null || newTile.hasPiece()) {
            return false;
        }

        int dx = newTile.getTileX() - oldTile.getTileX();
        int dy = newTile.getTileY() - oldTile.getTileY();

        if (!piece.isKing()) {
            if (piece.getType() == PieceType.DOG && dy != 1 && dy != 2) {
                return false;
            }
            if (piece.getType() == PieceType.CAT && dy != -1 && dy != -2) {
                return false;
            }
        } else {
            if (Math.abs(dy) != 1 && Math.abs(dy) != 2) {
                return false;
            }
        }

        if (Math.abs(dx) == 1 && Math.abs(dy) == 1) {
            return true;
        }

        if (Math.abs(dx) == 2 && Math.abs(dy) == 2) {
            int midX = (oldTile.getTileX() + newTile.getTileX()) / 2;
            int midY = (oldTile.getTileY() + newTile.getTileY()) / 2;
            Tile middleTile = board[midX][midY];
            Piece middlePiece = middleTile.getPiece();
            if (middlePiece != null && middlePiece.getType() != piece.getType()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Przesuwa pionek na nową pozycję.
     *
     * @param piece pionek do przesunięcia
     * @param newX  współrzędna x nowej pozycji
     * @param newY  współrzędna y nowej pozycji
     */
    public void movePiece(Piece piece, int newX, int newY) {
        Tile oldTile = board[piece.getTileX()][piece.getTileY()];
        Tile newTile = board[newX][newY];

        if (Math.abs(newX - piece.getTileX()) == 2) {
            int midX = (piece.getTileX() + newX) / 2;
            int midY = (piece.getTileY() + newY) / 2;
            Tile middleTile = board[midX][midY];
            Piece capturedPiece = middleTile.getPiece();
            if (capturedPiece != null) {
                pieceGroup.getChildren().remove(capturedPiece);
                middleTile.removePiece();
            }
        }

        oldTile.removePiece();
        newTile.setPiece(piece);

        piece.setTileX(newX);
        piece.setTileY(newY);

        // Centrowanie pionków na kratkach planszy
        piece.setTranslateX(newX * TILE_SIZE + TILE_SIZE / 2 - piece.getBoundsInLocal().getWidth() / 2);
        piece.setTranslateY(newY * TILE_SIZE + TILE_SIZE / 2 - piece.getBoundsInLocal().getHeight() / 2);

        if (piece.getType() == PieceType.DOG && newY == KING_ROW_DOG) {
            promoteToKing(piece);
        }
        if (piece.getType() == PieceType.CAT && newY == KING_ROW_CAT) {
            promoteToKing(piece);
        }

        switchTurn();
    }

    /**
     * Promuje pionek na króla.
     *
     * @param piece pionek do promowania
     */
    private void promoteToKing(Piece piece) {
        piece.setKing(true);
        System.out.println(piece.getType() + " piece promoted to King!");
    }

    /**
     * Aktualizuje etykietę tury z aktualnym graczem.
     *
     * @param currentPlayer obecny gracz
     */
    public void updateTurn(String currentPlayer) {
        this.currentPlayer = PieceType.valueOf(currentPlayer);
        turnLabel.setText("Tura: " + getPlayerTurnMessage());
    }

    /**
     * Zmienia turę na kolejnego gracza.
     */
    public void switchTurn() {
        currentPlayer = (currentPlayer == PieceType.DOG) ? PieceType.CAT : PieceType.DOG;
        turnLabel.setText("Tura: " + getPlayerTurnMessage()); // Update the turn label
        System.out.println("It's now " + currentPlayer + "'s turn.");
    }

    /**
     * Zwraca komunikat wskazujący turę obecnego gracza.
     *
     * @return komunikat wskazujący turę obecnego gracza
     */
    private String getPlayerTurnMessage() {
        if (currentPlayer == playerRole) {
            return "Twoja tura!";
        } else {
            return "Tura przeciwnika!";
        }
    }

    /**
     * Sprawdza warunki zwycięstwa, aby określić, czy któryś z graczy wygrał.
     *
     * @return Typ zwycięskiego gracza lub null, jeśli nikt jeszcze nie wygrał.
     */
    public PieceType checkWinCondition() {
        boolean dogHasPieces = false;
        boolean catHasPieces = false;

        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                Piece piece = board[col][row].getPiece();
                if (piece != null) {
                    if (piece.getType() == PieceType.DOG) {
                        dogHasPieces = true;
                    } else if (piece.getType() == PieceType.CAT) {
                        catHasPieces = true;
                    }
                }
            }
        }

        if (!dogHasPieces) {
            return PieceType.CAT;
        } else if (!catHasPieces) {
            return PieceType.DOG;
        }

        return null;
    }

    /**
     * Wyświetla zwycięzcę gry.
     *
     * @param winner Typ zwycięskiego gracza
     */
    public void displayWinner(PieceType winner) {
        boolean isWin = (winner == playerRole);
        WinLoseWindow.display(primaryStage, isWin, clientUI);
    }

    /**
     * Wyświetla główne menu.
     */
    public void showMainMenu() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Menu");
        alert.setHeaderText("Zapisz i wyjdź");
        alert.setContentText("Czy chcesz zapisać grę?");

        ButtonType saveButton = new ButtonType("Zapisz i wyjdź");
        ButtonType exitButton = new ButtonType("Wyjdź be zapisywania");
        ButtonType cancelButton = new ButtonType("Anuluj");
        alert.getButtonTypes().setAll(saveButton, exitButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == saveButton) {
                saveGameState();
                MainMenu menu = new MainMenu(primaryStage, clientUI);
                primaryStage.setScene(new Scene(menu.createMainMenuLayout(), 800, 600));
            } else if (response == exitButton) {
                MainMenu menu = new MainMenu(primaryStage, clientUI);
                primaryStage.setScene(new Scene(menu.createMainMenuLayout(), 800, 600));
            }
        });
    }


    /**
     * Zapisuje aktualny stan gry.
     */
    public void saveGameState() {
        GameState gameState = new GameState();
        gameState.setCurrentPlayer(currentPlayer);

        List<PieceState> pieces = new ArrayList<>();
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                Tile tile = board[col][row];
                if (tile.hasPiece()) {
                    Piece piece = tile.getPiece();
                    pieces.add(new PieceState(piece.getType(), col, row, piece.isKing()));
                }
            }
        }
        gameState.setPieces(pieces);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(gameState);
            clientUI.sendSaveRequest(json, playerRole.toString().toLowerCase());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ładuje zapisany stan gry.
     *
     * @param gameState zapisany stan gry
     */
    public void loadGameState(GameState gameState){
        pieceGroup.getChildren().clear();
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                Tile tile = board[col][row];
                tile.removePiece();
            }
        }

        this.currentPlayer = gameState.getCurrentPlayer();
        turnLabel.setText("Turn: " + getPlayerTurnMessage());

        for (PieceState pieceState : gameState.getPieces()) {
            int col = pieceState.getX();
            int row = pieceState.getY();
            Piece piece = createPiece(pieceState.getType(), col, row);
            piece.setKing(pieceState.isKing());
            board[col][row].setPiece(piece);
            pieceGroup.getChildren().add(piece);
        }

        // Powiadamia serwer o turze obecnego gracza po załadowaniu
        if (clientUI != null) {
            clientUI.updateTurn(currentPlayer.name());
        }
    }

    /**
     * Tworzy układ gry.
     *
     * @return BorderPane reprezentujący układ gry
     */
    public BorderPane createGameLayout() {
        return layout;
    }
}