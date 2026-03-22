package com.example.abyssaldescent.ui;

/**
 * Интерфейс субъекта (Subject) в паттерне Observer.
 * Объекты, которые генерируют события и уведомляют наблюдателей.
 */
public interface UISubject {
    
    /**
     * Добавить наблюдателя
     */
    void addObserver(UIObserver observer);
    
    /**
     * Удалить наблюдателя
     */
    void removeObserver(UIObserver observer);
    
    /**
     * Уведомить всех наблюдателей о событии
     */
    void notifyObservers(UIEvent event, Object data);
    
    /**
     * Уведомить конкретного наблюдателя
     */
    void notifyObserver(UIObserver observer, UIEvent event, Object data);
}
