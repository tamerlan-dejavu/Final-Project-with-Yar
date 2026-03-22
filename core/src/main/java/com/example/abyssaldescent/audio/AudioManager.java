package com.example.abyssaldescent.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

/**
 * Паттерн: Singleton + Facade.
 * Скрывает сложность управления объектами
 * Music и Sound из libGDX за простым интерфейсом.
 */
public class AudioManager {

    private static AudioManager instance;
    private final AssetManager assetManager;
    private final Map<String, Music> musicTracks;
    private final Map<String, Sound> soundEffects;
    
    // Настройки громкости
    private float musicVolume = 0.7f;
    private float sfxVolume = 1.0f;
    private boolean musicEnabled = true;
    private boolean sfxEnabled = true;
    
    // Текущая музыка
    private String currentMusicTrack;
    private Music currentMusic;

    private AudioManager(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.musicTracks = new HashMap<>();
        this.soundEffects = new HashMap<>();
        initializeAudioAssets();
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
    
    /**
     * Воспроизвести музыкальный трек
     */
    public void playMusic(String name) {
        if (!musicEnabled) return;
        
        Music music = musicTracks.get(name);
        if (music == null) {
            Gdx.app.log("AudioManager", "Музыкальный трек не найден: " + name);
            return;
        }
        
        // Останавливаем текущую музыку
        if (currentMusic != null && currentMusic != music) {
            currentMusic.stop();
        }
        
        currentMusic = music;
        currentMusicTrack = name;
        currentMusic.setVolume(musicVolume);
        currentMusic.setLooping(true);
        currentMusic.play();
        
        Gdx.app.log("AudioManager", "Воспроизведение музыки: " + name);
    }
    
    /**
     * Воспроизвести звуковой эффект
     */
    public void playSFX(String name) {
        if (!sfxEnabled) return;
        
        Sound sound = soundEffects.get(name);
        if (sound == null) {
            Gdx.app.log("AudioManager", "Звуковой эффект не найден: " + name);
            return;
        }
        
        sound.play(sfxVolume);
        Gdx.app.log("AudioManager", "Воспроизведение SFX: " + name);
    }
    
    /**
     * Воспроизвести звуковой эффект с указанной громкостью
     */
    public void playSFX(String name, float volume) {
        if (!sfxEnabled) return;
        
        Sound sound = soundEffects.get(name);
        if (sound == null) {
            Gdx.app.log("AudioManager", "Звуковой эффект не найден: " + name);
            return;
        }
        
        sound.play(volume * sfxVolume);
        Gdx.app.log("AudioManager", "Воспроизведение SFX: " + name + " (громкость: " + volume + ")");
    }
    
    /**
     * Установить громкость музыки
     */
    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0f, Math.min(1f, volume));
        if (currentMusic != null) {
            currentMusic.setVolume(this.musicVolume);
        }
        Gdx.app.log("AudioManager", "Громкость музыки установлена: " + this.musicVolume);
    }
    
    /**
     * Установить громкость звуковых эффектов
     */
    public void setSFXVolume(float volume) {
        this.sfxVolume = Math.max(0f, Math.min(1f, volume));
        Gdx.app.log("AudioManager", "Громкость SFX установлена: " + this.sfxVolume);
    }
    
    /**
     * Включить/выключить музыку
     */
    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled && currentMusic != null) {
            currentMusic.pause();
        } else if (enabled && currentMusic != null) {
            currentMusic.play();
        }
        Gdx.app.log("AudioManager", "Музыка " + (enabled ? "включена" : "выключена"));
    }
    
    /**
     * Включить/выключить звуковые эффекты
     */
    public void setSFXEnabled(boolean enabled) {
        this.sfxEnabled = enabled;
        Gdx.app.log("AudioManager", "SFX " + (enabled ? "включены" : "выключены"));
    }
    
    /**
     * Остановить всю музыку
     */
    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
            currentMusicTrack = null;
        }
    }
    
    /**
     * Остановить все звуки
     */
    public void stopAll() {
        stopMusic();
        
        // Останавливаем все звуковые эффекты
        for (Sound sound : soundEffects.values()) {
            sound.stop();
        }
        
        Gdx.app.log("AudioManager", "Все звуки остановлены");
    }
    
    /**
     * Получить текущий музыкальный трек
     */
    public String getCurrentMusicTrack() {
        return currentMusicTrack;
    }
    
    /**
     * Получить громкость музыки
     */
    public float getMusicVolume() {
        return musicVolume;
    }
    
    /**
     * Получить громкость SFX
     */
    public float getSFXVolume() {
        return sfxVolume;
    }
    
    /**
     * Проверить, включена ли музыка
     */
    public boolean isMusicEnabled() {
        return musicEnabled;
    }
    
    /**
     * Проверить, включены ли SFX
     */
    public boolean isSFXEnabled() {
        return sfxEnabled;
    }
    
    /**
     * Инициализация аудио ресурсов
     */
    private void initializeAudioAssets() {
        // TODO: Загрузить реальные аудио файлы через AssetManager
        // Пока используем заглушки
        
        // Музыкальные треки
        // musicTracks.put("menu_theme", assetManager.get("audio/music/menu_theme.ogg", Music.class));
        // musicTracks.put("dungeon_ambient", assetManager.get("audio/music/dungeon_ambient.ogg", Music.class));
        // musicTracks.put("combat_theme", assetManager.get("audio/music/combat_theme.ogg", Music.class));
        
        // Звуковые эффекты
        // soundEffects.put("sword_swing", assetManager.get("audio/sfx/sword_swing.ogg", Sound.class));
        // soundEffects.put("arrow_shot", assetManager.get("audio/sfx/arrow_shot.ogg", Sound.class));
        // soundEffects.put("enemy_hit", assetManager.get("audio/sfx/enemy_hit.ogg", Sound.class));
        // soundEffects.put("pickup_item", assetManager.get("audio/sfx/pickup_item.ogg", Sound.class));
        // soundEffects.put("door_open", assetManager.get("audio/sfx/door_open.ogg", Sound.class));
        
        Gdx.app.log("AudioManager", "Аудио менеджер инициализирован");
    }
    
    /**
     * Освободить ресурсы
     */
    public void dispose() {
        stopAll();
        
        for (Music music : musicTracks.values()) {
            music.dispose();
        }
        
        for (Sound sound : soundEffects.values()) {
            sound.dispose();
        }
        
        musicTracks.clear();
        soundEffects.clear();
        
        Gdx.app.log("AudioManager", "Аудио ресурсы освобождены");
    }
}