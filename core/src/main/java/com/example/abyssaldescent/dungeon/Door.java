package com.example.abyssaldescent.dungeon;

/**
 * Дверь между комнатами подземелья.
 * Может быть заблокирована до зачистки врагов.
 */
public class Door {

    public enum Direction {
        NORTH, SOUTH, EAST, WEST
    }

    private final Direction direction;
    private boolean locked;
    private Room connectedRoom;

    public Door(Direction direction, boolean locked) {
        this.direction = direction;
        this.locked = locked;
    }

    public void unlock() {
        this.locked = false;
    }

    public void lock() {
        this.locked = true;
    }

    public boolean isLocked() {
        return locked;
    }

    public Direction getDirection() {
        return direction;
    }

    public Room getConnectedRoom() {
        return connectedRoom;
    }

    public void setConnectedRoom(Room room) {
        this.connectedRoom = room;
    }
}