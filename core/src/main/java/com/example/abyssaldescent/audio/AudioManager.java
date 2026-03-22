package com.example.abyssaldescent.audio;

import com.badlogic.gdx.assets.AssetManager;

/**
 * Паттерн: Singleton + Facade.
 * Скрывает сложность управления объектами
 * Music и Sound из libGDX за простым интерфейсом.
 */
public class AudioManager {

    private static AudioManager instance;
    private final AssetManager assetManager;

    private AudioManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public static AudioManager getInstance(AssetManager assetManager) {
        if (instance == null) {
            instance = new AudioManager(assetManager);
        }
        return instance;
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                "AudioManager не инициализирован. " +
                "Сначала вызови getInstance(AssetManager)."
            );
        }
        return instance;
    }

    public void playMusic(String name) {
        // TODO: реализовать в Спринте 2
    }

    public void playSFX(String name) {
        // TODO: реализовать в Спринте 2
    }

    public void setMusicVolume(float volume) {
        // TODO: реализовать в Спринте 2
    }

    public void stopAll() {
        // TODO: реализовать в Спринте 2
    }
}