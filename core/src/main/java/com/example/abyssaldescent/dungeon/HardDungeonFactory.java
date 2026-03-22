package com.example.abyssaldescent.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Фабрика для создания подземелий высокой сложности.
 * Реализация паттерна Abstract Factory.
 */
public class HardDungeonFactory implements DungeonFactory {
    
    private final Random random;
    private static final String DIFFICULTY = "Hard";
    
    public HardDungeonFactory() {
        this.random = new Random();
    }
    
    @Override
    public List<Room> createDungeon(int width, int height) {
        List<Room> dungeon = new ArrayList<>();
        
        // Создаем стартовую комнату
        Room startRoom = createRoom(RoomType.START, 0, 0);
        dungeon.add(startRoom);
        
        // Генерируем больше комнат для сложного подземелья
        int roomCount = 8 + random.nextInt(5); // 8-12 комнат
        
        for (int i = 1; i < roomCount; i++) {
            RoomType type = getRandomRoomTypeHard();
            Room room = createRoom(type, i, 0);
            dungeon.add(room);
        }
        
        return dungeon;
    }
    
    @Override
    public Room createRoom(RoomType type, int x, int y) {
        String roomId = "hard_room_" + type.name().toLowerCase() + "_" + x + "_" + y;
        
        // Создаем комнату через RoomBuilder
        RoomBuilder builder = new RoomBuilder(roomId, type);
        
        // Для сложных комнат добавляем больше запертых дверей
        if (type == RoomType.COMBAT) {
            builder.addDoor(new Door(Door.Direction.NORTH, true));
            builder.addDoor(new Door(Door.Direction.SOUTH, true));
            builder.addDoor(new Door(Door.Direction.EAST, true)); // Дополнительная запертая дверь
        } else if (type == RoomType.START) {
            // Даже стартовая комната может иметь запертую дверь на сложном уровне
            builder.addDoor(new Door(Door.Direction.WEST, false));
        } else {
            // Остальные комнаты
            builder.addDoor(new Door(Door.Direction.EAST, false));
        }
        
        return builder.build();
    }
    
    @Override
    public Door createDoor(Room room1, Room room2) {
        // На сложном уровне двери могут быть заперты
        boolean locked = random.nextBoolean(); // 50% шанс, что дверь заперта
        Door door = new Door(Door.Direction.EAST, locked);
        door.setConnectedRoom(room2);
        return door;
    }
    
    @Override
    public String getDifficulty() {
        return DIFFICULTY;
    }
    
    private RoomType getRandomRoomTypeHard() {
        // На сложном уровне больше боевых комнат
        RoomType[] types = {RoomType.COMBAT, RoomType.COMBAT, RoomType.TREASURE, RoomType.COMBAT};
        return types[random.nextInt(types.length)];
    }
}
