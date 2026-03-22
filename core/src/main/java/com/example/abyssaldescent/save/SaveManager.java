package com.example.abyssaldescent.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.example.abyssaldescent.dungeon.Room;
import com.example.abyssaldescent.ui.UIManager;
import com.example.abyssaldescent.ui.UIEvent;

import java.util.List;

/**
 * Менеджер сохранений - основной интерфейс системы сохранения.
 * Паттерн Memento - координирует работу с сохранениями.
 */
public class SaveManager {
    
    private static SaveManager instance;
    private final SaveCaretaker caretaker;
    private final Json json;
    private final String saveDirectory;
    
    private SaveManager() {
        this.caretaker = new SaveCaretaker();
        this.json = new Json();
        this.json.setOutputType(JsonWriter.OutputType.json);
        this.saveDirectory = "saves/";
        
        // Создаем директорию для сохранений
        createSaveDirectory();
        loadAllSaves();
    }
    
    public static SaveManager getInstance() {
        if (instance == null) {
            instance = new SaveManager();
        }
        return instance;
    }
    
    /**
     * Создать снимок состояния игры
     */
    public GameMemento createMemento(HeroState heroState, int dungeonLevel, String difficulty,
                                    List<Room> clearedRooms, int clearedCount, long playTime) {
        return new GameMemento(heroState, dungeonLevel, difficulty, 
                              clearedRooms, clearedCount, playTime);
    }
    
    /**
     * Сохранить игру
     */
    public boolean saveGame(String saveName, GameMemento memento) {
        try {
            // Сохраняем в памяти
            boolean success = caretaker.saveGame(saveName, memento);
            
            if (success) {
                // Сохраняем в файл
                FileHandle saveFile = Gdx.files.local(saveDirectory + saveName + ".json");
                String saveData = json.prettyPrint(memento);
                saveFile.writeString(saveData, false);
                
                Gdx.app.log("SaveManager", "Игра сохранена: " + saveName);
                
                // Уведомляем UI
                UIManager.getInstance().notifyObservers(UIEvent.GAME_SAVED, saveName);
            }
            
            return success;
        } catch (Exception e) {
            Gdx.app.error("SaveManager", "Ошибка сохранения игры: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Загрузить игру
     */
    public GameMemento loadGame(String saveName) {
        try {
            // Пытаемся загрузить из памяти
            GameMemento memento = caretaker.loadGame(saveName);
            
            if (memento == null) {
                // Если нет в памяти, загружаем из файла
                FileHandle saveFile = Gdx.files.local(saveDirectory + saveName + ".json");
                if (saveFile.exists()) {
                    String saveData = saveFile.readString();
                    memento = json.fromJson(GameMemento.class, saveData);
                    
                    // Сохраняем в память
                    caretaker.saveGame(saveName, memento);
                }
            }
            
            if (memento != null) {
                Gdx.app.log("SaveManager", "Игра загружена: " + saveName);
                
                // Уведомляем UI
                UIManager.getInstance().notifyObservers(UIEvent.GAME_LOADED, saveName);
            }
            
            return memento;
        } catch (Exception e) {
            Gdx.app.error("SaveManager", "Ошибка загрузки игры: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Удалить сохранение
     */
    public boolean deleteSave(String saveName) {
        try {
            // Удаляем из памяти
            boolean success = caretaker.deleteSave(saveName);
            
            if (success) {
                // Удаляем файл
                FileHandle saveFile = Gdx.files.local(saveDirectory + saveName + ".json");
                if (saveFile.exists()) {
                    saveFile.delete();
                }
                
                Gdx.app.log("SaveManager", "Сохранение удалено: " + saveName);
            }
            
            return success;
        } catch (Exception e) {
            Gdx.app.error("SaveManager", "Ошибка удаления сохранения: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Получить список всех сохранений
     */
    public List<String> getAllSaveNames() {
        return caretaker.getAllSaveNames();
    }
    
    /**
     * Получить информацию о сохранении
     */
    public String getSaveInfo(String saveName) {
        GameMemento memento = caretaker.getSaveInfo(saveName);
        return memento != null ? memento.toString() : "Сохранение не найдено";
    }
    
    /**
     * Проверить существование сохранения
     */
    public boolean hasSave(String saveName) {
        return caretaker.hasSave(saveName);
    }
    
    /**
     * Получить последнее сохранение
     */
    public String getLatestSave() {
        return caretaker.getLatestSave();
    }
    
    /**
     * Автосохранение
     */
    public void autoSave(GameMemento memento) {
        String autoSaveName = "autosave";
        saveGame(autoSaveName, memento);
        Gdx.app.log("SaveManager", "Автосохранение выполнено");
    }
    
    /**
     * Быстрое сохранение
     */
    public boolean quickSave(GameMemento memento) {
        String quickSaveName = "quicksave";
        return saveGame(quickSaveName, memento);
    }
    
    /**
     * Быстрая загрузка
     */
    public GameMemento quickLoad() {
        String quickSaveName = "quicksave";
        return loadGame(quickSaveName);
    }
    
    /**
     * Создать директорию для сохранений
     */
    private void createSaveDirectory() {
        FileHandle dir = Gdx.files.local(saveDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
            Gdx.app.log("SaveManager", "Создана директория сохранений: " + saveDirectory);
        }
    }
    
    /**
     * Загрузить все сохранения из файлов
     */
    private void loadAllSaves() {
        FileHandle dir = Gdx.files.local(saveDirectory);
        if (dir.exists() && dir.isDirectory()) {
            FileHandle[] saveFiles = dir.list(".json");
            
            for (FileHandle saveFile : saveFiles) {
                try {
                    String saveName = saveFile.nameWithoutExtension();
                    String saveData = saveFile.readString();
                    GameMemento memento = json.fromJson(GameMemento.class, saveData);
                    
                    caretaker.saveGame(saveName, memento);
                    Gdx.app.log("SaveManager", "Загружено сохранение: " + saveName);
                } catch (Exception e) {
                    Gdx.app.error("SaveManager", "Ошибка загрузки сохранения " + 
                                 saveFile.name() + ": " + e.getMessage());
                }
            }
        }
        
        Gdx.app.log("SaveManager", "Загружено сохранений: " + caretaker.getSaveCount());
    }
    
    /**
     * Валидация всех сохранений
     */
    public List<String> validateAllSaves() {
        return caretaker.validateSaves();
    }
    
    /**
     * Получить статистику
     */
    public String getStatistics() {
        return caretaker.getStatistics();
    }
    
    /**
     * Очистить все сохранения
     */
    public void clearAllSaves() {
        try {
            // Очищаем в памяти
            caretaker.clearAllSaves();
            
            // Удаляем файлы
            FileHandle dir = Gdx.files.local(saveDirectory);
            if (dir.exists()) {
                FileHandle[] saveFiles = dir.list();
                for (FileHandle saveFile : saveFiles) {
                    saveFile.delete();
                }
            }
            
            Gdx.app.log("SaveManager", "Все сохранения удалены");
        } catch (Exception e) {
            Gdx.app.error("SaveManager", "Ошибка очистки сохранений: " + e.getMessage());
        }
    }
    
    /**
     * Экспортировать сохранение
     */
    public boolean exportSave(String saveName, String exportPath) {
        try {
            GameMemento memento = caretaker.getSaveInfo(saveName);
            if (memento == null) {
                return false;
            }
            
            FileHandle exportFile = Gdx.files.local(exportPath);
            String saveData = json.prettyPrint(memento);
            exportFile.writeString(saveData, false);
            
            Gdx.app.log("SaveManager", "Сохранение экспортировано: " + saveName + " -> " + exportPath);
            return true;
        } catch (Exception e) {
            Gdx.app.error("SaveManager", "Ошибка экспорта сохранения: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Импортировать сохранение
     */
    public boolean importSave(String importPath, String saveName) {
        try {
            FileHandle importFile = Gdx.files.local(importPath);
            if (!importFile.exists()) {
                return false;
            }
            
            String saveData = importFile.readString();
            GameMemento memento = json.fromJson(GameMemento.class, saveData);
            
            return saveGame(saveName, memento);
        } catch (Exception e) {
            Gdx.app.error("SaveManager", "Ошибка импорта сохранения: " + e.getMessage());
            return false;
        }
    }
}
