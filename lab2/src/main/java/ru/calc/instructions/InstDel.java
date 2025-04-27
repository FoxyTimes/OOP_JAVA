package ru.calc.instructions;

import ru.calc.arguments.BaseArgument;
import ru.calc.context.Context;
import ru.calc.errors.DivisionByZeroException;
import ru.calc.errors.StackPopException;

import java.util.List;

public class InstDel extends BaseInst {
    public InstDel() {
        super();
    }
    @Override
    public void execute(List<BaseArgument<?>> arguments, Context context) {
        if (context.getSizeStack()>1) {
            double a = context.popStack();
            double b = context.popStack();
            if (b==0) {
                context.pushStack(b);
                context.pushStack(a);
                throw new DivisionByZeroException("Division by zero in: " + getClass().getName());
            }
            context.pushStack(a / b);
            return;
        }
        throw new StackPopException("Stack pop error in: " + getClass().getName());
    }
}
