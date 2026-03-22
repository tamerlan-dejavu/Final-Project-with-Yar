package com.example.abyssaldescent.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.abyssaldescent.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Инвентарь - отображает предметы игрока.
 * Паттерн Observer - реагирует на изменения в инвентаре.
 */
public class InventoryUI implements UIObserver {
    
    private final BitmapFont font;
    private final List<Item> inventory;
    private boolean isVisible;
    private int selectedSlot;
    private int maxSlots;
    
    // Позиции инвентаря
    private static final int INV_X = 200;
    private static final int INV_Y = 100;
    private static final int SLOT_SIZE = 40;
    private static final int SLOTS_PER_ROW = 8;
    
    public InventoryUI() {
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE);
        this.font.getData().setScale(0.7f);
        
        this.inventory = new ArrayList<>();
        this.isVisible = false;
        this.selectedSlot = 0;
        this.maxSlots = 32; // 4x8 слотов
        
        // Регистрируемся как наблюдатель
        UIManager.getInstance().addObserver(this);
    }
    
    /**
     * Отрисовка инвентаря
     */
    public void render(SpriteBatch batch) {
        if (!isVisible) {
            return;
        }
        
        // Сохраняем текущий цвет шрифта
        Color originalColor = font.getColor();
        
        // Рисуем заголовок
        font.setColor(Color.WHITE);
        font.draw(batch, "Inventory", INV_X, INV_Y + 20);
        
        // Рисуем слоты инвентаря
        drawInventorySlots(batch);
        
        // Рисуем предметы
        drawItems(batch);
        
        // Рисуем информацию о выбранном предмете
        drawSelectedItemInfo(batch);
        
        // Восстанавливаем оригинальный цвет
        font.setColor(originalColor);
    }
    
    /**
     * Нарисовать слоты инвентаря
     */
    private void drawInventorySlots(SpriteBatch batch) {
        for (int i = 0; i < maxSlots; i++) {
            int row = i / SLOTS_PER_ROW;
            int col = i % SLOTS_PER_ROW;
            int slotX = INV_X + col * SLOT_SIZE;
            int slotY = INV_Y - row * SLOT_SIZE;
            
            // Подсветка выбранного слота
            if (i == selectedSlot) {
                font.setColor(Color.YELLOW);
            } else {
                font.setColor(Color.GRAY);
            }
            
            // Рисуем рамку слота
            font.draw(batch, "┌", slotX, slotY);
            font.draw(batch, "┐", slotX + SLOT_SIZE - 10, slotY);
            font.draw(batch, "└", slotX, slotY - SLOT_SIZE + 10);
            font.draw(batch, "┘", slotX + SLOT_SIZE - 10, slotY - SLOT_SIZE + 10);
            
            // Рисуем горизонтальные линии
            for (int j = 1; j < (SLOT_SIZE - 10) / 8; j++) {
                font.draw(batch, "─", slotX + j * 8, slotY);
                font.draw(batch, "─", slotX + j * 8, slotY - SLOT_SIZE + 10);
            }
            
            // Рисуем вертикальные линии
            for (int j = 1; j < (SLOT_SIZE - 10) / 16; j++) {
                font.draw(batch, "│", slotX, slotY - j * 16);
                font.draw(batch, "│", slotX + SLOT_SIZE - 10, slotY - j * 16);
            }
        }
    }
    
    /**
     * Нарисовать предметы в слотах
     */
    private void drawItems(SpriteBatch batch) {
        font.setColor(Color.WHITE);
        
        for (int i = 0; i < inventory.size() && i < maxSlots; i++) {
            Item item = inventory.get(i);
            int row = i / SLOTS_PER_ROW;
            int col = i % SLOTS_PER_ROW;
            int itemX = INV_X + col * SLOT_SIZE + 5;
            int itemY = INV_Y - row * SLOT_SIZE - 5;
            
            // Рисуем иконку предмета (первые 2 буквы названия)
            String icon = item.getName().substring(0, Math.min(2, item.getName().length()));
            font.draw(batch, icon, itemX, itemY);
            
            // Рисуем количество, если предметов > 1
            // TODO: Добавить систему количества предметов
        }
    }
    
    /**
     * Нарисовать информацию о выбранном предмете
     */
    private void drawSelectedItemInfo(SpriteBatch batch) {
        if (selectedSlot < inventory.size()) {
            Item selectedItem = inventory.get(selectedSlot);
            
            font.setColor(Color.CYAN);
            font.draw(batch, "Selected:", INV_X, INV_Y - 150);
            font.draw(batch, selectedItem.getName(), INV_X, INV_Y - 170);
            font.draw(batch, selectedItem.getDescription(), INV_X, INV_Y - 190);
            font.draw(batch, "Value: " + selectedItem.getValue() + " gold", INV_X, INV_Y - 210);
        }
    }
    
    @Override
    public void update(UIEvent event, Object data) {
        switch (event) {
            case ITEM_ADDED:
                if (data instanceof Item) {
                    addItem((Item) data);
                }
                break;
                
            case ITEM_REMOVED:
                if (data instanceof Item) {
                    removeItem((Item) data);
                } else if (data instanceof Integer) {
                    removeItemByIndex((Integer) data);
                }
                break;
                
            case ITEM_COUNT_CHANGED:
                // TODO: Обновить количество предметов
                Gdx.app.log("InventoryUI", "Количество предметов изменено");
                break;
                
            default:
                // Другие события не обрабатываем в InventoryUI
                break;
        }
    }
    
    @Override
    public String getObserverName() {
        return "InventoryUI";
    }
    
    /**
     * Добавить предмет в инвентарь
     */
    public void addItem(Item item) {
        if (inventory.size() < maxSlots) {
            inventory.add(item);
            Gdx.app.log("InventoryUI", "Добавлен предмет: " + item.getName());
        } else {
            Gdx.app.log("InventoryUI", "Инвентарь полон!");
        }
    }
    
    /**
     * Удалить предмет из инвентаря
     */
    public void removeItem(Item item) {
        if (inventory.remove(item)) {
            Gdx.app.log("InventoryUI", "Удален предмет: " + item.getName());
            
            // Корректируем выбранный слот
            if (selectedSlot >= inventory.size() && selectedSlot > 0) {
                selectedSlot--;
            }
        }
    }
    
    /**
     * Удалить предмет по индексу
     */
    public void removeItemByIndex(int index) {
        if (index >= 0 && index < inventory.size()) {
            Item removed = inventory.remove(index);
            Gdx.app.log("InventoryUI", "Удален предмет: " + removed.getName());
            
            // Корректируем выбранный слот
            if (selectedSlot >= inventory.size() && selectedSlot > 0) {
                selectedSlot--;
            }
        }
    }
    
    /**
     * Показать/скрыть инвентарь
     */
    public void setVisible(boolean visible) {
        this.isVisible = visible;
        Gdx.app.log("InventoryUI", "Инвентарь " + (visible ? "показан" : "скрыт"));
    }
    
    /**
     * Переключить видимость инвентаря
     */
    public void toggleVisibility() {
        setVisible(!isVisible);
    }
    
    /**
     * Выбрать следующий слот
     */
    public void selectNextSlot() {
        selectedSlot = (selectedSlot + 1) % Math.min(maxSlots, inventory.size());
    }
    
    /**
     * Выбрать предыдущий слот
     */
    public void selectPreviousSlot() {
        selectedSlot = (selectedSlot - 1 + Math.min(maxSlots, inventory.size())) % 
                      Math.min(maxSlots, inventory.size());
    }
    
    /**
     * Получить текущий выбранный предмет
     */
    public Item getSelectedItem() {
        if (selectedSlot < inventory.size()) {
            return inventory.get(selectedSlot);
        }
        return null;
    }
    
    /**
     * Использовать выбранный предмет
     */
    public void useSelectedItem() {
        Item selectedItem = getSelectedItem();
        if (selectedItem != null) {
            // TODO: Реализовать использование предмета
            Gdx.app.log("InventoryUI", "Использован предмет: " + selectedItem.getName());
            
            // Если расходуемый предмет, удаляем его
            // if (selectedItem instanceof Consumable) {
            //     removeItem(selectedItem);
            // }
        }
    }
    
    /**
     * Получить количество предметов в инвентаре
     */
    public int getItemCount() {
        return inventory.size();
    }
    
    /**
     * Проверить, видим ли инвентарь
     */
    public boolean isVisible() {
        return isVisible;
    }
    
    /**
     * Освободить ресурсы
     */
    public void dispose() {
        font.dispose();
        UIManager.getInstance().removeObserver(this);
    }
}
