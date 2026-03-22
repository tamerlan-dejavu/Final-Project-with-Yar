package com.example.abyssaldescent.entity.enemy;

import com.example.abyssaldescent.entity.Enemy;
import com.example.abyssaldescent.entity.enemy.CaveColossus;
import com.example.abyssaldescent.entity.enemy.EnemyStateMachine;
import com.example.abyssaldescent.entity.enemy.GoblinShadow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для EnemyStateMachine и паттерна State.
 */
class EnemyStateMachineTest {
    
    private EnemyStateMachine goblinStateMachine;
    private Enemy goblin;
    private EnemyStateMachine colossusStateMachine;
    private Enemy colossus;
    
    @BeforeEach
    void setUp() {
        goblin = new GoblinShadow(0, 0);
        goblinStateMachine = new EnemyStateMachine(goblin);
        
        colossus = new CaveColossus(100, 100);
        colossusStateMachine = new EnemyStateMachine(colossus);
    }
    
    @Test
    @DisplayName("Начальное состояние врага")
    void testInitialState() {
        assertNotNull(goblinStateMachine.getCurrentState(), "Начальное состояние не должно быть null");
        assertNotNull(colossusStateMachine.getCurrentState(), "Начальное состояние не должно быть null");
        
        // Гоблин начинает с патрулирования
        assertEquals("Patrol", goblinStateMachine.getCurrentStateName(), 
                    "Гоблин должен начинать с патрулирования");
        
        // Колосс начинает с покоя
        assertEquals("Idle", colossusStateMachine.getCurrentStateName(), 
                    "Колосс должен начинать с покоя");
    }
    
    @Test
    @DisplayName("Изменение состояния по имени")
    void testChangeStateByName() {
        // Меняем состояние на бой
        goblinStateMachine.changeState("combat");
        assertEquals("Combat", goblinStateMachine.getCurrentStateName(), 
                    "Состояние должно измениться на бой");
        
        // Меняем состояние на смерть
        goblinStateMachine.changeState("death");
        assertEquals("Death", goblinStateMachine.getCurrentStateName(), 
                    "Состояние должно измениться на смерть");
    }
    
    @Test
    @DisplayName("Изменение состояния напрямую")
    void testChangeStateDirectly() {
        EnemyState combatState = new CombatState();
        goblinStateMachine.changeState(combatState);
        
        assertEquals("Combat", goblinStateMachine.getCurrentStateName(), 
                    "Состояние должно измениться на бой");
        assertSame(combatState, goblinStateMachine.getCurrentState(), 
                  "Текущее состояние должно быть тем же объектом");
    }
    
    @Test
    @DisplayName("Проверка возможностей состояний")
    void testStateCapabilities() {
        // В состоянии покоя нельзя атаковать, но можно двигаться
        colossusStateMachine.changeState("idle");
        assertFalse(colossusStateMachine.canAttack(), "В покое нельзя атаковать");
        assertTrue(colossusStateMachine.canMove(), "В покое можно двигаться");
        
        // В состоянии боя можно и атаковать, и двигаться
        colossusStateMachine.changeState("combat");
        assertTrue(colossusStateMachine.canAttack(), "В бою можно атакировать");
        assertTrue(colossusStateMachine.canMove(), "В бою можно двигаться");
        
        // В состоянии смерти нельзя ни атаковать, ни двигаться
        colossusStateMachine.changeState("death");
        assertFalse(colossusStateMachine.canAttack(), "В смерти нельзя атаковать");
        assertFalse(colossusStateMachine.canMove(), "В смерти нельзя двигаться");
    }
    
    @Test
    @DisplayName("Обновление состояния")
    void testStateUpdate() {
        goblinStateMachine.changeState("idle");
        
        // Обновляем состояние (симуляция игрового времени)
        goblinStateMachine.update(0.5f);
        assertEquals("Idle", goblinStateMachine.getCurrentStateName(), 
                    "Состояние не должно измениться при коротком обновлении");
        
        // Обновляем на достаточно долгое время для перехода
        goblinStateMachine.update(2.5f); // Итого 3.0 секунды
        // Состояние должно измениться на патрулирование
        assertNotEquals("Idle", goblinStateMachine.getCurrentStateName(), 
                       "Состояние должно измениться после времени ожидания");
    }
    
    @Test
    @DisplayName("Обработка несуществующего состояния")
    void testInvalidStateName() {
        assertThrows(IllegalArgumentException.class, () -> {
            goblinStateMachine.changeState("nonexistent_state");
        }, "Должен выбрасываться исключение для несуществующего состояния");
    }
    
    @Test
    @DisplayName("Получение доступных состояний")
    void testGetAvailableStates() {
        String[] states = goblinStateMachine.getAvailableStates();
        
        assertNotNull(states, "Массив состояний не должен быть null");
        assertTrue(states.length > 0, "Должны быть доступные состояния");
        
        boolean hasIdle = false;
        boolean hasPatrol = false;
        boolean hasCombat = false;
        boolean hasDeath = false;
        
        for (String state : states) {
            if ("idle".equals(state)) hasIdle = true;
            if ("patrol".equals(state)) hasPatrol = true;
            if ("combat".equals(state)) hasCombat = true;
            if ("death".equals(state)) hasDeath = true;
        }
        
        assertTrue(hasIdle, "Должно быть состояние покоя");
        assertTrue(hasPatrol, "Должно быть состояние патрулирования");
        assertTrue(hasCombat, "Должно быть состояние боя");
        assertTrue(hasDeath, "Должно быть состояние смерти");
    }
    
    @Test
    @DisplayName("Автоматические переходы состояний")
    void testAutomaticTransitions() {
        // Устанавливаем полное здоровье
        goblinStateMachine.changeState("patrol");
        
        // Проверяем переходы
        goblinStateMachine.checkStateTransitions();
        assertEquals("Patrol", goblinStateMachine.getCurrentStateName(), 
                    "Здоровый враг не должен менять состояние");
        
        // Наносим смертельный урон
        goblin.takeDamage(goblin.getHealth());
        
        // Проверяем переход в состояние смерти
        goblinStateMachine.checkStateTransitions();
        assertEquals("Death", goblinStateMachine.getCurrentStateName(), 
                    "Мертвый враг должен перейти в состояние смерти");
    }
    
    @Test
    @DisplayName("Особенности врагов")
    void testEnemySpecificFeatures() {
        // Проверяем гоблина
        assertTrue(goblin instanceof GoblinShadow, "Гоблин должен быть экземпляром GoblinShadow");
        assertEquals("Гоблин-тень", goblin.getName(), "Имя гоблина должно совпадать");
        assertEquals(30, goblin.getMaxHealth(), "Здоровье гоблина должно совпадать");
        assertEquals(8, goblin.getDamage(), "Урон гоблина должен совпадать");
        
        // Проверяем колосса
        assertTrue(colossus instanceof CaveColossus, "Колосс должен быть экземпляром CaveColossus");
        assertEquals("Пещерный колосс", colossus.getName(), "Имя колосса должно совпадать");
        assertEquals(80, colossus.getMaxHealth(), "Здоровье колосса должно совпадать");
        assertEquals(15, colossus.getDamage(), "Урон колосса должен совпадать");
        
        // Проверяем состояние ярости колосса
        assertFalse(((CaveColossus) colossus).isEnraged(), "Колосс не должен быть в ярости");
        
        // Наносим урон для перехода в ярость
        colossus.takeDamage(40); // 50% здоровья
        assertTrue(((CaveColossus) colossus).isEnraged(), "Колосс должен войти в ярость");
        assertEquals(22, colossus.getDamage(), "Урон должен увеличиться в ярости");
    }
    
    @Test
    @DisplayName("Методы врагов")
    void testEnemyMethods() {
        // Проверяем атаку
        assertTrue(goblinStateMachine.canAttack(), "Гоблин должен уметь атаковать");
        goblinStateMachine.changeState("combat");
        goblin.attack(); // Не должно выбросить исключение
        
        // Проверяем движение
        assertTrue(goblinStateMachine.canMove(), "Гоблин должен уметь двигаться");
        float initialX = goblin.getX();
        float initialY = goblin.getY();
        goblin.move(10, 15);
        assertEquals(initialX + 10, goblin.getX(), 0.001, "Позиция X должна измениться");
        assertEquals(initialY + 15, goblin.getY(), 0.001, "Позиция Y должна измениться");
        
        // Проверяем, что враг жив
        assertTrue(goblin.isAlive(), "Гоблин должен быть жив");
        
        // Проверяем получение урона
        int initialHealth = goblin.getHealth();
        goblin.takeDamage(5);
        assertEquals(initialHealth - 5, goblin.getHealth(), "Здоровье должно уменьшиться");
        
        // Проверяем смерть
        goblin.takeDamage(goblin.getHealth());
        assertFalse(goblin.isAlive(), "Гоблин должен быть мертв");
        assertEquals("Death", goblinStateMachine.getCurrentStateName(), 
                    "Состояние должно быть смертью");
    }
    
    @Test
    @DisplayName("Паттерн State - разные состояния")
    void testStatePattern() {
        EnemyState idleState = new IdleState();
        EnemyState patrolState = new PatrolState();
        EnemyState combatState = new CombatState();
        EnemyState deathState = new DeathState();
        
        assertEquals("Idle", idleState.getStateName(), "Имя состояния покоя должно совпадать");
        assertEquals("Patrol", patrolState.getStateName(), "Имя состояния патрулирования должно совпадать");
        assertEquals("Combat", combatState.getStateName(), "Имя состояния боя должно совпадать");
        assertEquals("Death", deathState.getStateName(), "Имя состояния смерти должно совпадать");
        
        // Проверяем возможности состояний
        assertFalse(idleState.canAttack(), "В покое нельзя атаковать");
        assertFalse(idleState.canMove(), "В покое нельзя двигаться");
        
        assertFalse(patrolState.canAttack(), "При патрулировании нельзя атаковать");
        assertTrue(patrolState.canMove(), "При патрулировании можно двигаться");
        
        assertTrue(combatState.canAttack(), "В бою можно атаковать");
        assertTrue(combatState.canMove(), "В бою можно двигаться");
        
        assertFalse(deathState.canAttack(), "В смерти нельзя атаковать");
        assertFalse(deathState.canMove(), "В смерти нельзя двигаться");
    }
}
