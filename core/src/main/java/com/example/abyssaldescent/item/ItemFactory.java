package com.example.abyssaldescent.item;

import com.example.abyssaldescent.item.armor.Armor;
import com.example.abyssaldescent.item.consumable.Consumable;
import com.example.abyssaldescent.item.weapon.Weapon;

import java.util.Random;

/**
 * Фабрика предметов с использованием паттерна Prototype.
 * Создает предметы путем клонирования прототипов из реестра.
 */
public class ItemFactory {
    
    private final ItemRegistry registry;
    private final Random random;
    
    public ItemFactory() {
        this.registry = new ItemRegistry();
        this.random = new Random();
    }
    
    /**
     * Создать случайный предмет
     */
    public Item createRandomItem() {
        String[] availableItems = registry.getAvailableItems();
        String randomKey = availableItems[random.nextInt(availableItems.length)];
        return registry.getItem(randomKey);
    }
    
    /**
     * Создать предмет по ключу
     */
    public Item createItem(String key) {
        return registry.getItem(key);
    }
    
    /**
     * Создать случайное оружие
     */
    public Weapon createRandomWeapon() {
        String[] weaponKeys = {
            "sword_basic", "bow_basic", "staff_basic",
            "sword_fire", "bow_ice", "staff_arcane"
        };
        String randomKey = weaponKeys[random.nextInt(weaponKeys.length)];
        return (Weapon) registry.getItem(randomKey);
    }
    
    /**
     * Создать случайную броню
     */
    public Armor createRandomArmor() {
        String[] armorKeys = {
            "helmet_leather", "chest_iron", "boots_leather",
            "helmet_dragon", "chest_mithril"
        };
        String randomKey = armorKeys[random.nextInt(armorKeys.length)];
        return (Armor) registry.getItem(randomKey);
    }
    
    /**
     * Создать случайное зелье
     */
    public Consumable createRandomPotion() {
        String[] potionKeys = {
            "potion_health_small", "potion_health_large", 
            "potion_mana", "potion_strength"
        };
        String randomKey = potionKeys[random.nextInt(potionKeys.length)];
        return (Consumable) registry.getItem(randomKey);
    }
    
    /**
     * Создать предмет для сокровищницы (обычно что-то ценное)
     */
    public Item createTreasureItem() {
        String[] treasureKeys = {
            "sword_fire", "bow_ice", "staff_arcane",
            "helmet_dragon", "chest_mithril",
            "potion_health_large", "potion_mana"
        };
        String randomKey = treasureKeys[random.nextInt(treasureKeys.length)];
        return registry.getItem(randomKey);
    }
    
    /**
     * Создать предмет для стартовой комнаты (базовые предметы)
     */
    public Item createStarterItem() {
        String[] starterKeys = {
            "sword_basic", "bow_basic", "staff_basic",
            "helmet_leather", "chest_iron", "boots_leather",
            "potion_health_small"
        };
        String randomKey = starterKeys[random.nextInt(starterKeys.length)];
        return registry.getItem(randomKey);
    }
    
    /**
     * Получить доступные предметы
     */
    public String[] getAvailableItems() {
        return registry.getAvailableItems();
    }
}
