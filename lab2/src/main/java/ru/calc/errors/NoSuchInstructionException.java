package ru.calc.errors;

public class NoSuchInstructionException extends RuntimeException {
    private String message;
    public NoSuchInstructionException(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
