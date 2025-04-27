package ru.calc.errors;

public class InstructionExecuteException extends RuntimeException {
    private String message;
    public InstructionExecuteException(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
