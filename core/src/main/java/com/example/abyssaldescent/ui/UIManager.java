package com.example.abyssaldescent.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Менеджер UI - реализация паттерна Observer.
 * Управляет всеми UI компонентами и уведомлениями.
 */
public class UIManager implements UISubject {
    
    private static UIManager instance;
    private final List<UIObserver> observers;
    
    private UIManager() {
        this.observers = new ArrayList<>();
    }
    
    public static UIManager getInstance() {
        if (instance == null) {
            instance = new UIManager();
        }
        return instance;
    }
    
    @Override
    public void addObserver(UIObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            System.out.println("UIObserver добавлен: " + observer.getObserverName());
        }
    }
    
    @Override
    public void removeObserver(UIObserver observer) {
        if (observers.remove(observer)) {
            System.out.println("UIObserver удален: " + observer.getObserverName());
        }
    }
    
    @Override
    public void notifyObservers(UIEvent event, Object data) {
        for (UIObserver observer : observers) {
            try {
                observer.update(event, data);
            } catch (Exception e) {
                System.err.println("Ошибка при обновлении UIObserver " + 
                                 observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }
    
    @Override
    public void notifyObserver(UIObserver observer, UIEvent event, Object data) {
        if (observers.contains(observer)) {
            try {
                observer.update(event, data);
            } catch (Exception e) {
                System.err.println("Ошибка при обновлении UIObserver " + 
                                 observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Получить количество наблюдателей
     */
    public int getObserverCount() {
        return observers.size();
    }
    
    /**
     * Очистить всех наблюдателей
     */
    public void clearObservers() {
        observers.clear();
        System.out.println("Все UIObserver удалены");
    }
    
    /**
     * Проверить, существует ли наблюдатель
     */
    public boolean hasObserver(UIObserver observer) {
        return observers.contains(observer);
    }
}
