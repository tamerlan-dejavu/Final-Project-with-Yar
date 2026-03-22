package com.example.abyssaldescent.entity.enemy;

import com.example.abyssaldescent.entity.Enemy;

/**
 * Состояние боя врага.
 * Враг атакует игрока.
 */
public class CombatState implements EnemyState {
    
    private float attackCooldown;
    private static final float ATTACK_INTERVAL = 1.5f; // Атака каждые 1.5 секунды
    
    @Override
    public void update(Enemy enemy, float delta) {
        attackCooldown += delta;
        
        // Атакуем по кулдауну
        if (attackCooldown >= ATTACK_INTERVAL) {
            enemy.attack();
            attackCooldown = 0f;
        }
        
        // TODO: Добавить логику преследования игрока
        // Пока просто стоим на месте и атакуем
    }
    
    @Override
    public void enter(Enemy enemy) {
        attackCooldown = 0f;
        System.out.println(enemy.getName() + " вступает в бой!");
    }
    
    @Override
    public void exit(Enemy enemy) {
        System.out.println(enemy.getName() + " выходит из боя");
    }
    
    @Override
    public String getStateName() {
        return "Combat";
    }
    
    @Override
    public boolean canAttack() {
        return true;
    }
    
    @Override
    public boolean canMove() {
        return true; // Может двигаться для преследования
    }
}
