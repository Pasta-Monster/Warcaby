package org.oziemblo_parkitna.demo;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Klasa MainMenuTest testuje klasę MainMenu.
 */
class MainMenuTest {

    @Mock
    private Stage mockPrimaryStage;

    @Mock
    private ClientUI mockClientUI;

    private MainMenu mainMenu;

    /**
     * Ustawia początkowy stan przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicjalizuje mocki
        mainMenu = new MainMenu(mockPrimaryStage, mockClientUI);
    }

    /**
     * Testuje metodę createMainMenuLayout.
     */
    @Test
    void testCreateMainMenuLayout() {
        // Sprawdzamy, czy metoda createMainMenuLayout nie rzuca wyjątków
        StackPane layout = mainMenu.createMainMenuLayout();

        // Weryfikacja, że układ został pomyślnie utworzony
        assert(layout != null);
    }

    /**
     * Testuje działanie przycisku Play.
     */
    @Test
    void testPlayButtonAction() {
        // Tworzymy layout i sprawdzamy działanie przycisku Play
        StackPane layout = mainMenu.createMainMenuLayout();
        Button playButton = (Button) layout.lookup(".button");

        // Ustawiamy, że przycisk jest widoczny i jego akcja zostanie wywołana
        playButton.fire();

        // Sprawdzamy, czy widoczność przycisków dla opcji Play została zmieniona
        Button dogButton = (Button) layout.lookup(".button");
        Button catButton = (Button) layout.lookup(".button");
        assert(dogButton.isVisible());
        assert(catButton.isVisible());
    }

    /**
     * Testuje działanie przycisku Load.
     */
    @Test
    void testLoadButtonAction() {
        // Tworzymy layout i sprawdzamy działanie przycisku Load
        StackPane layout = mainMenu.createMainMenuLayout();
        Button loadButton = (Button) layout.lookup(".button");

        // Ustawiamy, że przycisk jest widoczny i jego akcja zostanie wywołana
        loadButton.fire();

        // Sprawdzamy, czy widoczność przycisków dla opcji Load została zmieniona
        Button loadDogButton = (Button) layout.lookup(".button");
        Button loadCatButton = (Button) layout.lookup(".button");
        assert(loadDogButton.isVisible());
        assert(loadCatButton.isVisible());
    }

    /**
     * Testuje działanie przycisku "Play as Dog".
     */
    @Test
    void testDogButtonAction() {
        // Tworzymy layout i przycisk Play
        StackPane layout = mainMenu.createMainMenuLayout();
        Button playButton = (Button) layout.lookup(".button");

        // Klikamy przycisk Play, aby pokazać opcje wyboru
        playButton.fire();

        // Klikamy przycisk "Play as Dog"
        Button dogButton = (Button) layout.lookup(".button");
        dogButton.fire();

        // Weryfikacja, że CheckersGame jest tworzony z typem pionka DOG
        verify(mockClientUI, times(1)).setRole("dog");
        verify(mockClientUI, times(1)).setCheckersGame(any(CheckersGame.class));
    }

    /**
     * Testuje działanie przycisku "Play as Cat".
     */
    @Test
    void testCatButtonAction() {
        // Tworzymy layout i przycisk Play
        StackPane layout = mainMenu.createMainMenuLayout();
        Button playButton = (Button) layout.lookup(".button");

        // Klikamy przycisk Play, aby pokazać opcje wyboru
        playButton.fire();

        // Klikamy przycisk "Play as Cat"
        Button catButton = (Button) layout.lookup(".button");
        catButton.fire();

        // Weryfikacja, że CheckersGame jest tworzony z typem pionka CAT
        verify(mockClientUI, times(1)).setRole("cat");
        verify(mockClientUI, times(1)).setCheckersGame(any(CheckersGame.class));
    }

    /**
     * Testuje działanie przycisku Exit.
     */
    @Test
    void testExitButtonAction() {
        // Tworzymy layout i sprawdzamy działanie przycisku Exit
        StackPane layout = mainMenu.createMainMenuLayout();
        Button exitButton = (Button) layout.lookup(".button");

        // Ustawiamy, że akcja dla Exit zostanie wywołana
        exitButton.fire();

        // Weryfikacja, że scena została zamknięta
        verify(mockPrimaryStage, times(1)).close();
    }

    /**
     * Testuje działanie przycisku Creators.
     */
    @Test
    void testCreatorsButtonAction() {
        // Tworzymy layout i sprawdzamy działanie przycisku Creators
        StackPane layout = mainMenu.createMainMenuLayout();
        Button creatorsButton = (Button) layout.lookup(".button");

        // Klikamy przycisk Creators
        creatorsButton.fire();

        // Weryfikacja, że nowa scena z układem twórców została ustawiona
        verify(mockPrimaryStage, times(1)).setScene(any(Scene.class));
    }

    /**
     * Testuje ładowanie gry dla roli Dog.
     */
    @Test
    void testLoadGameAction() {
        // Testujemy przycisk Load Dog's Game
        StackPane layout = mainMenu.createMainMenuLayout();
        Button loadDogButton = (Button) layout.lookup(".button");

        // Sprawdzamy, czy po kliknięciu przycisku załaduje grę dla psa
        loadDogButton.fire();

        // Weryfikacja, że gra została załadowana dla roli Dog
        verify(mockClientUI, times(1)).loadGameStateFromServer("dog");
    }
}