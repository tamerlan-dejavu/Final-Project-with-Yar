package com.example.abyssaldescent.ui;

/**
 * Интерфейс наблюдателя (Observer) в паттерне Observer.
 * UI компоненты, которые должны реагировать на изменения в игре.
 */
public interface UIObserver {
    
    /**
     * Обновить состояние UI компонента
     * @param event событие, вызвавшее обновление
     * @param data данные, связанные с событием
     */
    void update(UIEvent event, Object data);
    
    /**
     * Получить имя наблюдателя
     */
    String getObserverName();
}
