package ru.calc.instructions;

import ru.calc.arguments.BaseArgument;
import ru.calc.context.Context;
import ru.calc.errors.StackPopException;

import java.util.List;

public class InstMin extends BaseInst {
    public InstMin() {
        super();
    }
    @Override
    public void execute(List<BaseArgument<?>> arguments, Context context) {
        if (context.getSizeStack()>1) {
            double a = context.popStack();
            double b = context.popStack();
            context.pushStack(a - b);
            return;
        }
        throw new StackPopException("too low stack in: " + getClass().getName());
    }
}
