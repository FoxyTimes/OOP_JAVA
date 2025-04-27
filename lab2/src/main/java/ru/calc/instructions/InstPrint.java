package ru.calc.instructions;

import ru.calc.arguments.BaseArgument;
import ru.calc.context.Context;
import ru.calc.errors.EmptyStackContextException;

import java.util.EmptyStackException;
import java.util.List;

public class InstPrint extends BaseInst {
    public InstPrint() {
        super();
    }


    @Override
    public void execute(List<BaseArgument<?>> arguments, Context context) {
        try {
            System.out.println((context.getFirstStack()));
        }
        catch (EmptyStackException e) {
            throw new EmptyStackContextException("Empty stack in: " + getClass().getName());
        }
    }
}
