package ru.calc.errors;

public class FalseTypeException extends InstructionExecuteException {
    public FalseTypeException(String message) {
        super(message);
    }
}
