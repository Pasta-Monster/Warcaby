package org.oziemblo_parkitna.demo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Klasa WinLoseWindow wyświetla okno zwycięstwa lub porażki.
 */
public class WinLoseWindow {

    /**
     * Wyświetla okno zwycięstwa lub porażki.
     *
     * @param primaryStage główna scena aplikacji
     * @param isWin        true, jeśli gracz wygrał, w przeciwnym razie false
     * @param clientUI     instancja ClientUI
     */
    public static void display(Stage primaryStage, boolean isWin, ClientUI clientUI) {
        String backgroundImage = isWin ? "win.png" : "lose.png";

        ImageView background = new ImageView(new Image(backgroundImage));
        background.setPreserveRatio(true);


        Button mainMenuButton = new Button("Menu");
        mainMenuButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");

        mainMenuButton.setOnAction(e -> {
            MainMenu menu = new MainMenu(primaryStage, clientUI);
            primaryStage.setScene(new Scene(menu.createMainMenuLayout(), 800, 600));
        });

        VBox buttonBox = new VBox(mainMenuButton);
        buttonBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane();
        root.getChildren().addAll(background, buttonBox);
        StackPane.setAlignment(buttonBox, Pos.CENTER);

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> background.setFitWidth(newVal.doubleValue()));
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> background.setFitHeight(newVal.doubleValue()));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}