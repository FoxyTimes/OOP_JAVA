package ru.calc.errors;

public class StackPopException extends InstructionExecuteException {
    public StackPopException(String message) {
        super(message);
    }
}
