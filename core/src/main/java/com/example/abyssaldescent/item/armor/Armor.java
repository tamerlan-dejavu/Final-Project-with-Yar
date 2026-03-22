package com.example.abyssaldescent.item.armor;

import com.example.abyssaldescent.item.Item;

/**
 * Броня - тип предмета с защитой.
 * Поддерживает паттерн Prototype.
 */
public class Armor extends Item {
    
    private final int defense;
    private final ArmorType type;
    
    public Armor(String name, String description, int value, int defense, ArmorType type) {
        super(name, description, value);
        this.defense = defense;
        this.type = type;
    }
    
    public int getDefense() {
        return defense;
    }
    
    public ArmorType getType() {
        return type;
    }
    
    @Override
    public Armor clone() {
        return new Armor(this.name, this.description, this.value, this.defense, this.type);
    }
    
    @Override
    public String toString() {
        return name + " [" + type + "] - DEF: " + defense + " - " + value + " gold";
    }
    
    public enum ArmorType {
        HELMET, CHEST, BOOTS, GLOVES
    }
}
