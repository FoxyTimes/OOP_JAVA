package ru.calc.errors;

public class NotDefinedVariableException extends InstructionExecuteException {

    public NotDefinedVariableException(String message) {
        super(message);
    }
}
