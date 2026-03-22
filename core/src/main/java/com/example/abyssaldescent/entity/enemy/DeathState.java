package com.example.abyssaldescent.entity.enemy;

import com.example.abyssaldescent.entity.Enemy;

/**
 * Состояние смерти врага.
 * Враг мертв и не может действовать.
 */
public class DeathState implements EnemyState {
    
    @Override
    public void update(Enemy enemy, float delta) {
        // В состоянии смерти враг не обновляется
        // TODO: Запустить анимацию смерти
    }
    
    @Override
    public void enter(Enemy enemy) {
        System.out.println(enemy.getName() + " погибает!");
        // TODO: Запустить анимацию смерти
        // TODO: Выбросить лут
    }
    
    @Override
    public void exit(Enemy enemy) {
        // Из состояния смерти обычно не выходят
    }
    
    @Override
    public String getStateName() {
        return "Death";
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
