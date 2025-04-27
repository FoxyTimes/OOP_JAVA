package ru.calc.instructions;

import ru.calc.arguments.BaseArgument;
import ru.calc.context.Context;
import ru.calc.errors.StackPopException;

import java.util.EmptyStackException;
import java.util.List;

public class InstPop extends BaseInst {
    public InstPop() {
        super();
    }


    @Override
    public void execute(List<BaseArgument<?>> arguments, Context context) {
        try {
            context.popStack();
        }
        catch (EmptyStackException e) {
            throw new StackPopException("too low stack in: " + getClass().getName());
        }
    }
}
