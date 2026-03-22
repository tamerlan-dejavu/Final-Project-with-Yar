package com.example.abyssaldescent.entity.enemy;

import com.example.abyssaldescent.entity.Enemy;

/**
 * Гоблин-тень - быстрый и ловкий враг.
 * Использует паттерн State для управления поведением.
 */
public class GoblinShadow extends Enemy {
    
    private static final int GOBLIN_HEALTH = 30;
    private static final int GOBLIN_DAMAGE = 8;
    private static final float DETECTION_RANGE = 80.0f;
    
    public GoblinShadow(float x, float y) {
        super("Гоблин-тень", GOBLIN_HEALTH, GOBLIN_DAMAGE, x, y);
        
        // Начинаем в состоянии патрулирования
        setState(new PatrolState());
    }
    
    @Override
    public void update(float delta) {
        super.update(delta);
        
        // Проверяем, не видит ли гоблин игрока
        if (currentState instanceof PatrolState || currentState instanceof IdleState) {
            // TODO: Добавить проверку расстояния до игрока
            // Если игрок в зоне видимости, переходим в состояние боя
            // if (distanceToPlayer < DETECTION_RANGE) {
            //     setState(new CombatState());
            // }
        }
        
        // В состоянии боя проверяем, не ушел ли игрок
        if (currentState instanceof CombatState) {
            // TODO: Добавить проверку расстояния до игрока
            // Если игрок слишком далеко, возвращаемся к патрулированию
            // if (distanceToPlayer > DETECTION_RANGE * 2) {
            //     setState(new PatrolState());
            // }
        }
    }
    
    /**
     * Гоблины могут становиться невидимыми (особая способность)
     */
    public void becomeInvisible() {
        if (currentState instanceof PatrolState) {
            System.out.println(getName() + " становится невидимым!");
            // TODO: Добавить логику невидимости
        }
    }
    
    @Override
    public void attack() {
        super.attack();
        // Особая атака гоблина - быстрый удар с шансом крита
        if (Math.random() < 0.2) { // 20% шанс крита
            System.out.println(getName() + " наносит критический удар!");
        }
    }
}
