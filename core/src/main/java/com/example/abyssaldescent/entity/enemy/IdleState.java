package com.example.abyssaldescent.entity.enemy;

import com.example.abyssaldescent.entity.Enemy;

/**
 * Состояние покоя врага.
 * Враг стоит на месте и ждет.
 */
public class IdleState implements EnemyState {
    
    private float waitTime;
    private static final float WAIT_DURATION = 2.0f; // 2 секунды ожидания
    
    @Override
    public void update(Enemy enemy, float delta) {
        waitTime += delta;
        
        // После ожидания переходим к патрулированию
        if (waitTime >= WAIT_DURATION) {
            enemy.setState(new PatrolState());
        }
    }
    
    @Override
    public void enter(Enemy enemy) {
        waitTime = 0f;
        System.out.println(enemy.getName() + " входит в состояние покоя");
    }
    
    @Override
    public void exit(Enemy enemy) {
        System.out.println(enemy.getName() + " выходит из состояния покоя");
    }
    
    @Override
    public String getStateName() {
        return "Idle";
    }
    
    @Override
    public boolean canAttack() {
        return false;
    }
    
    @Override
    public boolean canMove() {
        return false;
    }
}
