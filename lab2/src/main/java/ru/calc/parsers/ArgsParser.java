package ru.calc.parsers;

import ru.calc.arguments.BaseArgument;
import ru.calc.arguments.NameArgument;
import ru.calc.arguments.NumberArgument;

import java.util.ArrayList;
import java.util.List;

public class ArgsParser {
    public List<BaseArgument<?>> parseArgs(List<String> args) {
        List<BaseArgument<?>> arguments = new ArrayList<>();
        String arg;
        double doubleArg;
        for (int i = 0; i < args.size(); i++) {
            arg = args.get(i);
            try {
                doubleArg = Double.parseDouble(arg);
                BaseArgument<?> argument = new NumberArgument(doubleArg);
                arguments.add(argument);
            }
            catch (NumberFormatException e) {
                BaseArgument<?> argument = new NameArgument(arg);
                arguments.add(argument);
            }

        }
        return arguments;
    }
}
