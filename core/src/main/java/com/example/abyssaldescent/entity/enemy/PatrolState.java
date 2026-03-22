package com.example.abyssaldescent.entity.enemy;

import com.example.abyssaldescent.entity.Enemy;
import java.util.Random;

/**
 * Состояние патрулирования врага.
 * Враг движется по заданной области.
 */
public class PatrolState implements EnemyState {
    
    private final Random random = new Random();
    private float moveTimer;
    private float moveDuration;
    private float targetX, targetY;
    private static final float PATROL_SPEED = 50.0f;
    private static final float AREA_SIZE = 100.0f;
    
    public PatrolState() {
        this.moveTimer = 0f;
        this.moveDuration = 1.0f + random.nextFloat() * 2.0f; // 1-3 секунды
        this.targetX = random.nextFloat() * AREA_SIZE - AREA_SIZE / 2;
        this.targetY = random.nextFloat() * AREA_SIZE - AREA_SIZE / 2;
    }
    
    @Override
    public void update(Enemy enemy, float delta) {
        moveTimer += delta;
        
        // Движемся к цели
        float dx = targetX - enemy.getX();
        float dy = targetY - enemy.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        if (distance > 1.0f) {
            // Нормализуем направление и двигаемся
            float moveX = (dx / distance) * PATROL_SPEED * delta;
            float moveY = (dy / distance) * PATROL_SPEED * delta;
            enemy.move(moveX, moveY);
        }
        
        // Если достигли цели или время вышло, выбираем новую цель
        if (distance <= 1.0f || moveTimer >= moveDuration) {
            if (random.nextFloat() < 0.3f) {
                // 30% шанс перейти в состояние покоя
                enemy.setState(new IdleState());
            } else {
                // Выбираем новую цель для патрулирования
                moveTimer = 0f;
                moveDuration = 1.0f + random.nextFloat() * 2.0f;
                targetX = random.nextFloat() * AREA_SIZE - AREA_SIZE / 2;
                targetY = random.nextFloat() * AREA_SIZE - AREA_SIZE / 2;
            }
        }
    }
    
    @Override
    public void enter(Enemy enemy) {
        System.out.println(enemy.getName() + " начинает патрулирование");
    }
    
    @Override
    public void exit(Enemy enemy) {
        System.out.println(enemy.getName() + " прекращает патрулирование");
    }
    
    @Override
    public String getStateName() {
        return "Patrol";
    }
    
    @Override
    public boolean canAttack() {
        return false;
    }
    
    @Override
    public boolean canMove() {
        return true;
    }
}
