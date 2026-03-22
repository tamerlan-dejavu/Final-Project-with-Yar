package com.example.abyssaldescent.item.weapon;

import com.example.abyssaldescent.item.Item;

/**
 * Оружие - тип предмета с атакой.
 * Поддерживает паттерн Prototype.
 */
public class Weapon extends Item {
    
    private final int damage;
    private final WeaponType type;
    
    public Weapon(String name, String description, int value, int damage, WeaponType type) {
        super(name, description, value);
        this.damage = damage;
        this.type = type;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public WeaponType getType() {
        return type;
    }
    
    @Override
    public Weapon clone() {
        return new Weapon(this.name, this.description, this.value, this.damage, this.type);
    }
    
    @Override
    public String toString() {
        return name + " [" + type + "] - DMG: " + damage + " - " + value + " gold";
    }
    
    public enum WeaponType {
        MELEE, RANGED, MAGIC
    }
}
