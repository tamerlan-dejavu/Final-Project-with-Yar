package com.example.abyssaldescent.entity;

import com.example.abyssaldescent.entity.enemy.EnemyState;
import com.example.abyssaldescent.entity.enemy.DeathState;

/**
 * Базовый класс врага.
 * Использует паттерн State для управления поведением.
 */
public class Enemy {
    
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected float x, y;
    protected EnemyState currentState;
    
    public Enemy(String name, int maxHealth, int damage, float x, float y) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.damage = damage;
        this.x = x;
        this.y = y;
    }
    
    public void update(float delta) {
        if (currentState != null) {
            currentState.update(this, delta);
        }
    }
    
    public void setState(EnemyState newState) {
        if (currentState != null) {
            currentState.exit(this);
        }
        currentState = newState;
        if (currentState != null) {
            currentState.enter(this);
        }
    }
    
    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
        if (health <= 0 && currentState != null) {
            // Переходим в состояние смерти
            setState(new DeathState());
        }
    }
    
    public void attack() {
        if (currentState != null && currentState.canAttack()) {
            // Логика атаки
            System.out.println(name + " атакует с уроном " + damage);
        }
    }
    
    public void move(float dx, float dy) {
        if (currentState != null && currentState.canMove()) {
            x += dx;
            y += dy;
        }
    }
    
    // Getters
    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getDamage() { return damage; }
    public float getX() { return x; }
    public float getY() { return y; }
    public EnemyState getCurrentState() { return currentState; }
    
    public boolean isAlive() {
        return health > 0;
    }
    
    @Override
    public String toString() {
        return name + " [HP: " + health + "/" + maxHealth + ", State: " + 
               (currentState != null ? currentState.getStateName() : "None") + "]";
    }
}
