package ru.minesweeper.client.view.gui.screens;

public enum ScreensE {
    SCREEN_GAME_MENU ("screenGameMenu"),
    SCREEN_OPTIONS_MENU ("optionsMenu"),
    SCREEN_CLIENT_MENU ("clientMenu"),
    SCREEN_MULTI_MENU ("multiMenu"),
    SCREEN_RECORD_MENU ("screenRecordMenu"),
    SCREEN_MULTI_PROCESS ("screenMultiProcess"),
    SCREEN_SINGLE_PROCESS ("screenSingleProcess"),
    SCREEN_LOBBY_MENU ("lobbyMenu"),
    SCREEN_DIFFICULTY_MENU ("difficultyMenu");

    final String name;

    ScreensE(String name) {
        this.name = name;
    };

    public String getName() {
        return name;
    }
}
