package com.example.abyssaldescent.dungeon;

import java.util.ArrayList;
import java.util.List;

/**
 * Паттерн: Builder.
 *
 * Роль в паттерне: ConcreteBuilder.
 * Проблема которую решает: объект Room имеет много
 * параметров конфигурации. Без Builder пришлось бы
 * создавать перегруженные конструкторы или передавать
 * null для необязательных параметров.
 *
 * Использование:
 *   Room room = new RoomBuilder("room_01", RoomType.COMBAT)
 *       .addDoor(new Door(Direction.NORTH, true))
 *       .addDoor(new Door(Direction.EAST, true))
 *       .build();
 */
public class RoomBuilder {

    private final String id;
    private final RoomType type;
    private final List<Door> doors;

    public RoomBuilder(String id, RoomType type) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException(
                "ID комнаты не может быть пустым"
            );
        }
        if (type == null) {
            throw new IllegalArgumentException(
                "Тип комнаты не может быть null"
            );
        }
        this.id = id;
        this.type = type;
        this.doors = new ArrayList<>();
    }

    /**
     * Добавить дверь в комнату.
     * Возвращает this для цепочки вызовов.
     */
    public RoomBuilder addDoor(Door door) {
        if (door == null) {
            throw new IllegalArgumentException(
                "Дверь не может быть null"
            );
        }
        doors.add(door);
        return this;
    }

    /**
     * Собрать объект Room с валидацией.
     * Боевая комната должна иметь хотя бы одну дверь.
     */
    public Room build() {
        if (type == RoomType.COMBAT && doors.isEmpty()) {
            throw new IllegalStateException(
                "Боевая комната должна иметь хотя бы одну дверь"
            );
        }
        return new Room(id, type, doors);
    }
}