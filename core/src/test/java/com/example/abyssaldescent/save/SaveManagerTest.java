package com.example.abyssaldescent.save;

import com.example.abyssaldescent.dungeon.Room;
import com.example.abyssaldescent.item.Item;
import com.example.abyssaldescent.item.weapon.Weapon;
import com.example.abyssaldescent.test.GdxTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для SaveManager и паттерна Memento.
 */
class SaveManagerTest extends GdxTestBase {
    
    private SaveManager saveManager;
    private HeroState testHeroState;
    private GameMemento testMemento;
    
    @BeforeEach
    void setUp() {
        saveManager = SaveManager.getInstance();
        
        // Создаем тестовое состояние героя
        List<Item> testInventory = new ArrayList<>();
        testInventory.add(new Weapon("Тестовый меч", "Тестовое описание", 100, 15, Weapon.WeaponType.MELEE));
        
        testHeroState = new HeroState(
            "Тестовый герой", 5, 1000, 80, 100, 50, 60, 100.0f, 200.0f, 
            testInventory, 500
        );
        
        testMemento = new GameMemento(
            testHeroState, 3, "Hard", new ArrayList<Room>(), 5, 3600000L // 1 час игры
        );
    }
    
    @AfterEach
    void tearDown() {
        saveManager.clearAllSaves();
    }
    
    @Test
    @DisplayName("Создание снимка состояния игры")
    void testCreateMemento() {
        GameMemento memento = saveManager.createMemento(
            testHeroState, 1, "Standard", new ArrayList<Room>(), 3, 1800000L
        );
        
        assertNotNull(memento, "Снимок не должен быть null");
        assertEquals(testHeroState, memento.getHeroState(), "Состояние героя должно совпадать");
        assertEquals(1, memento.getCurrentDungeonLevel(), "Уровень подземелья должен совпадать");
        assertEquals("Standard", memento.getCurrentDifficulty(), "Сложность должна совпадать");
        assertEquals(3, memento.getClearedRooms(), "Количество зачищенных комнат должно совпадать");
        assertEquals(1800000L, memento.getPlayTime(), "Время игры должно совпадать");
    }
    
    @Test
    @DisplayName("Сохранение и загрузка игры")
    void testSaveAndLoadGame() {
        String saveName = "test_save";
        
        // Сохраняем игру
        boolean saveResult = saveManager.saveGame(saveName, testMemento);
        assertTrue(saveResult, "Сохранение должно быть успешным");
        
        // Проверяем, что сохранение существует
        assertTrue(saveManager.hasSave(saveName), "Сохранение должно существовать");
        
        // Загружаем игру
        GameMemento loadedMemento = saveManager.loadGame(saveName);
        assertNotNull(loadedMemento, "Загруженный снимок не должен быть null");
        
        // Проверяем данные
        assertEquals(testMemento.getHeroState().getName(), 
                    loadedMemento.getHeroState().getName(), "Имя героя должно совпадать");
        assertEquals(testMemento.getCurrentDungeonLevel(), 
                    loadedMemento.getCurrentDungeonLevel(), "Уровень должен совпадать");
        assertEquals(testMemento.getCurrentDifficulty(), 
                    loadedMemento.getCurrentDifficulty(), "Сложность должна совпадать");
    }
    
    @Test
    @DisplayName("Удаление сохранения")
    void testDeleteSave() {
        String saveName = "test_delete";
        
        // Сохраняем игру
        assertTrue(saveManager.saveGame(saveName, testMemento), "Сохранение должно быть успешным");
        assertTrue(saveManager.hasSave(saveName), "Сохранение должно существовать");
        
        // Удаляем сохранение
        boolean deleteResult = saveManager.deleteSave(saveName);
        assertTrue(deleteResult, "Удаление должно быть успешным");
        
        // Проверяем, что сохранения больше нет
        assertFalse(saveManager.hasSave(saveName), "Сохранение не должно существовать после удаления");
    }
    
    @Test
    @DisplayName("Получение списка сохранений")
    void testGetAllSaveNames() {
        String save1 = "test_save_1";
        String save2 = "test_save_2";
        String save3 = "test_save_3";
        
        // Создаем несколько сохранений
        assertTrue(saveManager.saveGame(save1, testMemento), "Сохранение 1 должно быть успешным");
        assertTrue(saveManager.saveGame(save2, testMemento), "Сохранение 2 должно быть успешным");
        assertTrue(saveManager.saveGame(save3, testMemento), "Сохранение 3 должно быть успешным");
        
        // Получаем список
        List<String> saveNames = saveManager.getAllSaveNames();
        assertEquals(3, saveNames.size(), "Должно быть 3 сохранения");
        assertTrue(saveNames.contains(save1), "Список должен содержать сохранение 1");
        assertTrue(saveNames.contains(save2), "Список должен содержать сохранение 2");
        assertTrue(saveNames.contains(save3), "Список должен содержать сохранение 3");
    }
    
    @Test
    @DisplayName("Быстрое сохранение и загрузка")
    void testQuickSaveAndLoad() {
        // Быстрое сохранение
        boolean quickSaveResult = saveManager.quickSave(testMemento);
        assertTrue(quickSaveResult, "Быстрое сохранение должно быть успешным");
        
        // Быстрая загрузка
        GameMemento quickLoaded = saveManager.quickLoad();
        assertNotNull(quickLoaded, "Быстрая загрузка должна вернуть снимок");
        
        assertEquals(testMemento.getHeroState().getName(), 
                    quickLoaded.getHeroState().getName(), "Имя героя должно совпадать");
    }
    
    @Test
    @DisplayName("Автосохранение")
    void testAutoSave() {
        // Автосохранение
        saveManager.autoSave(testMemento);
        
        // Проверяем, что автосохранение существует
        assertTrue(saveManager.hasSave("autosave"), "Автосохранение должно существовать");
        
        GameMemento autoSave = saveManager.loadGame("autosave");
        assertNotNull(autoSave, "Автосохранение должно загружаться");
        assertEquals(testMemento.getHeroState().getName(), 
                    autoSave.getHeroState().getName(), "Данные должны совпадать");
    }
    
    @Test
    @DisplayName("Получение информации о сохранении")
    void testGetSaveInfo() {
        String saveName = "test_info";
        saveManager.saveGame(saveName, testMemento);
        
        String info = saveManager.getSaveInfo(saveName);
        assertNotNull(info, "Информация о сохранении не должна быть null");
        assertTrue(info.contains("GameMemento"), "Информация должна содержать тип");
        assertTrue(info.contains("Hard"), "Информация должна содержать сложность");
        assertTrue(info.contains("3"), "Информация должна содержать уровень");
    }
    
    @Test
    @DisplayName("Получение последнего сохранения")
    void testGetLatestSave() {
        String save1 = "old_save";
        String save2 = "new_save";
        
        // Создаем сохранения с задержкой
        saveManager.saveGame(save1, testMemento);
        try {
            Thread.sleep(10); // Небольшая задержка
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        saveManager.saveGame(save2, testMemento);
        
        String latest = saveManager.getLatestSave();
        assertEquals(save2, latest, "Последним сохранением должно быть новое");
    }
    
    @Test
    @DisplayName("HeroState проверка")
    void testHeroState() {
        assertEquals("Тестовый герой", testHeroState.getName(), "Имя героя должно совпадать");
        assertEquals(5, testHeroState.getLevel(), "Уровень должен совпадать");
        assertEquals(1000, testHeroState.getExperience(), "Опыт должен совпадать");
        assertEquals(80, testHeroState.getHealth(), "Здоровье должно совпадать");
        assertEquals(100, testHeroState.getMaxHealth(), "Максимальное здоровье должно совпадать");
        assertEquals(50, testHeroState.getMana(), "Мана должна совпадать");
        assertEquals(60, testHeroState.getMaxMana(), "Максимальная мана должна совпадать");
        assertEquals(100.0f, testHeroState.getPositionX(), 0.001, "Позиция X должна совпадать");
        assertEquals(200.0f, testHeroState.getPositionY(), 0.001, "Позиция Y должна совпадать");
        assertEquals(500, testHeroState.getGold(), "Золото должно совпадать");
        assertEquals(1, testHeroState.getInventory().size(), "В инвентаре должен быть 1 предмет");
    }
    
    @Test
    @DisplayName("GameMemento проверка возраста")
    void testMementoAge() {
        long initialAge = testMemento.getAge();
        assertTrue(initialAge >= 0, "Возраст должен быть неотрицательным");
        
        String formattedAge = testMemento.getFormattedAge();
        assertNotNull(formattedAge, "Форматированный возраст не должен быть null");
        assertFalse(formattedAge.isEmpty(), "Форматированный возраст не должен быть пустым");
    }
    
    @Test
    @DisplayName("Обработка несуществующего сохранения")
    void testLoadNonExistentSave() {
        GameMemento loaded = saveManager.loadGame("nonexistent_save");
        assertNull(loaded, "Загрузка несуществующего сохранения должна вернуть null");
    }
    
    @Test
    @DisplayName("Статистика сохранений")
    void testGetStatistics() {
        String save1 = "stat_test_1";
        String save2 = "stat_test_2";
        
        saveManager.saveGame(save1, testMemento);
        saveManager.saveGame(save2, testMemento);
        
        String stats = saveManager.getStatistics();
        assertNotNull(stats, "Статистика не должна быть null");
        assertTrue(stats.contains("Всего сохранений: 2"), "Статистика должна содержать количество");
        assertTrue(stats.contains("История:"), "Статистика должна содержать историю");
    }
}
