package com.example.abyssaldescent.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * Управление экранами игры.
 * Централизует переключение экранов.
 */
public class ScreenManager {

    private final Game game;

    public ScreenManager(Game game) {
        this.game = game;
    }

    public void showScreen(Screen screen) {
        Screen current = game.getScreen();
        game.setScreen(screen);
        if (current != null) {
            current.dispose();
        }
    }
}