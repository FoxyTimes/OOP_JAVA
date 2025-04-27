package ru.calc.instructions;

import ru.calc.arguments.BaseArgument;
import ru.calc.context.Context;
import ru.calc.errors.ArgumentsException;
import ru.calc.errors.FalseTypeException;

import java.util.List;
import java.util.Objects;

public class InstDef extends BaseInst {
    public InstDef() {
        super();
    }
    @Override
    public void execute(List<BaseArgument<?>> arguments, Context context) {
        if (arguments.size() != 2) {
            throw new ArgumentsException("InstDef requires two arguments");
        }
        if (Objects.equals(arguments.getFirst().getType(), "name") && Objects.equals(arguments.get(1).getType(), "number")) {
            BaseArgument<String> nameArgument = (BaseArgument<String>) arguments.getFirst();
            BaseArgument<Double> numberArgument = (BaseArgument<Double>) arguments.get(1);
            if (!context.isDefine(nameArgument.getValue())) {
                context.putDefine(nameArgument.getValue(), numberArgument.getValue());
            }
            return;
        }
        throw new FalseTypeException("instDef requires <NAME> <NUMBER>, but was " + arguments.getFirst().getType() + ": \"" + arguments.getFirst().getValue() + "\" | " + arguments.get(1).getType() + ": \"" + arguments.get(1).getValue() + "\"");
    }
}
