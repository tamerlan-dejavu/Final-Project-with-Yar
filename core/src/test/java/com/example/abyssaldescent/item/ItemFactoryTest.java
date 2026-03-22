package com.example.abyssaldescent.item;

import com.example.abyssaldescent.item.armor.Armor;
import com.example.abyssaldescent.item.consumable.Consumable;
import com.example.abyssaldescent.item.weapon.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для ItemFactory и ItemRegistry (паттерн Prototype).
 */
class ItemFactoryTest {
    
    private ItemFactory itemFactory;
    
    @BeforeEach
    void setUp() {
        itemFactory = new ItemFactory();
    }
    
    @Test
    @DisplayName("Создание случайного предмета")
    void testCreateRandomItem() {
        Item item = itemFactory.createRandomItem();
        
        assertNotNull(item, "Предмет не должен быть null");
        assertNotNull(item.getName(), "У предмета должно быть имя");
        assertTrue(item.getValue() >= 0, "Ценность предмета должна быть неотрицательной");
    }
    
    @Test
    @DisplayName("Создание предмета по ключу")
    void testCreateItemByKey() {
        Item sword = itemFactory.createItem("sword_basic");
        
        assertNotNull(sword, "Меч не должен быть null");
        assertEquals("Базовый меч", sword.getName(), "Имя меча должно совпадать");
        assertTrue(sword instanceof Weapon, "Меч должен быть оружием");
        
        Armor helmet = (Armor) itemFactory.createItem("helmet_leather");
        assertNotNull(helmet, "Шлем не должен быть null");
        assertEquals("Кожаный шлем", helmet.getName(), "Имя шлема должно совпадать");
        assertTrue(helmet instanceof Armor, "Шлем должен быть броней");
    }
    
    @Test
    @DisplayName("Создание случайного оружия")
    void testCreateRandomWeapon() {
        Weapon weapon = itemFactory.createRandomWeapon();
        
        assertNotNull(weapon, "Оружие не должно быть null");
        assertTrue(weapon.getDamage() > 0, "Урон оружия должен быть положительным");
        assertNotNull(weapon.getType(), "У оружия должен быть тип");
    }
    
    @Test
    @DisplayName("Создание случайной брони")
    void testCreateRandomArmor() {
        Armor armor = itemFactory.createRandomArmor();
        
        assertNotNull(armor, "Броня не должна быть null");
        assertTrue(armor.getDefense() >= 0, "Защита брони должна быть неотрицательной");
        assertNotNull(armor.getType(), "У брони должен быть тип");
    }
    
    @Test
    @DisplayName("Создание случайного зелья")
    void testCreateRandomPotion() {
        Consumable potion = itemFactory.createRandomPotion();
        
        assertNotNull(potion, "Зелье не должно быть null");
        assertTrue(potion.getEffectValue() > 0, "Эффект зелья должен быть положительным");
        assertNotNull(potion.getType(), "У зелья должен быть тип");
    }
    
    @Test
    @DisplayName("Создание предмета для сокровищницы")
    void testCreateTreasureItem() {
        Item treasure = itemFactory.createTreasureItem();
        
        assertNotNull(treasure, "Предмет сокровища не должен быть null");
        assertTrue(treasure.getValue() >= 40, "Ценность сокровища должна быть высокой");
    }
    
    @Test
    @DisplayName("Создание стартового предмета")
    void testCreateStarterItem() {
        Item starter = itemFactory.createStarterItem();
        
        assertNotNull(starter, "Стартовый предмет не должен быть null");
        assertTrue(starter.getValue() >= 15, "Ценность стартового предмета должна быть разумной");
        assertTrue(starter.getValue() <= 80, "Стартовый предмет не должен быть слишком ценным");
    }
    
    @Test
    @DisplayName("Проверка доступных предметов")
    void testGetAvailableItems() {
        String[] items = itemFactory.getAvailableItems();
        
        assertNotNull(items, "Массив доступных предметов не должен быть null");
        assertTrue(items.length > 0, "Должны быть доступные предметы");
        
        boolean hasSword = false;
        boolean hasArmor = false;
        boolean hasPotion = false;
        
        for (String itemKey : items) {
            if (itemKey.contains("sword")) hasSword = true;
            if (itemKey.contains("helmet") || itemKey.contains("chest")) hasArmor = true;
            if (itemKey.contains("potion")) hasPotion = true;
        }
        
        assertTrue(hasSword, "Должно быть доступно оружие");
        assertTrue(hasArmor, "Должна быть доступна броня");
        assertTrue(hasPotion, "Должны быть доступны зелья");
    }
    
    @Test
    @DisplayName("Паттерн Prototype - клонирование предметов")
    void testPrototypeCloning() {
        Item original = itemFactory.createItem("sword_basic");
        Item cloned = itemFactory.createItem("sword_basic");
        
        assertNotNull(original, "Оригинал не должен быть null");
        assertNotNull(cloned, "Клон не должен быть null");
        
        assertEquals(original.getName(), cloned.getName(), "Имена должны совпадать");
        assertEquals(original.getValue(), cloned.getValue(), "Ценность должна совпадать");
        
        // Проверяем, что это разные объекты
        assertNotSame(original, cloned, "Клон должен быть другим объектом");
        
        // Проверяем для оружия
        if (original instanceof Weapon && cloned instanceof Weapon) {
            Weapon origWeapon = (Weapon) original;
            Weapon cloneWeapon = (Weapon) cloned;
            assertEquals(origWeapon.getDamage(), cloneWeapon.getDamage(), 
                        "Урон должен совпадать");
            assertEquals(origWeapon.getType(), cloneWeapon.getType(), 
                        "Тип должен совпадать");
        }
    }
    
    @Test
    @DisplayName("Обработка несуществующего ключа")
    void testInvalidItemKey() {
        assertThrows(IllegalArgumentException.class, () -> {
            itemFactory.createItem("nonexistent_item");
        }, "Должен выбрасываться исключение для несуществующего ключа");
    }
    
    @Test
    @DisplayName("Типы предметов")
    void testItemTypes() {
        Weapon weapon = itemFactory.createRandomWeapon();
        Armor armor = itemFactory.createRandomArmor();
        Consumable consumable = itemFactory.createRandomPotion();
        
        assertTrue(weapon instanceof Weapon, "Случайное оружие должно быть оружием");
        assertTrue(armor instanceof Armor, "Случайная броня должна быть броней");
        assertTrue(consumable instanceof Consumable, "Случайное зелье должно быть расходуемым предметом");
        
        // Проверяем enum'ы типов
        assertNotNull(weapon.getType(), "У оружия должен быть тип");
        assertNotNull(armor.getType(), "У брони должен быть тип");
        assertNotNull(consumable.getType(), "У зелья должен быть тип");
    }
}
