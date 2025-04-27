package ru.calc.arguments;

public class NumberArgument implements BaseArgument<Double> {
    private double number;
    @Override
    public String getType() {
        return "number";
    }

    @Override
    public Double getValue() {
        return number;
    }

    public NumberArgument(double number) {
        this.number = number;
    }
}
