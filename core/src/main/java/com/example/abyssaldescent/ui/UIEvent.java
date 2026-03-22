package com.example.abyssaldescent.ui;

/**
 * События UI для паттерна Observer.
 * Определяют типы событий, на которые могут реагировать UI компоненты.
 */
public enum UIEvent {
    
    /** Изменение здоровья героя */
    HEALTH_CHANGED,
    
    /** Изменение маны героя */
    MANA_CHANGED,
    
    /** Изменение опыта героя */
    EXPERIENCE_CHANGED,
    
    /** Добавлен предмет в инвентарь */
    ITEM_ADDED,
    
    /** Удален предмет из инвентаря */
    ITEM_REMOVED,
    
    /** Изменение количества предметов */
    ITEM_COUNT_CHANGED,
    
    /** Герой передвинулся */
    PLAYER_MOVED,
    
    /** Начало боя */
    COMBAT_STARTED,
    
    /** Окончание боя */
    COMBAT_ENDED,
    
    /** Враг побежден */
    ENEMY_DEFEATED,
    
    /** Комната зачищена */
    ROOM_CLEARED,
    
    /** Уровень изменен */
    LEVEL_CHANGED,
    
    /** Сохранение игры */
    GAME_SAVED,
    
    /** Загрузка игры */
    GAME_LOADED
}
