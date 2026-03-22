package com.example.abyssaldescent.entity.enemy;

import com.example.abyssaldescent.entity.Enemy;

import java.util.HashMap;
import java.util.Map;

/**
 * Машина состояний врага.
 * Управляет переходами между состояниями.
 * Паттерн State - контекст, который хранит состояния.
 */
public class EnemyStateMachine {
    
    private Enemy enemy;
    private EnemyState currentState;
    private final Map<String, EnemyState> stateCache;
    
    public EnemyStateMachine(Enemy enemy) {
        this.enemy = enemy;
        this.stateCache = new HashMap<>();
        initializeStates();
    }
    
    /**
     * Установить начальное состояние
     */
    public void setInitialState(EnemyState initialState) {
        if (currentState != null) {
            currentState.exit(enemy);
        }
        currentState = initialState;
        if (currentState != null) {
            currentState.enter(enemy);
        }
    }
    
    /**
     * Изменить состояние
     */
    public void changeState(String stateName) {
        EnemyState newState = stateCache.get(stateName);
        if (newState == null) {
            throw new IllegalArgumentException("Неизвестное состояние: " + stateName);
        }
        
        if (currentState != null) {
            currentState.exit(enemy);
        }
        currentState = newState;
        currentState.enter(enemy);
    }
    
    /**
     * Изменить состояние напрямую
     */
    public void changeState(EnemyState newState) {
        if (currentState != null) {
            currentState.exit(enemy);
        }
        currentState = newState;
        currentState.enter(enemy);
    }
    
    /**
     * Обновить текущее состояние
     */
    public void update(float delta) {
        if (currentState != null) {
            currentState.update(enemy, delta);
        }
    }
    
    /**
     * Получить текущее состояние
     */
    public EnemyState getCurrentState() {
        return currentState;
    }
    
    /**
     * Получить название текущего состояния
     */
    public String getCurrentStateName() {
        return currentState != null ? currentState.getStateName() : "None";
    }
    
    /**
     * Проверить, может ли враг атаковать
     */
    public boolean canAttack() {
        return currentState != null && currentState.canAttack();
    }
    
    /**
     * Проверить, может ли враг двигаться
     */
    public boolean canMove() {
        return currentState != null && currentState.canMove();
    }
    
    /**
     * Добавить новое состояние в кэш
     */
    public void addState(String name, EnemyState state) {
        stateCache.put(name, state);
    }
    
    /**
     * Получить состояние по имени
     */
    public EnemyState getState(String name) {
        return stateCache.get(name);
    }
    
    /**
     * Инициализация базовых состояний
     */
    private void initializeStates() {
        stateCache.put("idle", new IdleState());
        stateCache.put("patrol", new PatrolState());
        stateCache.put("combat", new CombatState());
        stateCache.put("death", new DeathState());
    }
    
    /**
     * Проверить условия для автоматических переходов состояний
     */
    public void checkStateTransitions() {
        if (currentState == null || enemy == null) {
            return;
        }
        
        // Если враг умер, переходим в состояние смерти
        if (!enemy.isAlive() && !(currentState instanceof DeathState)) {
            changeState("death");
            return;
        }
        
        // Если в состоянии смерти, не делаем больше переходов
        if (currentState instanceof DeathState) {
            return;
        }
        
        // TODO: Добавить логику автоматических переходов
        // Например, проверка расстояния до игрока для перехода в бой
        
        // Пример логики:
        // if (currentState instanceof IdleState || currentState instanceof PatrolState) {
        //     if (isPlayerInRange()) {
        //         changeState("combat");
        //     }
        // }
        // 
        // if (currentState instanceof CombatState) {
        //     if (!isPlayerInRange()) {
        //         changeState("patrol");
        //     }
        // }
    }
    
    /**
     * Получить все доступные состояния
     */
    public String[] getAvailableStates() {
        return stateCache.keySet().toArray(new String[0]);
    }
}
