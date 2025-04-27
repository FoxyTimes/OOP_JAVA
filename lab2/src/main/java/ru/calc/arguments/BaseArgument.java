package ru.calc.arguments;

public interface BaseArgument<T> {
    String getType();
    T getValue();
}
