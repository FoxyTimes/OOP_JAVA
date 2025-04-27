package ru.calc.errors;

public class ArgumentsException extends InstructionExecuteException {
    public ArgumentsException(String message) {
        super(message);
    }
}
