package com.example.abyssaldescent.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Фабрика для создания подземелий стандартной сложности.
 * Реализация паттерна Abstract Factory.
 */
public class StandardDungeonFactory implements DungeonFactory {
    
    private final Random random;
    private static final String DIFFICULTY = "Standard";
    
    public StandardDungeonFactory() {
        this.random = new Random();
    }
    
    @Override
    public List<Room> createDungeon(int width, int height) {
        List<Room> dungeon = new ArrayList<>();
        
        // Создаем стартовую комнату
        Room startRoom = createRoom(RoomType.START, 0, 0);
        dungeon.add(startRoom);
        
        // Генерируем остальные комнаты
        int roomCount = 5 + random.nextInt(3); // 5-7 комнат
        
        for (int i = 1; i < roomCount; i++) {
            RoomType type = getRandomRoomType();
            Room room = createRoom(type, i, 0);
            dungeon.add(room);
        }
        
        return dungeon;
    }
    
    @Override
    public Room createRoom(RoomType type, int x, int y) {
        String roomId = "room_" + type.name().toLowerCase() + "_" + x + "_" + y;
        
        // Создаем комнату через RoomBuilder
        RoomBuilder builder = new RoomBuilder(roomId, type);
        
        // Добавляем двери в зависимости от типа комнаты
        if (type == RoomType.COMBAT) {
            // Для боевых комнат добавляем запертые двери
            builder.addDoor(new Door(Door.Direction.NORTH, true));
            builder.addDoor(new Door(Door.Direction.SOUTH, true));
        } else if (type == RoomType.START || type == RoomType.TREASURE || type == RoomType.ALTAR) {
            // Для не-боевых комнат двери не заперты
            builder.addDoor(new Door(Door.Direction.EAST, false));
        }
        
        return builder.build();
    }
    
    @Override
    public Door createDoor(Room room1, Room room2) {
        // Создаем простую дверь между комнатами
        Door door = new Door(Door.Direction.EAST, false);
        door.setConnectedRoom(room2);
        return door;
    }
    
    @Override
    public String getDifficulty() {
        return DIFFICULTY;
    }
    
    private RoomType getRandomRoomType() {
        RoomType[] types = {RoomType.COMBAT, RoomType.TREASURE, RoomType.COMBAT};
        return types[random.nextInt(types.length)];
    }
}
