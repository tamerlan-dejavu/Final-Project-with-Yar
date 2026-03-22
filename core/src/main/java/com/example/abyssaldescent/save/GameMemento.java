package com.example.abyssaldescent.save;

import com.example.abyssaldescent.dungeon.Room;

import java.util.List;

/**
 * Снимок состояния игры (Memento) для паттерна Memento.
 * Хранит полное состояние игры для сохранения/восстановления.
 */
public class GameMemento {
    
    private final HeroState heroState;
    private final int currentDungeonLevel;
    private final String currentDifficulty;
    private final List<Room> dungeonState;
    private final int clearedRooms;
    private final long playTime;
    private final String version;
    private final long timestamp;
    
    public GameMemento(HeroState heroState, int currentDungeonLevel, String currentDifficulty,
                      List<Room> dungeonState, int clearedRooms, long playTime) {
        this.heroState = heroState;
        this.currentDungeonLevel = currentDungeonLevel;
        this.currentDifficulty = currentDifficulty;
        this.dungeonState = dungeonState;
        this.clearedRooms = clearedRooms;
        this.playTime = playTime;
        this.version = "1.0.0";
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public HeroState getHeroState() { return heroState; }
    public int getCurrentDungeonLevel() { return currentDungeonLevel; }
    public String getCurrentDifficulty() { return currentDifficulty; }
    public List<Room> getDungeonState() { return dungeonState; }
    public int getClearedRooms() { return clearedRooms; }
    public long getPlayTime() { return playTime; }
    public String getVersion() { return version; }
    public long getTimestamp() { return timestamp; }
    
    /**
     * Проверить совместимость версии сохранения
     */
    public boolean isCompatible(String currentVersion) {
        return this.version.equals(currentVersion);
    }
    
    /**
     * Получить возраст сохранения в миллисекундах
     */
    public long getAge() {
        return System.currentTimeMillis() - timestamp;
    }
    
    /**
     * Получить возраст сохранения в человекочитаемом формате
     */
    public String getFormattedAge() {
        long age = getAge();
        long seconds = age / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        if (hours > 0) {
            return String.format("%d ч %d мин", hours, minutes % 60);
        } else if (minutes > 0) {
            return String.format("%d мин %d сек", minutes, seconds % 60);
        } else {
            return String.format("%d сек", seconds);
        }
    }
    
    @Override
    public String toString() {
        return String.format("GameMemento{level=%d, difficulty='%s', cleared=%d, " +
                           "playTime=%d min, age='%s', version='%s'}",
                           currentDungeonLevel, currentDifficulty, clearedRooms,
                           playTime / 60000, getFormattedAge(), version);
    }
}
