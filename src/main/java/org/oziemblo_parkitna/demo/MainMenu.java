package org.oziemblo_parkitna.demo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Klasa MainMenu reprezentuje główne menu aplikacji.
 */
public class MainMenu {
    private Stage primaryStage;
    private ClientUI clientUI;

    /**
     * Konstruktor tworzący nowe główne menu.
     *
     * @param primaryStage główna scena aplikacji
     * @param clientUI     instancja ClientUI
     */
    public MainMenu(Stage primaryStage, ClientUI clientUI) {
        this.primaryStage = primaryStage;
        this.clientUI = clientUI;
    }

    /**
     * Tworzy układ głównego menu.
     *
     * @return StackPane reprezentujący układ głównego menu
     */
    public StackPane createMainMenuLayout() {
        // Załadowanie tła
        ImageView background = new ImageView(new Image("mainmenu.png"));
        background.setFitWidth(primaryStage.getWidth());
        background.setFitHeight(primaryStage.getHeight());
        background.setPreserveRatio(false);

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> background.setFitWidth(newVal.doubleValue()));
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> background.setFitHeight(newVal.doubleValue()));

        // Tworzenie głównych przycisków
        Button playButton = new Button("Graj");
        Button loadButton = new Button("Wczytaj grę");
        Button creatorsButton = new Button("Twórcy");
        Button exitButton = new Button("Wyjście");

        playButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
        loadButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
        creatorsButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
        exitButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");


        // Tworzenie opcji gry
        Button dogButton = new Button("Graj jako Pies");
        Button catButton = new Button("Graj jako Kot");
        dogButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
        catButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
        dogButton.setVisible(false);
        catButton.setVisible(false);

        // Tworzenie opcji ładowania
        Button loadDogButton = new Button("Załaduj grę Psa");
        Button loadCatButton = new Button("Załaduj grę Kota");
        loadDogButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
        loadCatButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
        loadDogButton.setVisible(false);
        loadCatButton.setVisible(false);

        playButton.setOnAction(e -> {
            boolean arePlayOptionsVisible = dogButton.isVisible();
            dogButton.setVisible(!arePlayOptionsVisible);
            catButton.setVisible(!arePlayOptionsVisible);
        });

        loadButton.setOnAction(e -> {
            boolean areLoadOptionsVisible = loadDogButton.isVisible();
            loadDogButton.setVisible(!areLoadOptionsVisible);
            loadCatButton.setVisible(!areLoadOptionsVisible);
        });

        // Gra jako pies
        dogButton.setOnAction(e -> {
            CheckersGame game = new CheckersGame(PieceType.DOG, primaryStage);
            clientUI.setRole("dog");
            game.setClientUI(clientUI);
            clientUI.setCheckersGame(game);
            primaryStage.setScene(new Scene(game.createGameLayout(), 800, 800));
        });

        // Gra jako kot
        catButton.setOnAction(e -> {
            CheckersGame game = new CheckersGame(PieceType.CAT, primaryStage);
            clientUI.setRole("cat");
            game.setClientUI(clientUI);
            clientUI.setCheckersGame(game);
            primaryStage.setScene(new Scene(game.createGameLayout(), 800, 800));
        });

        // Ładowanie gry psa
        loadDogButton.setOnAction(e -> loadGame("dog"));

        // Ładowanie gry kota
        loadCatButton.setOnAction(e -> loadGame("cat"));

        // Przycisk "Twórcy"
        creatorsButton.setOnAction(e -> {
            StackPane creatorsLayout = createCreatorsLayout();
            primaryStage.setScene(new Scene(creatorsLayout, 800, 600));
        });

        // Przycisk "Wyjście"
        exitButton.setOnAction(e -> primaryStage.close());

        HBox mainButtonBox = new HBox(20, playButton, loadButton, creatorsButton, exitButton);
        mainButtonBox.setAlignment(Pos.CENTER);

        HBox playOptionsBox = new HBox(20, dogButton, catButton);
        playOptionsBox.setAlignment(Pos.CENTER);

        HBox loadOptionsBox = new HBox(20, loadDogButton, loadCatButton);
        loadOptionsBox.setAlignment(Pos.CENTER);

        VBox buttonBox = new VBox(20, mainButtonBox, playOptionsBox, loadOptionsBox);
        buttonBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane();
        root.getChildren().addAll(background, buttonBox);
        StackPane.setAlignment(buttonBox, Pos.CENTER);

        return root;
    }

    /**
     * Ładuje grę z serwera.
     *
     * @param role rola gracza (DOG lub CAT)
     */
    private void loadGame(String role) {
        CheckersGame game = new CheckersGame(PieceType.valueOf(role.toUpperCase()), primaryStage);
        game.setClientUI(clientUI);
        clientUI.setCheckersGame(game);
        clientUI.loadGameStateFromServer(role);
        primaryStage.setScene(new Scene(game.createGameLayout(), 800, 800));
    }

    /**
     * Tworzy układ ekranu twórców.
     *
     * @return StackPane reprezentujący układ ekranu twórców
     */
    private StackPane createCreatorsLayout() {
        // Load the background image for creators screen
        ImageView background = new ImageView(new Image("creators.png"));
        background.setFitWidth(primaryStage.getWidth());
        background.setFitHeight(primaryStage.getHeight());
        background.setPreserveRatio(false);


        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> background.setFitWidth(newVal.doubleValue()));
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> background.setFitHeight(newVal.doubleValue()));

        Button mainMenuButton = new Button("Menu");
        mainMenuButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
        mainMenuButton.setOnAction(event -> primaryStage.setScene(new Scene(createMainMenuLayout(), 800, 600)));

        VBox buttonBox = new VBox(mainMenuButton);
        buttonBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane();
        root.getChildren().addAll(background, buttonBox);
        StackPane.setAlignment(buttonBox, Pos.CENTER);

        return root;
    }
}