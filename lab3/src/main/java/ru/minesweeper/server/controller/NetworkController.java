package ru.minesweeper.server.controller;

import ru.minesweeper.server.GameServer;
import ru.minesweeper.server.model.Difficulties;
import ru.minesweeper.server.model.GameModel;

import java.io.IOException;

public class NetworkController {
    GameModel gameModel;
    GameServer gameServer;

    boolean isHostTurn = true;

    public NetworkController(GameModel gameModel, GameServer gameServer) {
        this.gameModel = gameModel;
        this.gameServer = gameServer;
    }

    public void sendInitialState() throws IOException {
        gameModel.sendInitialState();

    }

    public void createEmptyField(Difficulties difficulty, int id) {
        if (isHostTurn&&id==0&&gameServer.getClients().size()<=2) {
            gameModel.setDifficulty(difficulty);
            gameModel.createEmptyField(isHostTurn);
        }
    }

    public void createFillField(int startX, int startY, int id) {
        if (isHostTurn&&id==0) {
            gameModel.createFillField(startX, startY, isHostTurn);
        }
        else if (!isHostTurn&&id!=0) {
            gameModel.createFillField(startX, startY, isHostTurn);
        }
    }

    private void toggleHostTurn() {
        if (gameServer.getCountOfClients()>1) {
            isHostTurn = !isHostTurn;
            if (isHostTurn) {
                gameModel.stopTimerPlayer2();
                gameModel.startTimerPlayer1();
            }
            else {
                gameModel.stopTimerPlayer1();
                gameModel.startTimerPlayer2();
            }
        }
    }


    public void openCell(int x, int y, int id) {
        if (isHostTurn&&id==0) {
            gameModel.openCell(x, y, isHostTurn);
            toggleHostTurn();
        }
        else if (!isHostTurn&&id!=0) {
            gameModel.openCell(x, y, isHostTurn);
            toggleHostTurn();
        }
    }

    public void gameOver() {
        gameModel.stopAllTimers();
    }

    public void addPlayer(String nickname) {
        gameModel.addPlayer(nickname);
    }

    public void removePlayer(String nickname) {
        gameModel.removePlayer(nickname);
    }
}
