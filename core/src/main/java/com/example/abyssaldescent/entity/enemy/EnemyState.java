package com.example.abyssaldescent.entity.enemy;

import com.example.abyssaldescent.entity.Enemy;

/**
 * Интерфейс состояния врага.
 * Паттерн State - определяет поведение врага в зависимости от его состояния.
 */
public interface EnemyState {
    
    /**
     * Обновить состояние врага
     * @param enemy враг, чье состояние обновляется
     * @param delta время с последнего обновления
     */
    void update(Enemy enemy, float delta);
    
    /**
     * Войти в состояние
     * @param enemy враг, входящий в состояние
     */
    void enter(Enemy enemy);
    
    /**
     * Выйти из состояния
     * @param enemy враг, выходящий из состояния
     */
    void exit(Enemy enemy);
    
    /**
     * Получить название состояния
     */
    String getStateName();
    
    /**
     * Может ли враг атаковать в этом состоянии
     */
    boolean canAttack();
    
    /**
     * Может ли враг двигаться в этом состоянии
     */
    boolean canMove();
}
