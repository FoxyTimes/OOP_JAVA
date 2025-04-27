package ru.calc.instructions;

import ru.calc.arguments.BaseArgument;
import ru.calc.context.Context;
import ru.calc.errors.SqrtNegativeException;
import ru.calc.errors.StackPopException;

import java.util.EmptyStackException;
import java.util.List;

import static java.lang.Math.sqrt;

public class InstSqrt extends BaseInst {
    public InstSqrt() {
        super();
    }
    @Override
    public void execute(List<BaseArgument<?>> arguments, Context context) {
        try {
            if (context.getFirstStack()>-1) {
                double a = context.popStack();
                context.pushStack(sqrt(a));
                return;
            }
            throw new SqrtNegativeException("Negative SQRT number: " + context.getFirstStack());
        }
        catch (EmptyStackException e) {
            throw new StackPopException("Stack pop error in: " + getClass().getName());
        }
    }
}
