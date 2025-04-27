package ru.calc.instructions;

import ru.calc.arguments.BaseArgument;
import ru.calc.context.Context;
import ru.calc.errors.ArgumentsException;
import ru.calc.errors.NotDefinedVariableException;

import java.util.List;
import java.util.Objects;

public class InstPush extends BaseInst {
    public InstPush() {
        super();
    }


    @Override
    public void execute(List<BaseArgument<?>> arguments, Context context) {
        if (arguments.size() != 1) {
            throw new ArgumentsException("InstPush requires one argument");
        }
        if (Objects.equals(arguments.getFirst().getType(), "number")) {
            BaseArgument<Double> doubleArgument = (BaseArgument<Double>) arguments.getFirst();
            context.pushStack(doubleArgument.getValue());
            return;
        }
        else if (Objects.equals(arguments.getFirst().getType(), "name")) {
            BaseArgument<String> nameArgument = (BaseArgument<String>) arguments.getFirst();
            if (context.isDefine(nameArgument.getValue())) {
                context.pushStack(context.getDefine(nameArgument.getValue()));
                return;
            }
            throw new NotDefinedVariableException("the variable is not defined: " + nameArgument.getValue());
        }
    }
}
