package ru.minesweeper.errors;

public class OutOfSizeField extends RuntimeException {
    public OutOfSizeField(String message) {
        super(message);
    }
}
