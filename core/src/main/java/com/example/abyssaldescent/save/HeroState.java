package com.example.abyssaldescent.save;

import com.example.abyssaldescent.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Состояние героя для паттерна Memento.
 * Хранит всю информацию о персонаже для сохранения/загрузки.
 */
public class HeroState {
    
    private final String name;
    private final int level;
    private final int experience;
    private final int health;
    private final int maxHealth;
    private final int mana;
    private final int maxMana;
    private final float positionX;
    private final float positionY;
    private final List<Item> inventory;
    private final int gold;
    private final long timestamp;
    
    public HeroState(String name, int level, int experience, int health, int maxHealth,
                    int mana, int maxMana, float positionX, float positionY,
                    List<Item> inventory, int gold) {
        this.name = name;
        this.level = level;
        this.experience = experience;
        this.health = health;
        this.maxHealth = maxHealth;
        this.mana = mana;
        this.maxMana = maxMana;
        this.positionX = positionX;
        this.positionY = positionY;
        this.inventory = new ArrayList<>(inventory);
        this.gold = gold;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getMana() { return mana; }
    public int getMaxMana() { return maxMana; }
    public float getPositionX() { return positionX; }
    public float getPositionY() { return positionY; }
    public List<Item> getInventory() { return new ArrayList<>(inventory); }
    public int getGold() { return gold; }
    public long getTimestamp() { return timestamp; }
    
    /**
     * Проверить, является ли состояние устаревшим
     */
    public boolean isExpired(long maxAge) {
        return (System.currentTimeMillis() - timestamp) > maxAge;
    }
    
    @Override
    public String toString() {
        return String.format("HeroState{name='%s', level=%d, exp=%d, hp=%d/%d, mp=%d/%d, " +
                           "pos=(%.1f,%.1f), items=%d, gold=%d}",
                           name, level, experience, health, maxHealth, mana, maxMana,
                           positionX, positionY, inventory.size(), gold);
    }
}
