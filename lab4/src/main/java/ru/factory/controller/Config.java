package ru.factory.controller;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final Properties prop = new Properties();
    public void load(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            prop.load(inputStream);
        }
        catch (Exception e) {
            throw new RuntimeException("Error loading config file: " + fileName, e);
        }
    }
    public <T> T getValue(String key, Class<T> type) {
        String value = prop.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Config key not found: " + key);
        }

        // Преобразуем строку в тип T
        if (type == String.class) {
            return type.cast(value);  // Преобразуем в строку
        } else if (type == Integer.class) {
            return type.cast(Integer.parseInt(value));  // Преобразуем в int
        } else if (type == Boolean.class) {
            return type.cast(Boolean.parseBoolean(value));  // Преобразуем в boolean
        } else if (type == Double.class) {
            return type.cast(Double.parseDouble(value));  // Преобразуем в double
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type.getName());
        }
    }
}
