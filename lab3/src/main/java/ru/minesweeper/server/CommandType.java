package ru.minesweeper.server;

public enum CommandType {
    START_GAME,
    CONNECT_TO_LOBBY,
    DISCONNECT_FROM_LOBBY,
    OPEN_CELL,
    GENERATE_FIELD,
    GAME_OVER,
}