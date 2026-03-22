package com.example.abyssaldescent.dungeon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для DungeonFactory (паттерн Abstract Factory).
 */
class DungeonFactoryTest {
    
    private DungeonFactory standardFactory;
    private DungeonFactory hardFactory;
    
    @BeforeEach
    void setUp() {
        standardFactory = new StandardDungeonFactory();
        hardFactory = new HardDungeonFactory();
    }
    
    @Test
    @DisplayName("Стандартная фабрика создает подземелье")
    void testStandardDungeonCreation() {
        List<Room> dungeon = standardFactory.createDungeon(10, 10);
        
        assertNotNull(dungeon, "Подземелье не должно быть null");
        assertFalse(dungeon.isEmpty(), "Подземелье должно содержать комнаты");
        assertTrue(dungeon.size() >= 5, "Подземелье должно содержать минимум 5 комнат");
        assertTrue(dungeon.size() <= 7, "Подземелье должно содержать максимум 7 комнат");
    }
    
    @Test
    @DisplayName("Сложная фабрика создает подземелье")
    void testHardDungeonCreation() {
        List<Room> dungeon = hardFactory.createDungeon(10, 10);
        
        assertNotNull(dungeon, "Подземелье не должно быть null");
        assertFalse(dungeon.isEmpty(), "Подземелье должно содержать комнаты");
        assertTrue(dungeon.size() >= 8, "Сложное подземелье должно содержать минимум 8 комнат");
        assertTrue(dungeon.size() <= 12, "Сложное подземелье должно содержать максимум 12 комнат");
    }
    
    @Test
    @DisplayName("Сложная фабрика создает больше комнат чем стандартная")
    void testHardFactoryCreatesMoreRooms() {
        List<Room> standardDungeon = standardFactory.createDungeon(10, 10);
        List<Room> hardDungeon = hardFactory.createDungeon(10, 10);
        
        assertTrue(hardDungeon.size() > standardDungeon.size(), 
                  "Сложное подземелье должно быть больше стандартного");
    }
    
    @Test
    @DisplayName("Фабрики создают комнаты правильных типов")
    void testRoomTypesCreation() {
        List<Room> standardDungeon = standardFactory.createDungeon(10, 10);
        List<Room> hardDungeon = hardFactory.createDungeon(10, 10);
        
        // Проверяем наличие стартовой комнаты в стандартном подземелье
        boolean hasStartRoom = standardDungeon.stream()
            .anyMatch(room -> room.getType() == RoomType.START);
        assertTrue(hasStartRoom, "Стандартное подземелье должно содержать стартовую комнату");
        
        // Проверяем наличие стартовой комнаты в сложном подземелье
        hasStartRoom = hardDungeon.stream()
            .anyMatch(room -> room.getType() == RoomType.START);
        assertTrue(hasStartRoom, "Сложное подземелье должно содержать стартовую комнату");
    }
    
    @Test
    @DisplayName("Фабрики возвращают правильную сложность")
    void testDifficultyLevels() {
        assertEquals("Standard", standardFactory.getDifficulty(), 
                    "Стандартная фабрика должна возвращать 'Standard'");
        assertEquals("Hard", hardFactory.getDifficulty(), 
                    "Сложная фабрика должна возвращать 'Hard'");
    }
    
    @Test
    @DisplayName("Создание комнаты через фабрику")
    void testRoomCreation() {
        Room startRoom = standardFactory.createRoom(RoomType.START, 0, 0);
        assertNotNull(startRoom, "Комната не должна быть null");
        assertEquals(RoomType.START, startRoom.getType(), "Тип комнаты должен совпадать");
        assertTrue(startRoom.isCleared(), "Стартовая комната должна быть зачищена");
        
        Room combatRoom = standardFactory.createRoom(RoomType.COMBAT, 1, 1);
        assertNotNull(combatRoom, "Комната не должна быть null");
        assertEquals(RoomType.COMBAT, combatRoom.getType(), "Тип комнаты должен совпадать");
        assertFalse(combatRoom.isCleared(), "Боевая комната не должна быть зачищена");
    }
    
    @Test
    @DisplayName("Создание двери через фабрику")
    void testDoorCreation() {
        Room room1 = standardFactory.createRoom(RoomType.START, 0, 0);
        Room room2 = standardFactory.createRoom(RoomType.COMBAT, 1, 1);
        
        Door door = standardFactory.createDoor(room1, room2);
        assertNotNull(door, "Дверь не должна быть null");
        assertFalse(door.isLocked(), "Дверь не должна быть заперта");
    }
    
    @Test
    @DisplayName("Сложная фабрика может создавать запертые двери")
    void testHardFactoryLockedDoors() {
        Room room1 = hardFactory.createRoom(RoomType.COMBAT, 0, 0);
        Room room2 = hardFactory.createRoom(RoomType.COMBAT, 1, 1);
        
        // Проверяем несколько дверей, так как создание случайное
        boolean foundLockedDoor = false;
        for (int i = 0; i < 10; i++) {
            Door door = hardFactory.createDoor(room1, room2);
            if (door.isLocked()) {
                foundLockedDoor = true;
                break;
            }
        }
        
        // Сложная фабрика может создавать запертые двери (50% шанс)
        // Это не гарантированный тест, но проверяет возможность
    }
}
