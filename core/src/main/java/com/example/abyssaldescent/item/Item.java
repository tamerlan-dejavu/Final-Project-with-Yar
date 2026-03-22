package com.example.abyssaldescent.item;

/**
 * Базовый класс для всех предметов.
 * Поддерживает паттерн Prototype через метод clone().
 */
public abstract class Item implements Cloneable {
    
    protected String name;
    protected String description;
    protected int value;
    
    public Item(String name, String description, int value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getValue() {
        return value;
    }
    
    /**
     * Создает копию предмета (паттерн Prototype).
     * Подклассы должны переопределить этот метод.
     */
    @Override
    public abstract Item clone();
    
    @Override
    public String toString() {
        return name + " (" + description + ") - " + value + " gold";
    }
}
