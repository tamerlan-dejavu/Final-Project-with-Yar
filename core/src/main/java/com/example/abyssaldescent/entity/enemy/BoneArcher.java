package com.example.abyssaldescent.entity.enemy;

import com.example.abyssaldescent.entity.Enemy;

/**
 * Костяной лучник - враг дальнего боя.
 * Использует паттерн State для управления поведением.
 */
public class BoneArcher extends Enemy {
    
    private static final int ARCHER_HEALTH = 25;
    private static final int ARCHER_DAMAGE = 12;
    private static final float SHOOT_RANGE = 120.0f;
    private static final float MIN_SHOOT_RANGE = 40.0f;
    
    private float shootCooldown;
    private static final float SHOOT_INTERVAL = 2.0f;
    
    public BoneArcher(float x, float y) {
        super("Костяной лучник", ARCHER_HEALTH, ARCHER_DAMAGE, x, y);
        
        // Начинаем в состоянии покоя (лучники обычно ждут)
        setState(new IdleState());
        this.shootCooldown = 0f;
    }
    
    @Override
    public void update(float delta) {
        super.update(delta);
        
        // Обновляем кулдаун стрельбы
        if (shootCooldown > 0) {
            shootCooldown -= delta;
        }
        
        // Логика поведения лучника
        if (currentState instanceof PatrolState || currentState instanceof IdleState) {
            // TODO: Проверить расстояние до игрока
            // Если игрок в зоне стрельбы, атакуем с расстояния
            // if (distanceToPlayer <= SHOOT_RANGE && distanceToPlayer >= MIN_SHOOT_RANGE) {
            //     setState(new RangedCombatState());
            // }
        }
    }
    
    /**
     * Стрельба из лука (особая атака лучника)
     */
    public void shoot() {
        if (shootCooldown <= 0) {
            System.out.println(getName() + " выпускает стрелу!");
            // TODO: Создать объект стрелы
            shootCooldown = SHOOT_INTERVAL;
        }
    }
    
    @Override
    public void attack() {
        // Лучник предпочитает стрелять, а не атаковать в ближнем бою
        shoot();
    }
    
    /**
     * Лучники могут отступать для сохранения дистанции
     */
    public void maintainDistance() {
        if (currentState instanceof CombatState) {
            System.out.println(getName() + " отступает для сохранения дистанции");
            // TODO: Добавить логику отступления
        }
    }
}
