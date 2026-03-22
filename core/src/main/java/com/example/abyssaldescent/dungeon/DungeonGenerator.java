package com.example.abyssaldescent.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Генератор подземелья — создаёт список комнат для этажа.
 * Использует RoomBuilder (паттерн Builder) для создания
 * каждой комнаты.
 *
 * Структура этажа:
 *   START → COMBAT × N → TREASURE → COMBAT × M → TRANSITION
 *   (опционально ALTAR между боевыми комнатами)
 */
public class DungeonGenerator {

    private static final int MIN_ROOMS = 5;
    private static final int MAX_ROOMS = 10;

    private final Random random;

    public DungeonGenerator() {
        this.random = new Random();
    }

    public DungeonGenerator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Сгенерировать список комнат для одного этажа.
     *
     * @param floor номер этажа (влияет на количество врагов)
     * @return список комнат в порядке прохождения
     */
    public List<Room> generateFloor(int floor) {
        List<Room> rooms = new ArrayList<>();
        int totalRooms = MIN_ROOMS +
            random.nextInt(MAX_ROOMS - MIN_ROOMS + 1);

        // Стартовая комната — всегда первая
        rooms.add(buildStartRoom());

        // Боевые комнаты и опциональный алтарь
        int combatRooms = totalRooms - 3; // минус start, treasure, transition
        for (int i = 0; i < combatRooms; i++) {
            // Каждые 3 боевые комнаты — шанс алтаря
            if (i > 0 && i % 3 == 0 && random.nextFloat() < 0.4f) {
                rooms.add(buildAltarRoom(rooms.size()));
            } else {
                rooms.add(buildCombatRoom(rooms.size(), floor));
            }
        }

        // Сокровищница — перед переходом
        rooms.add(buildTreasureRoom(rooms.size()));

        // Комната перехода — всегда последняя
        rooms.add(buildTransitionRoom(rooms.size()));

        // Соединяем комнаты дверьми
        connectRooms(rooms);

        return rooms;
    }

    private Room buildStartRoom() {
        return new RoomBuilder("room_00", RoomType.START)
            .addDoor(new Door(Door.Direction.EAST, false))
            .build();
    }

    private Room buildCombatRoom(int index, int floor) {
        RoomBuilder builder = new RoomBuilder(
            "room_" + String.format("%02d", index),
            RoomType.COMBAT
        );
        // Добавляем двери — заблокированы до зачистки
        builder.addDoor(new Door(Door.Direction.WEST, true));
        if (random.nextBoolean()) {
            builder.addDoor(new Door(Door.Direction.NORTH, true));
        }
        builder.addDoor(new Door(Door.Direction.EAST, true));
        return builder.build();
    }

    private Room buildTreasureRoom(int index) {
        return new RoomBuilder(
            "room_" + String.format("%02d", index),
            RoomType.TREASURE
        )
        .addDoor(new Door(Door.Direction.WEST, false))
        .addDoor(new Door(Door.Direction.EAST, false))
        .build();
    }

    private Room buildTransitionRoom(int index) {
        return new RoomBuilder(
            "room_" + String.format("%02d", index),
            RoomType.TRANSITION
        )
        .addDoor(new Door(Door.Direction.WEST, false))
        .build();
    }

    private Room buildAltarRoom(int index) {
        return new RoomBuilder(
            "room_" + String.format("%02d", index),
            RoomType.ALTAR
        )
        .addDoor(new Door(Door.Direction.WEST, false))
        .addDoor(new Door(Door.Direction.EAST, false))
        .build();
    }

    /**
     * Соединить комнаты — каждая комната знает
     * о следующей через Door.connectedRoom.
     */
    private void connectRooms(List<Room> rooms) {
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room current = rooms.get(i);
            Room next = rooms.get(i + 1);
            // Ищем восточную дверь текущей и соединяем со следующей
            for (Door door : current.getDoors()) {
                if (door.getDirection() == Door.Direction.EAST) {
                    door.setConnectedRoom(next);
                    break;
                }
            }
        }
    }

    public int getMinRooms() { return MIN_ROOMS; }
    public int getMaxRooms() { return MAX_ROOMS; }
}
