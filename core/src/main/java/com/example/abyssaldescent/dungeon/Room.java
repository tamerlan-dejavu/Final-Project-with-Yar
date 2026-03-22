package com.example.abyssaldescent.dungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Комната подземелья — основная единица уровня.
 * Создаётся только через RoomBuilder (паттерн Builder).
 * Объект намеренно неизменяемый после создания через build().
 */
public class Room {

    private final RoomType type;
    private final List<Door> doors;
    private final String id;
    private boolean cleared;

    /**
     * Конструктор приватный — используй RoomBuilder.
     */
    Room(String id, RoomType type, List<Door> doors) {
        this.id = id;
        this.type = type;
        this.doors = new ArrayList<>(doors);
        this.cleared = (type == RoomType.START
                     || type == RoomType.TREASURE
                     || type == RoomType.ALTAR);
    }

    public String getId() { return id; }
    public RoomType getType() { return type; }
    public boolean isCleared() { return cleared; }

    public List<Door> getDoors() {
        return Collections.unmodifiableList(doors);
    }

    /**
     * Зачистить комнату — разблокировать все двери.
     */
    public void clear() {
        this.cleared = true;
        for (Door door : doors) {
            door.unlock();
        }
    }

    @Override
    public String toString() {
        return "Room[" + id + ", " + type + ", cleared=" + cleared + "]";
    }
}