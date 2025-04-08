package ru.minesweeper.model.entities.datatransfer;

import ru.minesweeper.model.entities.Field;

import java.util.List;
import java.util.Map;

public class DataTransferObject {
    Field field = null;
    int time;
    Map<String, List<Integer>> records = null;

    public DataTransferObject(Map<String, List<Integer>> record) {
        this.records = record;
    }

    public DataTransferObject(Field field, int time) {
        this.field = field;
        this.time = time;
    }

    public Field getData() {
        return field;
    }

    public int getTime() {
        return time;
    }

    public Map<String, List<Integer>> getRecords() {
        return records;
    }
}
