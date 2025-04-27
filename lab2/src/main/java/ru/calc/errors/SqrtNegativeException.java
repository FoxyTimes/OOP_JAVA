package ru.calc.errors;

public class SqrtNegativeException extends InstructionExecuteException {
  public SqrtNegativeException(String message) {
    super(message);
  }
}
