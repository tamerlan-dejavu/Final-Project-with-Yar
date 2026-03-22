package com.example.abyssaldescent.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Паттерн: Singleton + Observer.
 * Шина событий — компоненты подписываются на события
 * и публикуют их без прямой зависимости друг от друга.
 */
public class EventBus {

    private static EventBus instance;

    private final Map<Class<? extends GameEvent>,
                  List<GameEventListener>> listeners;

    private EventBus() {
        listeners = new HashMap<>();
    }

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    /**
     * Подписаться на событие определённого типа.
     */
    public <T extends GameEvent> void register(
            Class<T> eventType,
            GameEventListener listener) {
        listeners
            .computeIfAbsent(eventType, k -> new ArrayList<>())
            .add(listener);
    }

    /**
     * Отписаться от события.
     */
    public <T extends GameEvent> void unregister(
            Class<T> eventType,
            GameEventListener listener) {
        List<GameEventListener> list = listeners.get(eventType);
        if (list != null) {
            list.remove(listener);
        }
    }

    /**
     * Опубликовать событие — все подписчики будут оповещены.
     */
    public void publish(GameEvent event) {
        List<GameEventListener> list =
            listeners.get(event.getClass());
        if (list != null) {
            for (GameEventListener l : list) {
                l.onEvent(event);
            }
        }
    }
}