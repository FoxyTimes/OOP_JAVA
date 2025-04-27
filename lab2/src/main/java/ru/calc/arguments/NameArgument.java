package ru.calc.arguments;

public class NameArgument implements BaseArgument<String> {
    private String name;
    @Override
    public String getType() {
        return "name";
    }

    public String getValue() {
        return name;
    }

    public NameArgument(String name) {
        this.name = name;
    }
}
