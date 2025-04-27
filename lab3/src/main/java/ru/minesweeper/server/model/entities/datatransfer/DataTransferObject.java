package ru.minesweeper.server.model.entities.datatransfer;

import ru.minesweeper.server.model.Difficulties;
import ru.minesweeper.server.model.entities.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataTransferObject implements Serializable {



    Field field = null;
    int timePlayer1;
    int timePlayer2;
    Map<String, List<Integer>> records = null;
    Difficulties difficulty = null;
    int x;
    int y;
    boolean turn;
    String nickname = null;
    ArrayList<String> playerNicknamesOnServer = null;
    int newRecord;
    String winner;

    public DataTransferObject(Map<String, List<Integer>> record) {
        this.records = record;
    }

    public DataTransferObject() {}

    public DataTransferObject(ArrayList<String> playerNicknamesOnServer) {
        this.playerNicknamesOnServer = playerNicknamesOnServer;
    }

    public DataTransferObject(String nickname) {
        this.nickname = nickname;
    }

    public DataTransferObject(Field field, boolean turn, ArrayList<String> playerNicknamesOnServer, String winner) {
        this.field = field;
        this.turn = turn;
        this.winner = winner;
        this.playerNicknamesOnServer = playerNicknamesOnServer;
    }

    public DataTransferObject(int timePlayer1, int timePlayer2, boolean isTime) {
        this.timePlayer1 = timePlayer1;
        this.timePlayer2 = timePlayer2;
    }

    public DataTransferObject(int record) {
        this.newRecord = record;
    }

    public DataTransferObject(Difficulties difficulty) {
        this.difficulty = difficulty;
    }

    public DataTransferObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Field getData() {
        return field;
    }

    public String getWinner() {
        return winner;
    }

    public int getTimePlayer1() {
        return timePlayer1;
    }

    public int getTimePlayer2() {
        return timePlayer2;
    }

    public Map<String, List<Integer>> getRecords() {
        return records;
    }

    public Difficulties getDifficulty() {
        return difficulty;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public int getNewRecord() {
        return newRecord;
    }

    public String getNickname() {
        return nickname;
    }

    public ArrayList<String> getPlayerNicknamesOnServer() {
        return playerNicknamesOnServer;
    }

    public boolean getTurn() {
        return turn;
    }
}
