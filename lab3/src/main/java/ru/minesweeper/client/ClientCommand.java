package ru.minesweeper.client;

import ru.minesweeper.server.model.Difficulties;
import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.server.CommandType;

import java.io.Serializable;

public class ClientCommand implements Serializable {
    private final CommandType type;
    private DataTransferObject data=null;

    public ClientCommand(CommandType type, int x, int y) {
        this.type = type;
        this.data = new DataTransferObject(x, y);
    }

    public ClientCommand(CommandType type) {
        this.type = type;
    }

    public ClientCommand(CommandType type, String nickname) {
        this.type = type;
        this.data = new DataTransferObject(nickname);
    }

    public ClientCommand(CommandType type, Difficulties difficulty) {
        this.type = type;
        this.data = new DataTransferObject(difficulty);
    }

    public ClientCommand(CommandType type, int time) {
        this.type = type;
        this.data = new DataTransferObject(time);
    }

    public CommandType getType() {
        return type;
    }

    public DataTransferObject getData() {
        return data;
    }
}