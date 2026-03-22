package com.example.abyssaldescent.save;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Хранитель (Caretaker) для паттерна Memento.
 * Управляет сохранениями и их восстановлением.
 */
public class SaveCaretaker {
    
    private final Map<String, GameMemento> saves;
    private final List<String> saveHistory;
    private static final int MAX_SAVES = 10;
    private static final int MAX_HISTORY = 20;
    
    public SaveCaretaker() {
        this.saves = new HashMap<>();
        this.saveHistory = new ArrayList<>();
    }
    
    /**
     * Сохранить игру с указанным именем
     */
    public boolean saveGame(String saveName, GameMemento memento) {
        if (saveName == null || saveName.trim().isEmpty()) {
            return false;
        }
        
        // Проверяем лимит сохранений
        if (saves.size() >= MAX_SAVES && !saves.containsKey(saveName)) {
            // Удаляем самое старое сохранение
            String oldestSave = findOldestSave();
            if (oldestSave != null) {
                saves.remove(oldestSave);
                saveHistory.remove(oldestSave);
            }
        }
        
        saves.put(saveName, memento);
        
        // Обновляем историю
        if (!saveHistory.contains(saveName)) {
            saveHistory.add(0, saveName);
            
            // Ограничиваем размер истории
            if (saveHistory.size() > MAX_HISTORY) {
                saveHistory.remove(saveHistory.size() - 1);
            }
        }
        
        return true;
    }
    
    /**
     * Загрузить игру по имени
     */
    public GameMemento loadGame(String saveName) {
        return saves.get(saveName);
    }
    
    /**
     * Удалить сохранение
     */
    public boolean deleteSave(String saveName) {
        if (saves.remove(saveName) != null) {
            saveHistory.remove(saveName);
            return true;
        }
        return false;
    }
    
    /**
     * Получить список всех сохранений
     */
    public List<String> getAllSaveNames() {
        return new ArrayList<>(saveHistory);
    }
    
    /**
     * Получить информацию о сохранении
     */
    public GameMemento getSaveInfo(String saveName) {
        return saves.get(saveName);
    }
    
    /**
     * Проверить существование сохранения
     */
    public boolean hasSave(String saveName) {
        return saves.containsKey(saveName);
    }
    
    /**
     * Получить количество сохранений
     */
    public int getSaveCount() {
        return saves.size();
    }
    
    /**
     * Очистить все сохранения
     */
    public void clearAllSaves() {
        saves.clear();
        saveHistory.clear();
    }
    
    /**
     * Найти самое старое сохранение
     */
    private String findOldestSave() {
        String oldestSave = null;
        long oldestTime = Long.MAX_VALUE;
        
        for (Map.Entry<String, GameMemento> entry : saves.entrySet()) {
            if (entry.getValue().getTimestamp() < oldestTime) {
                oldestTime = entry.getValue().getTimestamp();
                oldestSave = entry.getKey();
            }
        }
        
        return oldestSave;
    }
    
    /**
     * Получить самое последнее сохранение
     */
    public String getLatestSave() {
        if (saveHistory.isEmpty()) {
            return null;
        }
        return saveHistory.get(0);
    }
    
    /**
     * Получить самое старое сохранение
     */
    public String getOldestSave() {
        return findOldestSave();
    }
    
    /**
     * Проверить, есть ли проблемы с сохранениями
     */
    public List<String> validateSaves() {
        List<String> issues = new ArrayList<>();
        
        for (Map.Entry<String, GameMemento> entry : saves.entrySet()) {
            GameMemento memento = entry.getValue();
            
            // Проверяем версию
            if (!memento.isCompatible("1.0.0")) {
                issues.add("Сохранение '" + entry.getKey() + "' имеет несовместимую версию");
            }
            
            // Проверяем возраст (старше 30 дней)
            if (memento.getAge() > 30L * 24 * 60 * 60 * 1000) {
                issues.add("Сохранение '" + entry.getKey() + "' очень старое");
            }
        }
        
        return issues;
    }
    
    /**
     * Получить статистику сохранений
     */
    public String getStatistics() {
        int totalSaves = saves.size();
        long totalSize = 0; // TODO: Реализовать подсчет размера
        
        StringBuilder stats = new StringBuilder();
        stats.append("Статистика сохранений:\n");
        stats.append("Всего сохранений: ").append(totalSaves).append("\n");
        stats.append("Максимальное количество: ").append(MAX_SAVES).append("\n");
        stats.append("История: ").append(saveHistory.size()).append(" из ").append(MAX_HISTORY).append("\n");
        
        if (!saveHistory.isEmpty()) {
            String latest = getLatestSave();
            GameMemento latestMemento = saves.get(latest);
            stats.append("Последнее сохранение: ").append(latest)
                 .append(" (").append(latestMemento.getFormattedAge()).append(" назад)\n");
        }
        
        return stats.toString();
    }
    
    @Override
    public String toString() {
        return "SaveCaretaker{saves=" + saves.size() + ", history=" + saveHistory.size() + "}";
    }
}
