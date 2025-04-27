package ru.minesweeper.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.minesweeper.server.model.Difficulties;

import java.io.*;
import java.util.*;

public class RecordsManager {
    private final String RECORDS_FILE = "records.json";
    private final Gson gson = new Gson();



    public void sendRecord(int time, Difficulties difficulty) {
        try {
            // Читаем существующие рекорды
            Map<String, List<Integer>> records = readRecords();

            // Добавляем новый рекорд
            records.computeIfAbsent(difficulty.toString(), k -> new ArrayList<>()).add(time);

            // Сортируем (лучшие время - первые)
            records.get(difficulty.toString()).sort(Comparator.naturalOrder());

            // Сохраняем обратно
            try (Writer writer = new FileWriter(RECORDS_FILE)) {
                gson.toJson(records, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<Integer>> readRecords() {
        File file = new File(RECORDS_FILE);
        if (!file.exists()) return new HashMap<>();

        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, new TypeToken<Map<String, List<Integer>>>(){}.getType());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}