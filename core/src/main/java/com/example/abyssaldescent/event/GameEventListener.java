package com.example.abyssaldescent.event;

/**
 * Паттерн: Observer — интерфейс наблюдателя.
 * Все компоненты подписывающиеся на события
 * реализуют этот интерфейс.
 */
public interface GameEventListener {
    void onEvent(GameEvent event);
}