package com.example.abyssaldescent.item;

import com.example.abyssaldescent.item.armor.Armor;
import com.example.abyssaldescent.item.consumable.Consumable;
import com.example.abyssaldescent.item.weapon.Weapon;

import java.util.HashMap;
import java.util.Map;

/**
 * Реестр прототипов предметов.
 * Паттерн Prototype - хранит эталонные экземпляры предметов для клонирования.
 */
public class ItemRegistry {
    
    private final Map<String, Item> prototypes;
    
    public ItemRegistry() {
        this.prototypes = new HashMap<>();
        initializePrototypes();
    }
    
    /**
     * Получить копию предмета по ключу
     */
    public Item getItem(String key) {
        Item prototype = prototypes.get(key);
        if (prototype == null) {
            throw new IllegalArgumentException("Неизвестный тип предмета: " + key);
        }
        return prototype.clone();
    }
    
    /**
     * Добавить новый прототип
     */
    public void addPrototype(String key, Item item) {
        prototypes.put(key, item);
    }
    
    /**
     * Получить все доступные ключи предметов
     */
    public String[] getAvailableItems() {
        return prototypes.keySet().toArray(new String[0]);
    }
    
    /**
     * Инициализация базовых прототипов предметов
     */
    private void initializePrototypes() {
        // Оружие
        addPrototype("sword_basic", new Weapon("Базовый меч", "Простой стальной меч", 50, 10, Weapon.WeaponType.MELEE));
        addPrototype("bow_basic", new Weapon("Базовый лук", "Простой деревянный лук", 40, 8, Weapon.WeaponType.RANGED));
        addPrototype("staff_basic", new Weapon("Базовый посох", "Простой магический посох", 60, 12, Weapon.WeaponType.MAGIC));
        
        addPrototype("sword_fire", new Weapon("Огненный меч", "Мечь, пылающий адским пламенем", 150, 25, Weapon.WeaponType.MELEE));
        addPrototype("bow_ice", new Weapon("Лук льда", "Лук, стреляющий ледяными стрелами", 120, 20, Weapon.WeaponType.RANGED));
        addPrototype("staff_arcane", new Weapon("Тайный посох", "Мощный посох чистой магии", 200, 35, Weapon.WeaponType.MAGIC));
        
        // Броня
        addPrototype("helmet_leather", new Armor("Кожаный шлем", "Простой кожаный шлем", 20, 3, Armor.ArmorType.HELMET));
        addPrototype("chest_iron", new Armor("Железный нагрудник", "Прочный железный нагрудник", 80, 15, Armor.ArmorType.CHEST));
        addPrototype("boots_leather", new Armor("Кожаные сапоги", "Удобные кожаные сапоги", 25, 4, Armor.ArmorType.BOOTS));
        
        addPrototype("helmet_dragon", new Armor("Шлем из драконьей чешуи", "Легендарный шлем дракона", 300, 30, Armor.ArmorType.HELMET));
        addPrototype("chest_mithril", new Armor("Мифриловый доспех", "Легкий и прочный мифриловый доспех", 500, 45, Armor.ArmorType.CHEST));
        
        // Расходуемые предметы
        addPrototype("potion_health_small", new Consumable("Малое зелье здоровья", "Восстанавливает 25 HP", 15, Consumable.ConsumableType.HEALTH_POTION, 25));
        addPrototype("potion_health_large", new Consumable("Большое зелье здоровья", "Восстанавливает 75 HP", 40, Consumable.ConsumableType.HEALTH_POTION, 75));
        addPrototype("potion_mana", new Consumable("Зелье маны", "Восстанавливает 50 MP", 25, Consumable.ConsumableType.MANA_POTION, 50));
        addPrototype("potion_strength", new Consumable("Зелье силы", "Увеличивает силу на 10", 30, Consumable.ConsumableType.STRENGTH_BOOST, 10));
    }
}
