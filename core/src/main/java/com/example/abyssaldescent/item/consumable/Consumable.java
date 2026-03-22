package com.example.abyssaldescent.item.consumable;

import com.example.abyssaldescent.item.Item;

/**
 * Расходуемые предметы (зелья, еда).
 * Поддерживает паттерн Prototype.
 */
public class Consumable extends Item {
    
    private final ConsumableType type;
    private final int effectValue;
    
    public Consumable(String name, String description, int value, ConsumableType type, int effectValue) {
        super(name, description, value);
        this.type = type;
        this.effectValue = effectValue;
    }
    
    public ConsumableType getType() {
        return type;
    }
    
    public int getEffectValue() {
        return effectValue;
    }
    
    @Override
    public Consumable clone() {
        return new Consumable(this.name, this.description, this.value, this.type, this.effectValue);
    }
    
    @Override
    public String toString() {
        return name + " [" + type + "] - Effect: " + effectValue + " - " + value + " gold";
    }
    
    public enum ConsumableType {
        HEALTH_POTION, MANA_POTION, STRENGTH_BOOST, SPEED_BOOST
    }
}
