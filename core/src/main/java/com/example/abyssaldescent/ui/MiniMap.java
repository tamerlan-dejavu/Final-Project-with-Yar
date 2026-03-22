package com.example.abyssaldescent.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.abyssaldescent.dungeon.Room;

import java.util.List;

/**
 * Миникарта - отображает текущий подземелье.
 * Паттерн Observer - реагирует на изменения положения игрока и комнат.
 */
public class MiniMap implements UIObserver {
    
    private final BitmapFont font;
    private float playerX;
    private float playerY;
    private List<Room> currentDungeon;
    private Room currentRoom;
    private boolean isVisible;
    
    // Позиции миникарты
    private static final int MAP_X = 600;
    private static final int MAP_Y = 400;
    private static final int MAP_SIZE = 150;
    private static final int ROOM_SIZE = 20;
    
    public MiniMap() {
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE);
        this.font.getData().setScale(0.8f);
        
        this.playerX = 0;
        this.playerY = 0;
        this.isVisible = true;
        
        // Регистрируемся как наблюдатель
        UIManager.getInstance().addObserver(this);
    }
    
    /**
     * Отрисовка миникарты
     */
    public void render(SpriteBatch batch) {
        if (!isVisible || currentDungeon == null) {
            return;
        }
        
        // Сохраняем текущий цвет шрифта
        Color originalColor = font.getColor();
        
        // Рисуем рамку миникарты
        font.setColor(Color.GRAY);
        font.draw(batch, "MiniMap", MAP_X, MAP_Y + MAP_SIZE + 20);
        
        // Рисуем комнаты
        for (Room room : currentDungeon) {
            drawRoom(batch, room);
        }
        
        // Рисуем позицию игрока
        drawPlayer(batch);
        
        // Восстанавливаем оригинальный цвет
        font.setColor(originalColor);
    }
    
    /**
     * Нарисовать комнату на миникарте
     */
    private void drawRoom(SpriteBatch batch, Room room) {
        // Вычисляем позицию комнаты на миникарте
        int roomX = MAP_X + (int) (room.getId().hashCode() % 5) * ROOM_SIZE;
        int roomY = MAP_Y + (int) (room.getId().hashCode() / 5) * ROOM_SIZE;
        
        // Цвет в зависимости от типа комнаты
        switch (room.getType()) {
            case START:
                font.setColor(Color.GREEN);
                break;
            case COMBAT:
                font.setColor(room.isCleared() ? Color.BLUE : Color.RED);
                break;
            case TREASURE:
                font.setColor(Color.YELLOW);
                break;
            case ALTAR:
                font.setColor(Color.PURPLE);
                break;
            case TRANSITION:
                font.setColor(Color.CYAN);
                break;
            default:
                font.setColor(Color.GRAY);
        }
        
        // Рисуем квадрат комнаты
        for (int i = 0; i < ROOM_SIZE / 10; i++) {
            for (int j = 0; j < ROOM_SIZE / 10; j++) {
                font.draw(batch, "█", roomX + i * 10, roomY + j * 10);
            }
        }
    }
    
    /**
     * Нарисовать позицию игрока
     */
    private void drawPlayer(SpriteBatch batch) {
        font.setColor(Color.WHITE);
        int playerMapX = MAP_X + MAP_SIZE / 2;
        int playerMapY = MAP_Y + MAP_SIZE / 2;
        font.draw(batch, "@", playerMapX, playerMapY);
    }
    
    @Override
    public void update(UIEvent event, Object data) {
        switch (event) {
            case PLAYER_MOVED:
                if (data instanceof float[]) {
                    float[] position = (float[]) data;
                    playerX = position[0];
                    playerY = position[1];
                }
                break;
                
            case ROOM_CLEARED:
                if (data instanceof Room) {
                    Room clearedRoom = (Room) data;
                    Gdx.app.log("MiniMap", "Комната зачищена: " + clearedRoom.getId());
                    // Обновляем отображение комнаты
                }
                break;
                
            case LEVEL_CHANGED:
                Gdx.app.log("MiniMap", "Новый уровень - обновляем миникарту");
                // TODO: Очистить и перерисовать миникарту для нового уровня
                break;
                
            case COMBAT_STARTED:
                Gdx.app.log("MiniMap", "Бой начался - подсвечиваем врагов");
                // TODO: Показать позиции врагов на миникарте
                break;
                
            case COMBAT_ENDED:
                Gdx.app.log("MiniMap", "Бой закончился - скрываем врагов");
                // TODO: Скрыть позиции врагов на миникарте
                break;
                
            default:
                // Другие события не обрабатываем в MiniMap
                break;
        }
    }
    
    @Override
    public String getObserverName() {
        return "MiniMap";
    }
    
    /**
     * Установить текущее подземелье
     */
    public void setCurrentDungeon(List<Room> dungeon) {
        this.currentDungeon = dungeon;
        Gdx.app.log("MiniMap", "Установлено новое подземелье с " + 
                   (dungeon != null ? dungeon.size() : 0) + " комнатами");
    }
    
    /**
     * Установить текущую комнату игрока
     */
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }
    
    /**
     * Обновить позицию игрока
     */
    public void setPlayerPosition(float x, float y) {
        this.playerX = x;
        this.playerY = y;
    }
    
    /**
     * Показать/скрыть миникарту
     */
    public void setVisible(boolean visible) {
        this.isVisible = visible;
        Gdx.app.log("MiniMap", "Миникарта " + (visible ? "показана" : "скрыта"));
    }
    
    /**
     * Переключить видимость миникарты
     */
    public void toggleVisibility() {
        setVisible(!isVisible);
    }
    
    /**
     * Проверить, видима ли миникарта
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
