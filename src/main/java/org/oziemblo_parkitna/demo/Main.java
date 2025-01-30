package org.oziemblo_parkitna.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Klasa Main uruchamia aplikację JavaFX.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainMenu menu = new MainMenu(primaryStage, new ClientUI());

        // Ustawienie początkowej sceny na główne menu
        primaryStage.setTitle("Warcaby");
        primaryStage.setScene(new Scene(menu.createMainMenuLayout(), 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}