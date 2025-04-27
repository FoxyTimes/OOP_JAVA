package ru.calc.errors;

public class EmptyStackContextException extends InstructionExecuteException {
    public EmptyStackContextException(String message) {
        super(message);
    }
}
