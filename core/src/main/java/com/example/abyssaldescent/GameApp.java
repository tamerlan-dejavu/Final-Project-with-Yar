package com.example.abyssaldescent;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.example.abyssaldescent.audio.AudioManager;
import com.example.abyssaldescent.core.ScreenManager;
import com.example.abyssaldescent.event.EventBus;
import com.example.abyssaldescent.screen.MenuScreen;

/**
 * Точка входа в приложение.
 * Реализует ApplicationListener через Game (libGDX).
 *
 * Паттерны инициализируемые здесь:
 *   - Singleton : AudioManager, EventBus
 *   - Facade    : AudioManager скрывает libGDX Audio API
 */
public class GameApp extends Game {

    private ScreenManager screenManager;
    private AssetManager assetManager;

    @Override
    public void create() {
        // Асинхронная загрузка ресурсов
        assetManager = new AssetManager();

        // Инициализация Singleton-ов
        EventBus.getInstance();
        AudioManager.getInstance(assetManager);

        // Менеджер экранов
        screenManager = new ScreenManager(this);

        // Первый экран — главное меню
        screenManager.showScreen(
            new MenuScreen(screenManager, assetManager)
        );

        Gdx.app.log("GameApp", "Abyssal Descent — initialized");
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
        Gdx.app.log("GameApp", "Resources disposed");
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}