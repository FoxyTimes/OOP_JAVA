package ru.calc.instructions;


import ru.calc.arguments.BaseArgument;
import ru.calc.context.Context;

import java.util.List;

abstract public class BaseInst {

    public BaseInst() {}

    public abstract void execute(List<BaseArgument<?>> arguments, Context context);
}
