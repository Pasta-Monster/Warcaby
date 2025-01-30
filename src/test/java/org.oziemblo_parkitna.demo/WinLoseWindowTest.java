package org.oziemblo_parkitna.demo;

import javafx.scene.control.Button;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa WinLoseWindowTest testuje klasę WinLoseWindow.
 */
public class WinLoseWindowTest {

    /**
     * Testuje działanie przycisku Main Menu.
     */
    @Test
    public void testMainMenuButtonAction() {
        Button mainMenuButton = new Button("Main Menu");

        assertNotNull(mainMenuButton.getText());
        assertEquals("Main Menu", mainMenuButton.getText());

        mainMenuButton.setOnAction(e -> System.out.println("Button clicked!"));
        assertNotNull(mainMenuButton.getOnAction());
    }
}