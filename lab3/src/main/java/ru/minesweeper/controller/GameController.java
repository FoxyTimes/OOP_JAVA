package ru.minesweeper.controller;

import ru.minesweeper.Difficulties;
import ru.minesweeper.model.GameModel;

public class GameController {

    GameModel model;
    RecordsManager recordsManager;

    public GameController(GameModel model) {
        this.model = model;
        this.recordsManager = new RecordsManager();
    }

    public void createEmptyField(Difficulties difficulty) {
        model.setDifficulty(difficulty);
        model.createEmptyField();
    }

    public void createFillField(int startX, int startY) {
        model.createFillField(startX, startY);
    }

    public void openCell(int x, int y) {
        model.openCell(x, y);
    }

    public void sendRecord(int time) {
        recordsManager.sendRecord(time, model.getDifficulty()); //????
        updateRecords();
    }

    public void GameOver() {
        model.stopTimer();
    }

    public void updateRecords() {
        model.updateRecords(recordsManager.readRecords());
    }
}
