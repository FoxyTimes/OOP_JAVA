package ru.calc.errors;

public class DivisionByZeroException extends InstructionExecuteException {
    public DivisionByZeroException(String message) {
        super(message);
    }
}
