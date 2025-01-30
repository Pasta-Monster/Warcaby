package org.oziemblo_parkitna.demo;

import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Klasa CheckersGameTest testuje klasę CheckersGame.
 */
class CheckersGameTest {

    @Mock
    private ClientUI mockClientUI;

    @Mock
    private Stage mockStage;

    private CheckersGame game;

    /**
     * Ustawia początkowy stan przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicjalizuje mocki
        // Inicjalizacja gry
        game = new CheckersGame(PieceType.DOG, mockStage);
        game.setClientUI(mockClientUI);
    }

    /**
     * Testuje metodę movePiece dla prawidłowego ruchu.
     */
    @Test
    void testMovePieceValidMove() {
        // Zakładając, że mamy odpowiedni stan planszy, sprawdzamy, czy ruch jest prawidłowy
        Piece dogPiece = game.board[1][2].getPiece();
        Tile destinationTile = game.board[2][3]; // Prosty, dozwolony ruch

        // Testowanie metody movePiece
        game.movePiece(1, 2, 2, 3);

        // Weryfikacja ruchu pionka
        verify(mockClientUI, times(1)).sendMove(1, 2, 2, 3);
    }

    /**
     * Testuje metodę movePiece dla nieprawidłowego ruchu.
     */
    @Test
    void testMovePieceInvalidMove() {
        // Próba wykonania nieprawidłowego ruchu (np. zbyt daleki ruch)
        Piece dogPiece = game.board[1][2].getPiece();
        Tile destinationTile = game.board[3][4]; // Niedozwolony ruch

        // Testowanie metody movePiece
        game.movePiece(1, 2, 3, 4);

        // Sprawdzamy, że nie wysłano ruchu do ClientUI
        verify(mockClientUI, never()).sendMove(1, 2, 3, 4);
    }

    /**
     * Testuje metodę updateTurn.
     */
    @Test
    void testUpdateTurn() {
        // Zakładając, że zmieniamy turę na "CAT", sprawdzamy czy etykieta jest aktualizowana
        game.updateTurn("CAT");

        // Weryfikujemy, że etykieta została zaktualizowana
        assert(game.turnLabel.getText().equals("Turn: Opponent's turn!"));
    }

    /**
     * Testuje metodę checkWinCondition, zakładając, że wygrywa DOG.
     */
    @Test
    void testCheckWinConditionWithDog() {
        // Sprawdzamy, czy metoda checkWinCondition prawidłowo zidentyfikuje zwycięzcę (DOG)
        PieceType winner = game.checkWinCondition();

        // Sprawdzamy, czy DOG jest zwycięzcą
        assert(winner == PieceType.DOG);
    }

    /**
     * Testuje metodę checkWinCondition, zakładając, że wygrywa CAT.
     */
    @Test
    void testCheckWinConditionWithCat() {
        // Sprawdzamy, czy metoda checkWinCondition prawidłowo zidentyfikuje zwycięzcę (CAT)

        // Usuwamy wszystkie pionki DOG
        for (int row = 0; row < game.HEIGHT; row++) {
            for (int col = 0; col < game.WIDTH; col++) {
                if (game.board[col][row].hasPiece() && game.board[col][row].getPiece().getType() == PieceType.DOG) {
                    game.board[col][row].removePiece();
                }
            }
        }

        PieceType winner = game.checkWinCondition();

        // Sprawdzamy, czy CAT jest zwycięzcą
        assert(winner == PieceType.CAT);
    }

    /**
     * Testuje metodę displayWinner.
     */
    @Test
    void testDisplayWinner() {
        // Testujemy metodę displayWinner
        game.displayWinner(PieceType.DOG);

        // Weryfikacja, czy wyświetlany jest odpowiedni alert
        verify(mockClientUI, times(1)).sendSaveRequest(any(), any());
    }

    /**
     * Testuje metodę saveGameState.
     */
    @Test
    void testSaveGameState() throws IOException {
        // Mockujemy stan gry
        GameState mockGameState = mock(GameState.class);
        when(mockGameState.getCurrentPlayer()).thenReturn(PieceType.DOG);

        // Testujemy metodę saveGameState
        game.saveGameState();

        // Weryfikacja, że stan gry został zapisany i wysłany do ClientUI
        verify(mockClientUI, times(1)).sendSaveRequest(any(), any());
    }

    /**
     * Testuje metodę showMainMenu.
     */
    @Test
    void testShowMainMenu() {
        // Testujemy, czy funkcja showMainMenu działa poprawnie (wyskakuje okno dialogowe)
        game.showMainMenu();

        // Sprawdzamy, czy alert został pokazany
        verify(mockStage, times(1)).show();
    }

}