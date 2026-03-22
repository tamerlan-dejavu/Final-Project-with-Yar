package com.example.abyssaldescent.entity.enemy;

import com.example.abyssaldescent.entity.Enemy;

/**
 * Пещерный колосс - большой и сильный враг.
 * Использует паттерн State для управления поведением.
 */
public class CaveColossus extends Enemy {
    
    private static final int COLOSSUS_HEALTH = 80;
    private static final int COLOSSUS_DAMAGE = 15;
    private static final float CHARGE_RANGE = 60.0f;
    
    private boolean isEnraged;
    private float enrageThreshold;
    
    public CaveColossus(float x, float y) {
        super("Пещерный колосс", COLOSSUS_HEALTH, COLOSSUS_DAMAGE, x, y);
        
        // Колоссы обычно начинают в состоянии покоя
        setState(new IdleState());
        this.isEnraged = false;
        this.enrageThreshold = COLOSSUS_HEALTH / 2; // Ярость при 50% здоровья
    }
    
    @Override
    public void update(float delta) {
        super.update(delta);
        
        // Проверяем, не нужно ли входить в ярость
        if (!isEnraged && health <= enrageThreshold) {
            enterEnrageMode();
        }
        
        // Логика поведения колосса
        if (currentState instanceof PatrolState || currentState instanceof IdleState) {
            // TODO: Проверить расстояние до игрока
            // Колосс медленный, но начинает рывок, если игрок близко
            // if (distanceToPlayer < CHARGE_RANGE) {
            //     setState(new ChargeState());
            // }
        }
    }
    
    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        
        // Колосс может войти в ярость от получения урона
        if (!isEnraged && health <= enrageThreshold) {
            enterEnrageMode();
        }
    }
    
    /**
     * Режим ярости колосса
     */
    private void enterEnrageMode() {
        isEnraged = true;
        System.out.println(getName() + " входит в режим ярости!");
        
        // Увеличиваем урон
        this.damage = (int) (COLOSSUS_DAMAGE * 1.5);
        
        // Меняем состояние на агрессивное
        if (!(currentState instanceof CombatState)) {
            setState(new CombatState());
        }
    }
    
    /**
     * Мощная атака колосса
     */
    public void groundSmash() {
        if (isEnraged) {
            System.out.println(getName() + " совершает сокрушительный удар по земле!");
            // TODO: Создать эффект удара по земле
        }
    }
    
    @Override
    public void attack() {
        super.attack();
        
        // В ярости может совершать особую атаку
        if (isEnraged && Math.random() < 0.3) { // 30% шанс
            groundSmash();
        }
    }
    
    /**
     * Проверка, находится ли колосс в ярости
     */
    public boolean isEnraged() {
        return isEnraged;
    }
}
