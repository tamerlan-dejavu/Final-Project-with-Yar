package com.example.abyssaldescent.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * HUD (Heads-Up Display) - основной интерфейс игрока.
 * Отображает здоровье, ману, опыт и другую информацию.
 * Паттерн Observer - реагирует на изменения состояния игрока.
 */
public class HUD implements UIObserver {
    
    private final BitmapFont font;
    private int playerHealth;
    private int playerMaxHealth;
    private int playerMana;
    private int playerMaxMana;
    private int playerExperience;
    private int playerLevel;
    
    // Позиции элементов HUD
    private static final int HEALTH_X = 20;
    private static final int HEALTH_Y = 20;
    private static final int MANA_X = 20;
    private static final int MANA_Y = 50;
    private static final int EXP_X = 20;
    private static final int EXP_Y = 80;
    private static final int LEVEL_X = 20;
    private static final int LEVEL_Y = 110;
    
    public HUD() {
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE);
        
        // Начальные значения
        this.playerHealth = 100;
        this.playerMaxHealth = 100;
        this.playerMana = 50;
        this.playerMaxMana = 50;
        this.playerExperience = 0;
        this.playerLevel = 1;
        
        // Регистрируемся как наблюдатель
        UIManager.getInstance().addObserver(this);
    }
    
    /**
     * Отрисовка HUD
     */
    public void render(SpriteBatch batch) {
        // Сохраняем текущий цвет шрифта
        Color originalColor = font.getColor();
        
        // Отрисовка здоровья
        font.setColor(Color.RED);
        font.draw(batch, "HP: " + playerHealth + "/" + playerMaxHealth, HEALTH_X, HEALTH_Y);
        
        // Отрисовка маны
        font.setColor(Color.BLUE);
        font.draw(batch, "MP: " + playerMana + "/" + playerMaxMana, MANA_X, MANA_Y);
        
        // Отрисовка опыта
        font.setColor(Color.GREEN);
        font.draw(batch, "EXP: " + playerExperience, EXP_X, EXP_Y);
        
        // Отрисовка уровня
        font.setColor(Color.YELLOW);
        font.draw(batch, "Level: " + playerLevel, LEVEL_X, LEVEL_Y);
        
        // Восстанавливаем оригинальный цвет
        font.setColor(originalColor);
    }
    
    @Override
    public void update(UIEvent event, Object data) {
        switch (event) {
            case HEALTH_CHANGED:
                if (data instanceof Integer[]) {
                    Integer[] healthData = (Integer[]) data;
                    playerHealth = healthData[0];
                    playerMaxHealth = healthData[1];
                } else if (data instanceof Integer) {
                    playerHealth = (Integer) data;
                }
                break;
                
            case MANA_CHANGED:
                if (data instanceof Integer[]) {
                    Integer[] manaData = (Integer[]) data;
                    playerMana = manaData[0];
                    playerMaxMana = manaData[1];
                } else if (data instanceof Integer) {
                    playerMana = (Integer) data;
                }
                break;
                
            case EXPERIENCE_CHANGED:
                if (data instanceof Integer) {
                    playerExperience = (Integer) data;
                }
                break;
                
            case LEVEL_CHANGED:
                if (data instanceof Integer) {
                    playerLevel = (Integer) data;
                }
                break;
                
            case COMBAT_STARTED:
                Gdx.app.log("HUD", "Бой начался - отображаем боевой режим");
                // TODO: Отобразить боевые индикаторы
                break;
                
            case COMBAT_ENDED:
                Gdx.app.log("HUD", "Бой закончился - скрываем боевые индикаторы");
                // TODO: Скрыть боевые индикаторы
                break;
                
            default:
                // Другие события не обрабатываем в HUD
                break;
        }
    }
    
    @Override
    public String getObserverName() {
        return "HUD";
    }
    
    /**
     * Обновить здоровье игрока
     */
    public void setPlayerHealth(int health, int maxHealth) {
        this.playerHealth = health;
        this.playerMaxHealth = maxHealth;
    }
    
    /**
     * Обновить ману игрока
     */
    public void setPlayerMana(int mana, int maxMana) {
        this.playerMana = mana;
        this.playerMaxMana = maxMana;
    }
    
    /**
     * Обновить опыт игрока
     */
    public void setPlayerExperience(int experience) {
        this.playerExperience = experience;
    }
    
    /**
     * Обновить уровень игрока
     */
    public void setPlayerLevel(int level) {
        this.playerLevel = level;
    }
    
    /**
     * Освободить ресурсы
     */
    public void dispose() {
        font.dispose();
        UIManager.getInstance().removeObserver(this);
    }
}
