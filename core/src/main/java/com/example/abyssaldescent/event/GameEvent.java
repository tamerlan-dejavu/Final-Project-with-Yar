package com.example.abyssaldescent.event;

/**
 * Базовый класс для всех игровых событий.
 * Паттерн: Observer — это объект-сообщение (Event).
 */
public abstract class GameEvent {

    private final long timestamp;

    protected GameEvent() {
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }
}