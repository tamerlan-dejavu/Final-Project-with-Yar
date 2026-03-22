package com.example.abyssaldescent.dungeon;

import java.util.List;

/**
 * Фабрика для создания подземелий.
 * Паттерн Abstract Factory - создает семейства связанных объектов (комнат, дверей).
 */
public interface DungeonFactory {
    
    /**
     * Создает подземелье указанного размера
     * @param width ширина подземелья
     * @param height высота подземелья
     * @return список комнат подземелья
     */
    List<Room> createDungeon(int width, int height);
    
    /**
     * Создает комнату указанного типа
     * @param type тип комнаты
     * @param x координата X
     * @param y координата Y
     * @return созданная комната
     */
    Room createRoom(RoomType type, int x, int y);
    
    /**
     * Создает дверь между двумя комнатами
     * @param room1 первая комната
     * @param room2 вторая комната
     * @return созданная дверь
     */
    Door createDoor(Room room1, Room room2);
    
    /**
     * Возвращает сложность подземелья
     * @return уровень сложности
     */
    String getDifficulty();
}
