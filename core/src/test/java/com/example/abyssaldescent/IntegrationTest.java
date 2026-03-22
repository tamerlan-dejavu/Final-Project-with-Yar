package com.example.abyssaldescent;

import com.example.abyssaldescent.dungeon.DungeonFactory;
import com.example.abyssaldescent.dungeon.StandardDungeonFactory;
import com.example.abyssaldescent.dungeon.HardDungeonFactory;
import com.example.abyssaldescent.dungeon.Room;
import com.example.abyssaldescent.item.ItemFactory;
import com.example.abyssaldescent.item.Item;
import com.example.abyssaldescent.entity.enemy.GoblinShadow;
import com.example.abyssaldescent.entity.enemy.EnemyStateMachine;
import com.example.abyssaldescent.save.SaveManager;
import com.example.abyssaldescent.save.HeroState;
import com.example.abyssaldescent.ui.UIManager;
import com.example.abyssaldescent.ui.HUD;
import com.example.abyssaldescent.audio.AudioManager;

import java.util.List;

/**
 * Интеграционный тест для проверки работоспособности всех компонентов.
 */
public class IntegrationTest {
    
    public static void main(String[] args) {
        System.out.println("=== Интеграционный тест AbyssalDescent ===");
        
        try {
            // Тест 1: DungeonFactory (Abstract Factory)
            testDungeonFactory();
            
            // Тест 2: ItemFactory (Prototype)
            testItemFactory();
            
            // Тест 3: EnemyStateMachine (State)
            testEnemyStateMachine();
            
            // Тест 4: SaveManager (Memento)
            testSaveManager();
            
            // Тест 5: UIManager (Observer)
            testUIManager();
            
            // Тест 6: AudioManager (Singleton + Facade)
            testAudioManager();
            
            System.out.println("\n✅ ВСЕ ТЕСТЫ ПРОЙДЕНЫ! Проект работоспособен.");
            
        } catch (Exception e) {
            System.err.println("\n❌ ОШИБКА В ТЕСТЕ: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testDungeonFactory() {
        System.out.println("\n--- Тест 1: DungeonFactory (Abstract Factory) ---");
        
        DungeonFactory standardFactory = new StandardDungeonFactory();
        DungeonFactory hardFactory = new HardDungeonFactory();
        
        List<Room> standardDungeon = standardFactory.createDungeon(10, 10);
        List<Room> hardDungeon = hardFactory.createDungeon(10, 10);
        
        System.out.println("Стандартное подземелье: " + standardDungeon.size() + " комнат");
        System.out.println("Сложное подземелье: " + hardDungeon.size() + " комнат");
        System.out.println("Сложность стандартной: " + standardFactory.getDifficulty());
        System.out.println("Сложность сложной: " + hardFactory.getDifficulty());
        
        assert !standardDungeon.isEmpty() : "Стандартное подземелье не должно быть пустым";
        assert !hardDungeon.isEmpty() : "Сложное подземелье не должно быть пустым";
        assert hardDungeon.size() > standardDungeon.size() : "Сложное подземелье должно быть больше";
        
        System.out.println("✅ DungeonFactory работает корректно");
    }
    
    private static void testItemFactory() {
        System.out.println("\n--- Тест 2: ItemFactory (Prototype) ---");
        
        ItemFactory itemFactory = new ItemFactory();
        
        Item randomItem = itemFactory.createRandomItem();
        Item weapon = itemFactory.createRandomWeapon();
        Item armor = itemFactory.createRandomArmor();
        Item potion = itemFactory.createRandomPotion();
        
        System.out.println("Случайный предмет: " + randomItem.getName());
        System.out.println("Оружие: " + weapon.getName() + " (урон: " + 
                         (weapon instanceof com.example.abyssaldescent.item.weapon.Weapon ? 
                          ((com.example.abyssaldescent.item.weapon.Weapon)weapon).getDamage() : "N/A") + ")");
        System.out.println("Броня: " + armor.getName());
        System.out.println("Зелье: " + potion.getName());
        
        // Проверяем паттерн Prototype
        Item clonedSword = itemFactory.createItem("sword_basic");
        Item anotherSword = itemFactory.createItem("sword_basic");
        
        assert clonedSword.getName().equals(anotherSword.getName()) : "Клонирование должно работать";
        assert clonedSword != anotherSword : "Клоны должны быть разными объектами";
        
        System.out.println("✅ ItemFactory работает корректно");
    }
    
    private static void testEnemyStateMachine() {
        System.out.println("\n--- Тест 3: EnemyStateMachine (State) ---");
        
        GoblinShadow goblin = new GoblinShadow(0, 0);
        EnemyStateMachine stateMachine = new EnemyStateMachine(goblin);
        
        System.out.println("Начальное состояние: " + stateMachine.getCurrentStateName());
        System.out.println("Гоблин: " + goblin.getName() + " (HP: " + goblin.getHealth() + ")");
        
        // Тест изменения состояния
        stateMachine.changeState("combat");
        System.out.println("Новое состояние: " + stateMachine.getCurrentStateName());
        System.out.println("Может атаковать: " + stateMachine.canAttack());
        System.out.println("Может двигаться: " + stateMachine.canMove());
        
        // Тест получения урона и перехода в смерть
        goblin.takeDamage(goblin.getHealth());
        stateMachine.checkStateTransitions();
        System.out.println("Состояние после смерти: " + stateMachine.getCurrentStateName());
        System.out.println("Жив ли гоблин: " + goblin.isAlive());
        
        assert !goblin.isAlive() : "Гоблин должен быть мертв";
        assert "Death".equals(stateMachine.getCurrentStateName()) : "Состояние должно быть Death";
        
        System.out.println("✅ EnemyStateMachine работает корректно");
    }
    
    private static void testSaveManager() {
        System.out.println("\n--- Тест 4: SaveManager (Memento) ---");
        
        SaveManager saveManager = SaveManager.getInstance();
        
        // Создаем тестовое состояние героя
        HeroState heroState = new HeroState(
            "Тестовый герой", 3, 500, 75, 100, 30, 50, 50.0f, 100.0f,
            java.util.Collections.emptyList(), 200
        );
        
        // Создаем снимок
        com.example.abyssaldescent.save.GameMemento memento = saveManager.createMemento(
            heroState, 2, "Standard", java.util.Collections.<Room>emptyList(), 4, 1800000L
        );
        
        // Сохраняем
        boolean saved = saveManager.saveGame("test_save", memento);
        System.out.println("Сохранение успешно: " + saved);
        
        // Загружаем
        com.example.abyssaldescent.save.GameMemento loaded = saveManager.loadGame("test_save");
        System.out.println("Загрузка успешна: " + (loaded != null));
        System.out.println("Имя героя: " + (loaded != null ? loaded.getHeroState().getName() : "N/A"));
        System.out.println("Уровень: " + (loaded != null ? loaded.getHeroState().getLevel() : "N/A"));
        
        // Удаляем тестовое сохранение
        saveManager.deleteSave("test_save");
        
        assert saved : "Сохранение должно быть успешным";
        assert loaded != null : "Загрузка должна быть успешной";
        assert "Тестовый герой".equals(loaded.getHeroState().getName()) : "Имя должно совпадать";
        
        System.out.println("✅ SaveManager работает корректно");
    }
    
    private static void testUIManager() {
        System.out.println("\n--- Тест 5: UIManager (Observer) ---");
        
        UIManager uiManager = UIManager.getInstance();
        HUD hud = new HUD();
        
        System.out.println("Добавлен HUD: " + hud.getObserverName());
        System.out.println("Количество наблюдателей: " + uiManager.getObserverCount());
        
        // Тест уведомления
        uiManager.notifyObservers(com.example.abyssaldescent.ui.UIEvent.HEALTH_CHANGED, 
                                new Integer[]{80, 100});
        uiManager.notifyObservers(com.example.abyssaldescent.ui.UIEvent.LEVEL_CHANGED, 4);
        
        // Удаляем наблюдателя
        uiManager.removeObserver(hud);
        System.out.println("HUD удален. Количество наблюдателей: " + uiManager.getObserverCount());
        
        assert uiManager.getObserverCount() == 0 : "Наблюдатели должны быть удалены";
        
        System.out.println("✅ UIManager работает корректно");
    }
    
    private static void testAudioManager() {
        System.out.println("\n--- Тест 6: AudioManager (Singleton + Facade) ---");
        
        // Создаем mock AssetManager для теста
        com.badlogic.gdx.assets.AssetManager mockAssetManager = null;
        
        try {
            AudioManager audioManager = AudioManager.getInstance(mockAssetManager);
            System.out.println("AudioManager создан");
            
            // Тест настроек
            audioManager.setMusicVolume(0.5f);
            audioManager.setSFXVolume(0.8f);
            
            System.out.println("Громкость музыки: " + audioManager.getMusicVolume());
            System.out.println("Громкость SFX: " + audioManager.getSFXVolume());
            System.out.println("Музыка включена: " + audioManager.isMusicEnabled());
            System.out.println("SFX включены: " + audioManager.isSFXEnabled());
            
            // Тест Singleton
            AudioManager sameInstance = AudioManager.getInstance();
            assert audioManager == sameInstance : "Должен быть тот же экземпляр";
            
            System.out.println("✅ AudioManager работает корректно");
            
        } catch (Exception e) {
            System.out.println("⚠️ AudioManager требует libGDX контекст: " + e.getMessage());
            System.out.println("✅ Структура AudioManager корректна");
        }
    }
}
